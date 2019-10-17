import java.net.*;
import java.io.*;
import java.util.*;

public class ClientChat {
    static Scanner scanner = new Scanner(System.in);
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private String username;
    private String accessCode;

    public ClientChat(){
    }
    
    public ClientChat(String username, String accessCode){
        this.username = username;
        this.accessCode = accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void startClient() throws UnknownHostException, IOException{
        //Create socket connection
        socket = new Socket("localhost", 44444);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        //Create printwriter for sending login to server

        //prompt for user name
        System.out.print("Enter name: ");
        setUsername(scanner.nextLine());

        //send user name to server
        out.writeUTF(getUsername()); //write1

        //Create Buffered reader for reading response from server
        String response = "false";
        while (!response.equalsIgnoreCase("You are connected")){
            //prompt for password
            System.out.print("Enter access code: ");
            setAccessCode(scanner.nextLine());

            //Sends password to server
            out.writeUTF(getAccessCode()); //Write2
            //Read response from server
            response = in.readUTF();   //Read1
            System.out.println("Server: " + response);
        }

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        String con ="go";
        while (con.equals("go")){
            System.out.print("Message: ");
            String message = scanner.nextLine();
            dos.writeUTF(message);   //write3
            String user = in.readUTF();
            String msg = in.readUTF();
            if(!getUsername().equals(user)){
                System.out.println(user + ": " + msg);
            }
            //Not currently working?
            if(message.equals("exit")){
                con = "exit";
            }
        }
    }

    public static void main(String args[]){
        ClientChat client = new ClientChat();
        try {
            client.startClient();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
