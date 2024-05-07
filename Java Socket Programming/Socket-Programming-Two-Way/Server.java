import java.net.*;
import java.io.*;
import java.io.InputStreamReader;

public class Server{
    public static void main(String[] args){
        try{
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();
            while(true){
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String message_from_client = (String)dis.readUTF();
                System.out.println("Client: "+message_from_client);
                if(message_from_client.equals("exit")){
                    dis.close();
                    s.close();
                    ss.close();
                    break;
                }
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String message_to_client = reader.readLine();
                dout.writeUTF(message_to_client);
                if(message_to_client.equals("exit")){
                    dout.close();
                    s.close();
                    ss.close();
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}