import java.net.DatagramPacket;

/**
 * Thread used to receive messages
 * @author Ithan Velarde, Taha Mdarhri, Aichetou M'Bareck
 */
public class ClientReceiveThread extends Thread{

    /**
     * Client that is running this thread
     */
    private Client client;

    /**
     * Thread constructor
     * @param client client running this thread
     */
    public ClientReceiveThread(Client client){
        this.client = client;
    }

    @Override
    public void run(){
        try {
            while (true){
                byte[] buf = new byte[1000];
                DatagramPacket received = new DatagramPacket(buf, buf.length);
                client.getSocket().receive(received);
                String toPrint = new String(buf, 0, received.getLength(), "UTF-8");
                if(!toPrint.split(":|\s")[0].equals(client.getName())) {
                    System.out.println("\n" + toPrint);
                    System.out.println("message body: ");
                }
            }
        }catch (Exception e){
        }
    }
}
