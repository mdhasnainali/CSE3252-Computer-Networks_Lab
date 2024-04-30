import java.net.*;
import java.io.*;
import java.time.LocalTime;  
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;  

public class Server{
    public static void main(String[] args){
        try{
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();
            while(true){
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String message_from_client = (String)dis.readUTF();
                System.out.println("Client: "+message_from_client);
                
                if(message_from_client.equals("hi")){
                    System.out.println("Server: Hello");
                }
                else if(message_from_client.equals("date")){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
                    LocalDateTime now = LocalDateTime.now();  
                    System.out.println(dtf.format(now));
                }
                else if(message_from_client.equals("time")){
                    System.out.println("Server: " + LocalTime.now());
                }
                else if(message_from_client.equals("exit")){
                    dis.close();
                    s.close();
                    ss.close();
                    break;
                }
                else{
                    System.out.println("Invalid Message from Client");
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}