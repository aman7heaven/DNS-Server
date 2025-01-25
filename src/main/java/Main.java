import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Main class to simulate a basic DNS server using UDP.
 * The server listens on port 53 for DNS queries, processes them, and responds.
 */
public class Main {

    public static void main(String[] args) {
        // Default port for DNS servers
        final int port = 53;

        // DatagramSocket for listening to incoming DNS queries
        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("DNS Server is running on port " + port + "...");

            // DNS packets are typically no more than 512 bytes (as per RFC 1035)
            byte[] buffer = new byte[512];

            // Infinite loop to handle incoming DNS queries
            while (true) {
                try {
                    // 1. Receive a DNS query
                    DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(requestPacket); // Blocks until a packet is received
                    System.out.println("Received DNS query from " + requestPacket.getAddress() + ":" + requestPacket.getPort());

                    // 2. Process the query and generate a response
                    byte[] response = DNSHandler.handleQuery(requestPacket.getData());
                    System.out.println("Processed DNS query and generated response.");

                    // 3. Send the response back to the client
                    DatagramPacket responsePacket = new DatagramPacket(
                            response,
                            response.length,
                            requestPacket.getAddress(),
                            requestPacket.getPort()
                    );
                    socket.send(responsePacket);
                    System.out.println("Sent DNS response to " + requestPacket.getAddress() + ":" + requestPacket.getPort());
                } catch (IOException e) {
                    // Log error for the current request but allow the server to continue
                    System.err.println("Error processing DNS request: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (SocketException e) {
            // Log critical error and exit
            System.err.println("Failed to initialize DNS server on port " + port + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("DNS Server initialization failed", e);
        }
    }
}
