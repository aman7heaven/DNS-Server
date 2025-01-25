import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * The DNSRecord class represents a single DNS record, typically an A record (which maps domain names to IP addresses).
 * It provides methods to convert the DNS record into a byte array that can be included in a DNS response.
 */
public class DNSRecord {
    private String domainName;
    private String ipAddress;

    /**
     * Constructor to initialize a DNS record with a domain name and an IP address.
     *
     * @param domainName The domain name for the record.
     * @param ipAddress  The IP address associated with the domain name.
     */
    public DNSRecord(String domainName, String ipAddress) {
        this.domainName = domainName;
        this.ipAddress = ipAddress;
    }

    /**
     * Converts the DNS record into a byte array that can be included in a DNS response packet.
     *
     * @return A byte array representing the DNS record.
     * @throws UnknownHostException If the IP address is invalid or cannot be resolved.
     */
    public byte[] toBytes() throws UnknownHostException {
        ByteBuffer buffer = ByteBuffer.allocate(32);

        // Step 1: Write the domain name
        // The domain name is split into labels (substrings separated by '.').
        // Each label is preceded by a byte indicating its length.
        for (String label : domainName.split("\\.")) {
            buffer.put((byte) label.length());
            buffer.put(label.getBytes());
        }
        buffer.put((byte) 0); // End of the domain name (null byte)

        // Step 2: Write the Type and Class
        // In this example, we're creating an A record (IPv4 address).
        buffer.putShort((short) 1); // Type A (Address record)
        buffer.putShort((short) 1); // Class IN (Internet)

        // Step 3: Write the TTL (Time to Live)
        // TTL is typically set to 3600 seconds (1 hour) for simplicity.
        buffer.putInt(3600); // TTL in seconds

        // Step 4: Write the Resource Data Length
        // For an A record, the length of the resource data is always 4 bytes (IPv4 address).
        buffer.putShort((short) 4); // IPv4 address length

        // Step 5: Write the IP address
        // Convert the IP address from a string to a byte array and write it to the buffer.
        byte[] ipBytes = InetAddress.getByName(ipAddress).getAddress(); // Convert IP address to byte array
        buffer.put(ipBytes);

        return buffer.array();
    }
}
