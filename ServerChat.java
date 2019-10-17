import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerChat {
    private ArrayList<ClientChat> clients = new ArrayList<ClientChat>();
    ServerSocket serversocket;
    Socket client;
    int clientCount = 0;
    ExecutorService pool = Executors.newFixedThreadPool(10);

    public void start() throws IOException{
        System.out.println("Connection Starting on port: 44444");
        //make connection to client on port specified
        serversocket = new ServerSocket(44444);

        //accept connection from client
        while (true){
            client = serversocket.accept();
            clientCount++;
            ServerThread runnable = new ServerThread();
            pool.execute(runnable);
        }
    }
    
    public class ServerThread implements Runnable{
        DataInputStream in;
        DataOutputStream out;
        public void run(){
            try{
                //open buffered reader for reading data from client
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());

                String username = in.readUTF(); //read1
                System.out.println("SERVER SIDE: " + username);
                String accessCode = "";
                while (!accessCode.equalsIgnoreCase("4444")){
                    accessCode = in.readUTF(); //read2
                    System.out.println("SERVER SIDE: " + accessCode);

                    if(accessCode.equals("4444")){
                        out.writeUTF("You are connected");
                        clients.add(new ClientChat(username, accessCode));
                    }else {
                        out.writeUTF("Incorrect access code");
                    }
                }
                while (true){
                    String message = in.readUTF(); //read3
                    System.out.println(username + ": " + message);
                    out.writeUTF(username);
                    out.writeUTF(message);
                }

            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ServerChat server = new ServerChat();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
