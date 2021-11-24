import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * Client controller, gets everything up and running before launching two threads
 * @author Ithan Velarde, Taha Mdarhri, Aichetou M'Bareck
 */
public class Client {
    /**
     * Name of this client
     */
    private static String name;
    /**
     * Socket from which the client will read or write to
     */
    private static MulticastSocket socket;
    /**
     * Inet Address of the client
     */
    private static InetAddress group;
    /**
     * Port in which the client is listening
     */
    private static int port;
    /**
     * Thread used to send messages
     */
    private static Thread send;
    /**
     * Thread used to send messages
     */
    private static Thread receive;

    /**
     * Constructor of the client
     * @param name name of the client
     * @param socket Socket from which the client will read or write to
     * @param group Inet Address of the client
     * @param port Port in which the client is listening
     */
    public Client(String name, MulticastSocket socket, InetAddress group, int port){
        this.name = name;
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    /**
     * Sets a new thread into the send thread
     * @param send new thread to be set
     */
    public void setSend(Thread send) {
        Client.send = send;
    }
    /**
     * Sets a new thread into the receive thread
     * @param receive new thread to be set
     */
    public void setReceive(Thread receive) {
        Client.receive = receive;
    }

    /**
     * Entry point for the client
     * @param args program arguments
     */
    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: [port]");
            System.exit(1);
        }
        Scanner sc=new Scanner(System.in);
        int otherPort = Integer.parseInt(args[0]);
        System.out.println("Hello there! Please tell me who you are");
        String otherName = sc.nextLine();
        try {
            InetAddress otherGroup = InetAddress.getByName("228.5.6.7");
            MulticastSocket otherSocket = new MulticastSocket(Integer.parseInt(args[0]));
            Client client = new Client(otherName, otherSocket, otherGroup, otherPort);
            client.getSocket().joinGroup(group);
            client.setSend(new ClientSendThread(client));
            client.setReceive(new ClientReceiveThread(client));
            client.getSend().start();
            client.getReceive().start();
        } catch (Exception e) {
            System.out.println("Exception in client" + e);
        }
    }

    /**
     * Gets the thread used to send messages
     * @return the thread used to send messages
     */
    public Thread getSend() {
        return send;
    }

    /**
     * Gets the thread used to receive messages
     * @return the thread used to receive messages
     */
    public Thread getReceive() {
        return receive;
    }

    /**
     * Gets the name of the client
     * @return the client's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the socket used to write and listen for messages
     * @return the socket used to write and listen for messages
     */
    public MulticastSocket getSocket() {
        return socket;
    }

    /**
     * Gets the port in which the client is listening
     * @return the port in which the client is listening
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the Inet Address of the client
     * @return the Inet Address of the client
     */
    public InetAddress getGroup() {
        return group;
    }

    /**
     * Stops all running threads and exits the program
     */
    public void stop(){
        receive.stop();
        try {
            socket.leaveGroup(group);
        }catch (Exception e){
            System.out.println("Exception + " + e);
        }
        System.exit(0);
    }
}
