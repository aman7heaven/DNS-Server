import java.util.HashMap;
import java.util.Map;

/**
 * DNSHandler class is responsible for processing incoming DNS queries,
 * resolving domain names to IP addresses, and generating appropriate DNS responses.
 */
public class DNSHandler {

    // A simple map to store hardcoded DNS records (domain name -> IP address)
    // In a real-world scenario, DNS records would be fetched from a database or external service.
    public static final Map<String, String> DNS_RECORDS = new HashMap<>();

    // Static block to populate DNS records
    static {
        // Adding some sample domain name to IP address mappings
        // Add some sample records
        DNS_RECORDS.put("www.google.com", "93.184.216.34");
        DNS_RECORDS.put("www.youtube.com", "127.0.0.1");
        // These mappings are for demonstration; they can be modified to fit the real use case.
    }

    /**
     * Processes a DNS query, resolves the domain name to an IP address, and generates the response.
     *
     * @param requestedData The raw byte data representing the incoming DNS query.
     * @return A byte array representing the DNS response. The response will contain the resolved IP if the domain is found.
     */
    public static byte[] handleQuery(byte[] requestedData) {
        try {
            // Step 1: Parse the incoming query
            // Convert the raw byte data into a DNSPacket object which contains the query information.
            DNSPacket query = DNSPacket.fromBytes(requestedData);

            // Print the requested domain name to the console (for logging purposes)
            System.out.println("Requested domain: " + query.getDomainName());

            // Step 2: Prepare the DNS response
            // Create a new DNSPacket object for the response.
            DNSPacket response = query.createResponse();

            // Step 3: Lookup the domain name in the DNS_RECORDS map to resolve the IP address
            String resolvedIp = DNS_RECORDS.get(query.getDomainName());

            // Step 4: Check if the domain was found in the records
            if (resolvedIp != null) {
                // If the domain was found, add the resolved IP address as an answer to the response.
                // The addAnswer method adds the IP address to the response in the appropriate format.
                response.addAnswer(query.getDomainName(), resolvedIp);
                System.out.println("Resolved IP: " + resolvedIp);
            } else {
                // If the domain was not found, log a message to indicate this.
                System.out.println("Domain not found: " + query.getDomainName());
            }

            // Step 5: Return the response in byte format
            // Convert the DNSPacket response to a byte array and return it.
            // The response byte array will be sent back to the client.
            return response.toBytes(response);

        } catch (Exception e) {
            // If there is any error during the query processing, print the stack trace.
            // Return an empty byte array to indicate that an error occurred.
            e.printStackTrace();
            return new byte[0]; // Return an empty response in case of an error
        }
    }
}
