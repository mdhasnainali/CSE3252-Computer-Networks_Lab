import java.net.*;
import java.io.*;
import java.io.InputStreamReader;

public class Client{
    public static void main(String[] args){
        try{
            Socket s = new Socket("localhost", 6666);
            while(true){
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String message_to_server = reader.readLine();
                dout.writeUTF(message_to_server);
                
                if(message_to_server.equals("exit")){
                    dout.close();
                    s.close();
                    break;
                }

                DataInputStream dis = new DataInputStream(s.getInputStream());
                String message_from_server = (String)dis.readUTF();
                System.out.println("Server: "+message_from_server);
                if(message_from_server.equals("exit")){
                    dis.close();
                    s.close();
                    break;
                }
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
}