import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//mysql imports

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Server {
    //the distance coming from each station
    public static String dataFromStation1;
    public static String dataFromStation2;
    public static String dataFromStation3;
    //the calculated coordinates of the target device
    public static double DeviceXCoordinate;
    public static double DeviceYCoordinate;

    private static final Scanner sc = new Scanner(System.in);


    public Server() {
        //taking input from the user about the dimensions of the room
        System.out.println("insert the x dimension of the room ");
        int roomXDimension = sc.nextInt();
        System.out.println("insert the y dimension of the room ");
        int roomYDimension = sc.nextInt();
        //taking input from the user about the location of each station in the roam
        System.out.println("insert the x coordinate of the first station: ");
        int device1XCoordinate = sc.nextInt();
        System.out.println("insert the y coordinate of the first station: ");
        int device1YCoordinate = sc.nextInt();

        System.out.println("insert the x coordinate of the second station: ");
        int device2XCoordinate = sc.nextInt();
        System.out.println("insert the y coordinate of the second station: ");
        int device2YCoordinate = sc.nextInt();

        System.out.println("insert the x coordinate of the third station: ");
        int device3XCoordinate = sc.nextInt();
        System.out.println("insert the y coordinate of the third station: ");
        int device3YCoordinate = sc.nextInt();

        //opening and configuring a tcp socket to listen for the stations
        ServerSocket server = null;
        try {
            server = new ServerSocket(5000);
            System.out.println("Server started");
        } catch (IOException exception) {
            System.out.println("couldn't start the server");
        }
        //waiting for a connection from each station, and starting a thread to listen to multiple stations at the same
        //time, (multi-threaded server)
        while (true) {
            try {

                System.out.println("Waiting for a client ...");

                Socket socket = server.accept();
                System.out.println("Client accepted");

                // takes input from the client socket
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                Thread t = new ClientHandler(socket, in);
                t.start();

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

            findCoordinates();
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection= DriverManager.getConnection("jdbc:mysql://192.168.100.53:3306/wifi","wifiuser","sa");
                Statement statement = connection.createStatement();
                String insertStatement = "INSERT INTO `intries` (`time_stamp`, `mac`, `x`, `y`) VALUES (current_timestamp(), '00:00:00:00:00', "+Double.toString(Server.DeviceXCoordinate)+", " + Double.toString(Server.DeviceYCoordinate) +")";
                statement.executeUpdate(insertStatement);
                connection.close();

            }catch (SQLException | ClassNotFoundException sqlException){
                System.out.println(sqlException.toString());
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

    private void findCoordinates(){
        Server.DeviceXCoordinate = 1;
        Server.DeviceYCoordinate = 2;
    }
}
