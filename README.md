🎉 Welcome to the DIY DNS Server 🎉

![App Screenshot](https://www.indusface.com/wp-content/uploads/2024/10/DNS-lookup-process-.png)

📡 About This Project
Ever wondered how the Internet works behind the scenes? 🧐 When you type a domain like www.google.com into your browser, it magically turns into an IP address. But how? 🤔 This transformation is done by DNS (Domain Name System), and guess what? You’re about to learn how to build your very own DNS server!

This project simulates a simple DNS server that resolves domain names to IP addresses. Using Java and some clever networking techniques, we’ve built a small yet functional DNS server capable of answering queries for predefined domain names.

Ready to dive into the world of DNS? 🌐🚀

🚀 How to Get Started
1. Clone the Repository
   First, you’ll need to clone this repository to your local machine. Open your terminal and run the following command:

bash
Copy
Edit
git clone https://github.com/your-username/dns-server-java.git
cd dns-server-java
2. Set Up Java Development Environment
   This project is built with Java 17+. If you don’t have it installed, head over to Oracle's JDK download page and get Java set up.

Alternatively, if you're using Amazon Corretto, you can download it from here.

3. Run the DNS Server
   To run the DNS server, simply compile and execute the Main.java file:

bash
Copy
Edit
javac Main.java
java Main
This will start the DNS server on port 53, the default port for DNS queries.

4. Test the Server
   Once the server is running, open another terminal and use nslookup to query the DNS server:

bash
Copy
Edit
nslookup www.google.com 127.0.0.1
The server will resolve www.google.com to the predefined IP address 93.184.216.34. 🎉

🛠️ How the DNS Server Works
The DNS Request & Response Flow
When you query a domain name (e.g., www.google.com), the DNS server follows these steps:

Receiving a Query: The server listens on port 53 and waits for incoming DNS queries.
Processing the Query: When it receives a request, it parses the domain name from the query.
Looking Up the Domain: It checks if the domain name is in its preconfigured DNS records.
Sending a Response: If the domain is found, it sends back the associated IP address (e.g., 93.184.216.34); otherwise, it returns a "not found" message.
🏗️ Understanding the Code
Let's break down the different classes in this project:

1. Main.java – The DNS Server
   This is where the magic happens! The Main class is the entry point for the DNS server. It initializes the server and starts listening for incoming requests. Here's the step-by-step flow:

Initialize Server: The server listens on port 53 for incoming UDP packets (the standard protocol for DNS).
Handle Requests: When a request is received, it’s passed to DNSHandler to process and send a response.
Respond to Queries: The response is sent back to the client after processing.
Key Methods:
main(String[] args): Starts the server and listens for requests.
socket.receive(requestPacket): Blocks and waits for incoming requests.
socket.send(responsePacket): Sends the response back to the client.
2. DNSHandler.java – Handling DNS Queries
   This class handles the logic of responding to DNS queries. When a DNS query is received, this class does the following:

Parse the DNS Packet: It decodes the received byte array into a DNS packet.
Resolve Domain: It checks if the requested domain exists in the pre-configured DNS records.
Generate Response: If the domain is found, it adds the corresponding IP to the response. If not, it sends a "Domain Not Found" message.
Key Methods:
handleQuery(byte[] requestedData): Handles the entire DNS query process – parses the data, resolves the IP, and creates a response.
fromBytes(byte[] data): Converts the received byte data into a DNSPacket object, parsing the transaction ID and domain name.
createResponse(): Creates a new DNSPacket object as a response, reusing the transaction ID and setting the response flag.
addAnswer(String domainName, String ipAddress): Adds the resolved domain name and IP address to the response.
3. DNSPacket.java – Building and Parsing DNS Packets
   This class is the backbone of packet handling. It’s responsible for creating DNS requests and responses, as well as parsing incoming packets.

Parsing the Request: It takes in the byte array from the incoming request and extracts the transaction ID and domain name.
Constructing the Response: It builds a DNS response packet, which includes the original transaction ID, the resolved IP address, and other DNS record information.
Key Methods:
fromBytes(byte[] data): Parses incoming DNS query packets.
createResponse(): Creates a response DNS packet with the original transaction ID.
toBytes(DNSPacket packet): Converts the response packet to a byte array for sending back to the client.
4. DNSRecord.java – Storing DNS Records
   This class stores DNS records (domain name and IP address). It’s a simple Java object that holds the domain name and its associated IP address. It’s used when adding an answer to the response packet.

Key Methods:
toBytes(): Converts the DNS record (domain name and IP address) into a byte array to be included in the DNS response.
🌐 DNS Basics: What’s Happening Under the Hood?
What is DNS?
DNS (Domain Name System) is like the phone book of the internet. It translates human-readable domain names (like www.google.com) into machine-readable IP addresses (like 93.184.216.34). This process is essential because, while humans prefer easy-to-remember names, computers communicate using numerical IP addresses.

How DNS Works
When you want to visit a website, your computer sends a DNS query to a DNS server, asking for the IP address associated with the domain name. If the DNS server has the record, it sends back the IP address. If not, it can forward the request to other DNS servers until an answer is found.

Parsing and Building DNS Packets
DNS packets follow a specific structure. They are divided into several sections:

Header: Contains metadata like transaction ID, flags, and counts of the sections.
Question: Contains the domain name and query type (e.g., A record for IPv4 addresses).
Answer: Contains the resolved domain name and its associated IP address.
Additional Sections: Can include additional information, but we are keeping it simple for this implementation.
🔍 Why This is Awesome
Educational: This project is a great way to understand how DNS works at a low level. You get hands-on experience with networking, byte manipulation, and packet handling.
Customizable: You can easily add more DNS records to resolve more domains.
Lightweight: This server is small, simple, and built using only standard Java libraries.
Practical: It works in real-world scenarios (tested with nslookup and compatible with any DNS client).
📜 Conclusion
By building this DNS server, we’ve gone through the fascinating journey of networking, parsing, and manipulating raw bytes to resolve domain names into IP addresses. Whether you're a beginner or an experienced developer, this project offers a simple yet powerful way to understand the core principles of how the internet works.

So, next time you visit a website, you’ll know exactly what happens behind the scenes. 🌍🚀

💡 What’s Next?
Feel free to contribute! 🚀 Open an issue or submit a pull request if you want to add features or improve the server.

Happy coding, and enjoy exploring the world of DNS! 🎉

✨ Don’t forget to star this repo if you found it useful! ✨