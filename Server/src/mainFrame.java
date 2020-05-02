import com.mysql.jdbc.exceptions.MySQLDataException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;

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
    private JButton reFreshButton;
    protected static Server server;
    protected CustomDrawingPanel customDrawingPanel;
    private double xPhone = 2;
    private double yPhone = 2;
    public mainFrame() {

        JFrame Xyplane = new JFrame("x y plane");


        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double xroom = Double.parseDouble(roomXDimensionTextField.getText());
                double yroom = Double.parseDouble(roomYDimensionTextField.getText());
                double xstation1 = Double.parseDouble(xCoordinateStation1TextField.getText());
                double ystation1 = Double.parseDouble(yCoordinateStation1TextField.getText());
                double xstation2 = Double.parseDouble(xCoordinateStation2TextField.getText());
                double ystation2 = Double.parseDouble(yCoordinateStation2TextField.getText());
                double xstation3 = Double.parseDouble(xCoordinateStation3TextField.getText());
                double ystation3 = Double.parseDouble(yCoordinateStation3TextField.getText());

                customDrawingPanel =  new CustomDrawingPanel(xroom,yroom,xstation1,ystation1,xstation2,ystation2,xstation3,ystation3);
                Xyplane.setContentPane(customDrawingPanel);
                Xyplane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Xyplane.setResizable(false);
                Xyplane.pack();
                //Xyplane.setSize((int)xroom*100+10,(int)yroom*100+10);
                Xyplane.setVisible(true);
                Thread t = new ServerHandler(xroom,yroom,xstation1,ystation1,xstation2,ystation2,xstation3,ystation3);
                t.start();




            }
        });

        reFreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchDeviceLocation();
                customDrawingPanel.setXPhone(xPhone);
                customDrawingPanel.setYPhone(yPhone);
            }
        });
    }

    private void fetchDeviceLocation(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.100.53:3306/wifi", "wifiuser", "sa");
            Statement statement = connection.createStatement();
            String selectStatement = "SELECT * FROM `intries`";
            ResultSet rs = statement.executeQuery(selectStatement);
            //statement.executeUpdate(insertStatement);


            rs.last();
            xPhone = (double) rs.getInt("x");
            yPhone = (double) rs.getInt("y");
            connection.close();

        }
        catch (SQLException | ClassNotFoundException sqlException){
            System.out.println(sqlException.toString());
        }
    }
}



class ServerHandler extends Thread
{

    double xRoom;
    double yRoom;
    double xStation1;
    double yStation1;
    double xStation2;
    double yStation2;
    double xStation3;
    double yStation3;

    // Constructor
    public ServerHandler(double xroom,double yroom,double xstation1,double ystation1,double xstation2, double ystation2,double xstation3,double ystation3)
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


