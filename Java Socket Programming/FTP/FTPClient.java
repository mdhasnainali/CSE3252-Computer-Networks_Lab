import java.io.*;
import java.net.Socket;

public class FTPClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost", 6666);

            System.out.println("Successfully Connected with the FTP Server...");

            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            DataInputStream dis = new DataInputStream(s.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            final String clientDir = "./FTP_Client_Directory";

            while (true){
                System.out.print("> ");

                String command = reader.readLine();
                dos.writeUTF(command);

                if (command.equals("exit")){
                    System.out.println("Disconnected");
                    s.close();
                    break;
                } else if (command.equals("ls")) {
                    printFileList(dis);
                } else if (command.length() >= 5 && command.substring(0,4).equals("get ")) {
                    int condition = dis.readInt();
                    if (condition == 2){
                        String fileName = command.substring(4).trim();
                        receiveFile(dis,fileName, clientDir);
                    } else if (condition == 1) {
                        System.out.println("Can't Receive: This is a directory");
                    } else {
                        System.out.println("File Not Found");
                    }
                } else if (command.length() >= 5 && command.substring(0,4).equals("put ")) {
                    String fileName = command.substring(4).trim();
                    File file = new File(clientDir + "/" + fileName);
                    if(file.isFile() && file.exists()){
                        dos.writeInt(2);
                        sendFile(dos, clientDir, fileName);
                    } else if (file.isDirectory()) {
                        dos.writeInt(1);
                        System.out.println("Can't Send File: " + fileName + " is a directory");
                    }  else {
                        dos.writeInt(0);
                        System.out.println("File Not Found");
                    }
                }
                else {
                    System.out.println("Command Not Found");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printFileList(DataInputStream dis) throws IOException {
        String fileList = dis.readUTF();
        System.out.println(fileList);
    }

    public static void sendFile(DataOutputStream dos, String directory, String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(directory + "/" + fileName);

        byte buffer[] = new byte[20971520];  //  Max file size 20 MB

        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, bytesRead);
        }
        fis.close();

        System.out.println("Successfully File Sent");
    }

    public static void receiveFile(DataInputStream dis, String fileName, String directory) throws IOException {
        FileOutputStream fos = new FileOutputStream(directory + "/" +fileName);

        byte[] buffer = new byte[20971520];  //  Max file size 20 MB
        int bytesRead;
        bytesRead = dis.read(buffer);
        fos.write(buffer, 0, bytesRead);
        fos.close();

        System.out.println("Successfully File Received");
    }
}
