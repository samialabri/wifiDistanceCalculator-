import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Jama.Matrix;
//mysql imports

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Server {
    //the distance coming from each station
    public static String dataFromStation1 ="1";
    public static String dataFromStation2 = "1";
    public static String dataFromStation3 = "1";
    //the calculated coordinates of the target device
    public static double DeviceXCoordinate;
    public static double DeviceYCoordinate;

    public double roomXDimension;
    public double roomYDimension;

    public static double station1XCoordinate;
    public static double station1YCoordinate;

    public static double station2XCoordinate;
    public static double station2YCoordinate;

    public static double station3XCoordinate;
    public static double station3YCoordinate;

    private static final Scanner sc = new Scanner(System.in);


    public Server(double xroom,double yroom,double xstation1,double ystation1,double xstation2, double ystation2,double xstation3,double ystation3) {
        roomXDimension = xroom;
        roomYDimension = yroom;
        station1XCoordinate = xstation1;
        station1YCoordinate = ystation1;
        station2XCoordinate = xstation2;
        station2YCoordinate = ystation2;
        station3XCoordinate = xstation3;
        station3YCoordinate = ystation3;


        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection= DriverManager.getConnection("jdbc:mysql://192.168.100.59:3306/wifi","wifiuser","sa");
            Statement statement = connection.createStatement();
            String insertStatement = "INSERT INTO `stations` (`id`, `x1`, `y1`, `x2`, `y2`, `x3`, `y3`) VALUES (NULL,"+Double.toString(station1XCoordinate)+","+ Double.toString(station1YCoordinate)+","+Double.toString(station2XCoordinate)+"," + Double.toString(station2YCoordinate) +","+Double.toString(station3XCoordinate)+","+Double.toString(station3YCoordinate)+")";
            statement.executeUpdate(insertStatement);
            connection.close();

        }catch (SQLException | ClassNotFoundException sqlException){
            System.out.println(sqlException.toString());
        }
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
    }}



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
                Connection connection= DriverManager.getConnection("jdbc:mysql://192.168.100.59:3306/wifi","wifiuser","sa");
                Statement statement = connection.createStatement();
                String insertStatement = "INSERT INTO `entries` (`time_stamp`, `mac`, `x`, `y`) VALUES (current_timestamp(), '00:00:00:00:00:00', "+Double.toString(Server.DeviceXCoordinate)+", " + Double.toString(Server.DeviceYCoordinate) +")";
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
        double cor[]=getLoc(Server.station1XCoordinate,Server.station1YCoordinate,Double.parseDouble(Server.dataFromStation1),Server.station2XCoordinate,Server.station2YCoordinate,Double.parseDouble(Server.dataFromStation2),Server.station3XCoordinate,Server.station3YCoordinate,Double.parseDouble(Server.dataFromStation3));
        Server.DeviceXCoordinate = cor[0];
        Server.DeviceYCoordinate = cor[1];
    }

    private double [] getLoc(double x1,double y1,double d1,double x2,double y2,double d2,double x3,double y3,double d3){
        double hv[][]= { {x2-x1 , y2-y1},  {x3-x1, y3-y1}   } ;
        double bv[][]={ { 0.5*( ((d1*d1)-(d2*d2)) + ((x2*x2)+(y2*y2)) - ((x1*x1)+(y1*y1)))}
                ,{ 0.5*( ((d1*d1)-(d3*d3)) + ((x3*x3)+(y3*y3)) - ((x1*x1)+(y1*y1)))} };
        Matrix h = new Matrix(hv);
        Matrix b = new Matrix(bv);
        Matrix ht = h.transpose();
        Matrix hth = ht.times(h);
        Matrix hthInv=hth.inverse();
        Matrix hthInvHt = hthInv.times(ht);
        Matrix res = hthInvHt.times(b);
        res.print(res.getRowDimension(),res.getColumnDimension());
        double out[]=new double [2];
        out[0]= res.get(0,0);
        out[1]= res.get(1,0);



        return out;}

}
