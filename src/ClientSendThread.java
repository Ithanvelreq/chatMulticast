import java.net.DatagramPacket;
import java.util.Scanner;

public class ClientSendThread extends Thread{
    private Client client;
    public ClientSendThread(Client client){
        this.client = client;
    }
    @Override
    public void run(){
        Scanner sc=new Scanner(System.in);
        String toSend = client.getName() + " has joined the chat";
        try {
            while (true){
                DatagramPacket message = new DatagramPacket(toSend.getBytes(), toSend.length(),
                        client.getGroup(), client.getPort());
                client.getSocket().send(message);
                System.out.print("message body: ");
                String body = sc.nextLine();
                toSend = client.getName() + ": " + body;
                if(body.equals("exit")){
                    toSend = client.getName() + " has left the chat";
                    message = new DatagramPacket(toSend.getBytes(), toSend.length(),
                            client.getGroup(), client.getPort());
                    client.getSocket().send(message);
                    client.getSocket().send(message);
                    client.stop();
                }
            }
        }catch (Exception e){

        }
    }
}
