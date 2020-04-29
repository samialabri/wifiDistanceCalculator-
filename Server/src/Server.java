import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private Socket socket   = null;
    private ServerSocket server   = null;
    private DataInputStream in       =  null;
    public static String dataFromStation1;
    public static String dataFromStation2;
    public static String dataFromStation3;



    public Server() {
        try {
            server = new ServerSocket(5000);
            System.out.println("Server started");
        } catch (IOException exception) {
            System.out.println("couldn't start the server");
        }

        while (true) {
            try {

                System.out.println("Waiting for a client ...");

                socket = server.accept();
                System.out.println("Client accepted");

                // takes input from the client socket
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                Thread t = new ClientHandler(socket, in);
                t.start();

            /*try{
                data = in.readUTF();
                System.out.println(data);
                socket.close();
            }
            catch (IOException exception){
                System.out.println(exception.toString());
            }*/
            } catch (Exception exception) {
                System.out.println(exception.toString());
            }
        }
    }
}


class ClientHandler extends Thread
{

    final DataInputStream dis;
    final Socket s;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis)
    {
        this.s = s;
        this.dis = dis;
    }

    @Override
    public void run()
    {
        try {
            String received = dis.readUTF();
            System.out.println(received);
            if(received.contains("1")){
                Server.dataFromStation1 = received.substring(received.indexOf(',')+1);
            }else if(received.contains("2")){
                Server.dataFromStation2 = received.substring(received.indexOf(',')+1);
            }else if(received.contains("3")){
                Server.dataFromStation3 = received.substring(received.indexOf(',')+1);
            }
            //System.out.println(received);

        } catch (IOException e) {
            e.printStackTrace();
        }


        try
        {
            // closing resources
            this.dis.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
