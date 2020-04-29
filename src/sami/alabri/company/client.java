package sami.alabri.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class client {

    private Socket socket = null;
    private DataOutputStream out = null;
    private String ipAdress;
    private int portNumber;
    private int identity;

    public client(String ipadress, int portnumber,int id){
        ipAdress =ipadress;
        portNumber = portnumber;
        identity = id;


    }

    public void connect(){
        try{
            socket = new Socket(ipAdress,portNumber);
            System.out.println("Connected to Server");
            out = new DataOutputStream(socket.getOutputStream());
        }

        catch (UnknownHostException exception){
            System.out.println(exception.toString());
        }
        catch (IOException exception){
            System.out.println(exception.toString());
        }
    }

    public void sendData(String data){
        connect();
            try {

                out.writeUTF(Integer.toString(identity)+"," + data);
                socket.close();
            }
            catch (IOException exception){
                System.out.println(exception.toString());
            }
    }

    public void disconnect(){
        try{
        out.close();
        socket.close();
        }
        catch (IOException exception){
            System.out.println(exception.toString());
        }

    }


}
