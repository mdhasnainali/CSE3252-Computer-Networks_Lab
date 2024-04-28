import java.net.*;
import java.io.*;
import java.io.InputStreamReader;

public class Client{
    public static void main(String[] args){
        try{
            Socket s = new Socket("localhost", 6666);
            while(true){
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

                String message = reader.readLine();
                dout.writeUTF(message);
                
                if(message.equals("exit")){
                    dout.close();
                    s.close();
                    break;
                }
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
}