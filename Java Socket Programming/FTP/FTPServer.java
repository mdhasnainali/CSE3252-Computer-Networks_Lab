import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class FTPServer {
    public static void main(String[] args){
        try {
            ServerSocket ss = new ServerSocket(6666);
            Socket s = ss.accept();

            System.out.println("Successfully Connected with Client...");

            DataInputStream dis  = new DataInputStream(s.getInputStream());
            DataOutputStream dos  = new DataOutputStream(s.getOutputStream());

            final String servertDir = "./FTP_Server_Directory";

            while (true){
                String commandFromClient = dis.readUTF();
                System.out.print("Client Command: " );

                if (commandFromClient.equals("")){
                    System.out.println("<Blank>");
                }
                else{
                    System.out.println(commandFromClient);
                }

                if (commandFromClient.equals("exit")){
                    System.out.println("Disconnected");
                    s.close();
                    ss.close();
                    break;
                } else if (commandFromClient.equals("ls")) {
                    getFileList(dos,servertDir);
                } else if (commandFromClient.length() >= 5 && commandFromClient.substring(0,4).equals("get ")) {
                    String fileName = commandFromClient.substring(4).trim();
                    File file = new File(servertDir + "/" + fileName);
                    if(file.isFile() && file.exists()){
                        dos.writeInt(2);
                        sendFile(dos, servertDir, fileName);
                    } else if (file.isDirectory()) {
                        dos.writeInt(1);
                        System.out.println("Can't Send File: " + fileName + " is a directory");
                    }  else {
                        dos.writeInt(0);
                        System.out.println("File Not Found");
                    }
                } else if (commandFromClient.length() >= 5 && commandFromClient.substring(0,4).equals("put ")) {
                    int condition = dis.readInt();
                    if (condition == 2){
                        String fileName = commandFromClient.substring(4).trim();
                        receiveFile(dis, servertDir, fileName);
                    } else if (condition == 1) {
                        System.out.println("Can't Receive: This is a directory");
                    } else {
                        System.out.println("File Not Found");
                    }
                }
                else {
                    System.out.println("Command Not Found");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getFileList(DataOutputStream dos, String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        String fileList = "";

        if (files != null) {
            for (File file : files) {
                if(file.isFile()){
                    fileList = fileList.concat(file.getName() + "\n");
                }
                else if (file.isDirectory()){
                    fileList = fileList.concat(file.getName() + "(dir)\n");
                }
            }
        }
        dos.writeUTF(fileList);
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

    public static void receiveFile(DataInputStream dis, String directory, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(directory + "/" +fileName);

        byte[] buffer = new byte[20971520];  //  Max file size 20 MB
        int bytesRead;
        bytesRead = dis.read(buffer);
        fos.write(buffer, 0, bytesRead);
        fos.close();

        System.out.println("Successfully File Received");
    }
}