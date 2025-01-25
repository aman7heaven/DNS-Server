import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * The DNSPacket class represents a DNS query or response packet. It provides methods to parse,
 * generate, and manage DNS packets, including handling questions, answers, and other DNS record
 * sections.
 */
public class DNSPacket {

    private byte[] transactionId; // Transaction ID for the query/response
    private Boolean isQuery; // Flag indicating whether this is a query or response
    private String domainName; // The domain name in the DNS query/response
    private static List<DNSRecord> answers; // List to store DNS records (answers)

    public DNSPacket() {
        answers = new ArrayList<>();  // Initialize the answers list in the constructor
    }

    /**
     * Gets the domain name of the DNS packet.
     *
     * @return The domain name as a string.
     */
    public String getDomainName() {
        return this.domainName;
    }

    /**
     * Parses the raw byte data of a DNS packet and converts it into a DNSPacket object.
     *
     * @param data The raw byte data of the DNS packet.
     * @return The parsed DNSPacket object.
     */
    public static DNSPacket fromBytes(byte[] data) {
        DNSPacket dnsPacket = new DNSPacket();
        ByteBuffer buffer = ByteBuffer.wrap(data);

        // Header section (12 bytes)
        dnsPacket.transactionId = new byte[2]; // 2 bytes for the transaction ID
        buffer.get(dnsPacket.transactionId);

        // Skip the Flags and Questions sections (2 bytes each)
        buffer.getShort(); // Flags
        buffer.getShort(); // Number of Questions

        // Skip Answer RRs, Authority RRs, Additional RRs (2 bytes each)
        buffer.getShort(); // Answer RRs
        buffer.getShort(); // Authority RRs
        buffer.getShort(); // Additional RRs

        // Question section
        StringBuilder domainName = new StringBuilder();
        int length;

        // Read the domain name, which is split into labels. Each label starts with a length byte.
        while ((length = buffer.get() & 0xff) > 0) {
            byte[] label = new byte[length];
            buffer.get(label);
            domainName.append(new String(label)).append("."); // Add label to domain name
        }

        // Remove the trailing "." from the domain name
        dnsPacket.domainName = domainName.substring(0, domainName.length() - 1);

        return dnsPacket; // Return the constructed DNSPacket object
    }

    /**
     * Creates a DNS response packet based on this DNS query packet.
     *
     * @return A DNSPacket object representing the response to this query.
     */
    public DNSPacket createResponse() {
        DNSPacket response = new DNSPacket();
        response.transactionId = this.transactionId; // Reuse the same transaction ID
        response.isQuery = false; // Set the response flag to false (this is a response, not a query)
        response.domainName = this.domainName; // Set the domain name for the response
        return response;
    }

    /**
     * Adds an answer (DNS record) to the DNS packet's answers list.
     *
     * @param domainName The domain name of the record.
     * @param ipAddress  The IP address associated with the domain name.
     */
    public void addAnswer(String domainName, String ipAddress) {
        DNSRecord dnsRecord = new DNSRecord(domainName, ipAddress); // Create a new DNS record
        if (answers == null) {
            answers = new ArrayList<>(); // Initialize the list if it's not already initialized
        }
        answers.add(dnsRecord); // Add the DNS record to the list of answers
    }

    /**
     * Converts the DNSPacket object into a byte array representation that can be sent over the network.
     *
     * @param packet The DNSPacket object to convert.
     * @return A byte array representing the DNS packet.
     * @throws UnknownHostException If the IP address is not valid.
     */
    public byte[] toBytes(DNSPacket packet) throws UnknownHostException {
        ByteBuffer buffer = ByteBuffer.allocate(512); // DNS packet size (usually no more than 512 bytes)

        // Step 1: Write the transaction ID
        buffer.put(transactionId); // Write the 2-byte transaction ID

        // Step 2: Write the Flags (response flag set and success code)
        buffer.putShort((short) 0x8180); // 0x8180: Response (1) and success (0) flag

        // Step 3: Write the Questions and Answer RRs counts
        buffer.putShort((short) 1); // One question in this response
        buffer.putShort((short) answers.size()); // Number of answers (DNS records)
        buffer.putShort((short) 0); // Authority RRs (none)
        buffer.putShort((short) 0); // Additional RRs (none)

        // Step 4: Write the domain name question
        // Each domain name is written as labels, starting with a byte indicating the length of the label.
        for (String label : domainName.split("\\.")) {
            buffer.put((byte) label.length()); // Write the length of the label
            buffer.put(label.getBytes()); // Write the label itself
        }
        buffer.put((byte) 0); // End of the domain name (null byte)

        // Step 5: Write the Type and Class for the query
        buffer.putShort((short) 1); // Type A (Address record)
        buffer.putShort((short) 1); // Class IN (Internet)

        // Step 6: Write each answer (DNS records)
        // For each DNS record in the answers list, we convert it to bytes and append it to the buffer
        for (DNSRecord answer : answers) {
            buffer.put(answer.toBytes()); // Convert the DNS record to bytes and add it to the buffer
        }

        // Step 7: Return the byte array representation of the DNS packet
        return buffer.array(); // Convert the buffer to a byte array
    }
}
