import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static String name;
    private static MulticastSocket socket;
    private static InetAddress group;
    private static int port;
    private static Thread send;
    private static Thread receive;

    public Client(String name, MulticastSocket socket, InetAddress group, int port){
        this.name = name;
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    public void setSend(Thread send) {
        Client.send = send;
    }

    public void setReceive(Thread receive) {
        Client.receive = receive;
    }

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

    public Thread getSend() {
        return send;
    }

    public Thread getReceive() {
        return receive;
    }

    public String getName() {
        return name;
    }

    public MulticastSocket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getGroup() {
        return group;
    }

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
