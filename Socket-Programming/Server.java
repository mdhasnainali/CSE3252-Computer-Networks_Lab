import java.net.*;
import java.io.*;

public class Server{
    public static void main(String[] args){
        try{
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();
            while(true){
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String message = (String)dis.readUTF();
                System.out.println("Client: "+message);
                if(message.equals("exit")){
                    ss.close();
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}