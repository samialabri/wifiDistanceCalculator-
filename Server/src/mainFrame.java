import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class mainFrame {
    public JPanel panel1;
    private JButton startServerButton;
    private JTextField xCoordinateStation1TextField;
    private JLabel XcoordinateStation1Label;
    private JTextField yCoordinateStation1TextField;
    private JTextField xCoordinateStation2TextField;
    private JTextField yCoordinateStation2TextField;
    private JTextField xCoordinateStation3TextField;
    private JTextField yCoordinateStation3TextField;
    private JLabel XcoordinateStation2Label;
    private JLabel XcoordinateStation3Label;
    private JLabel YcoordinateStation1Label;
    private JLabel YcoordinateStation2Label;
    private JLabel YcoordinateStation3Label;
    private JTextField roomXDimensionTextField;
    private JLabel roomXDimensionLabel;
    private JTextField roomYDimensionTextField;
    private JLabel roomYDimensionLabel;
    protected static Server server;
    public mainFrame() {


        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int xroom = Integer.parseInt(roomXDimensionTextField.getText());
                int yroom = Integer.parseInt(roomYDimensionTextField.getText());
                int xstation1 = Integer.parseInt(xCoordinateStation1TextField.getText());
                int ystation1 = Integer.parseInt(yCoordinateStation1TextField.getText());
                int xstation2 = Integer.parseInt(xCoordinateStation2TextField.getText());
                int ystation2 = Integer.parseInt(yCoordinateStation2TextField.getText());
                int xstation3 = Integer.parseInt(xCoordinateStation3TextField.getText());
                int ystation3 = Integer.parseInt(yCoordinateStation3TextField.getText());

                Thread t = new ServerHandler(xroom,yroom,xstation1,ystation1,xstation2,ystation2,xstation3,ystation3);
                t.start();
            }
        });

    }
}



class ServerHandler extends Thread
{

    int xRoom;
    int yRoom;
    int xStation1;
    int yStation1;
    int xStation2;
    int yStation2;
    int xStation3;
    int yStation3;

    // Constructor
    public ServerHandler(int xroom,int yroom,int xstation1,int ystation1,int xstation2, int ystation2,int xstation3,int ystation3)
    {
        xRoom = xroom;
        yRoom = yroom;
        xStation1 = xstation1;
        yStation1 = ystation1;
        xStation2 = xstation2;
        yStation2 = ystation2;
        xStation3 = xstation3;
        yStation3 = ystation3;
    }

    @Override
    public void run()
    {

        mainFrame.server = new Server(xRoom,yRoom,xStation1,yStation1,xStation2,yStation2,xStation3,yStation3);
    }


}

