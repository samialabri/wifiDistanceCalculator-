import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CustomDrawingPanel extends JPanel {

    private double xRoomDimension;
    private double yRoomDimension;

    private double xStation1;
    private double yStation1;
    private double xStation2;
    private double yStation2;
    private double xStation3;
    private double yStation3;

    private double xPhone = 2.0;
    private double yPhone = 2.0;

    private final int scale = 100;



    public CustomDrawingPanel(double xroom,double yroom,double x1,double y1,double x2, double y2, double x3,double y3){

        xRoomDimension = xroom*scale;
        yRoomDimension = yroom*scale;
        xPhone = xPhone*scale;
        yPhone = yPhone*scale;

        xStation1 = x1*scale;
        xStation2 = x2*scale;
        xStation3 = x3*scale;

        yStation1 = y1*scale;
        yStation2 = y2*scale;
        yStation3 = y3*scale;

        this.setPreferredSize(new Dimension((int)xRoomDimension,(int)yRoomDimension));
    }

    BufferedImage raspberryPiImage = null;
    BufferedImage phoneImage = null;

    public void setXPhone(double x){
        xPhone = x*scale;
        repaint();
    }

    public void setYPhone(double y){
        yPhone = y*scale;
        repaint();
    }

    Image sizedImage = null;
    Image phoneSizedImage = null;
    @Override
    protected void paintComponent(Graphics g){
        try {
            raspberryPiImage = ImageIO.read(new File("./Server/assets/RaspbberryPiIcon.png"));
            phoneImage = ImageIO.read(new File("./Server/assets/phone.png"));
            sizedImage = raspberryPiImage.getScaledInstance(50,100,Image.SCALE_DEFAULT);
            phoneSizedImage = phoneImage.getScaledInstance(50,100,Image.SCALE_DEFAULT);
        }catch (IOException exception){
            System.out.println("image not found");
        }
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawLine(10,10,10,(int)yRoomDimension-10);
        graphics2D.drawLine(10,(int)yRoomDimension-10,(int)xRoomDimension-10,(int)yRoomDimension-10);

        int stationHalfSize = 25;

        graphics2D.setFont(new Font("TimesRoman",Font.PLAIN,18));
        graphics2D.drawImage(sizedImage,(int)xStation1-stationHalfSize,(int)yStation1-stationHalfSize,null);
        graphics2D.drawString("S1 (" +Double.toString(xStation1/scale) +"," + Double.toString(yStation1/scale) +")",(int)xStation1-stationHalfSize,(int)yStation1-stationHalfSize);
        graphics2D.drawImage(sizedImage,(int)xStation2-stationHalfSize,(int)yStation2-stationHalfSize,null);
        graphics2D.drawString("S2 (" +Double.toString(xStation2/scale) +"," + Double.toString(yStation2/scale) +")",(int)xStation2-stationHalfSize,(int)yStation2-stationHalfSize);
        graphics2D.drawImage(sizedImage,(int)xStation3-stationHalfSize,(int)yStation3-stationHalfSize,null);
        graphics2D.drawString("S3 (" +Double.toString(xStation3/scale) +"," + Double.toString(yStation3/scale) +")",(int)xStation3-stationHalfSize,(int)yStation3-stationHalfSize);

        graphics2D.drawImage(phoneSizedImage,(int)xPhone-stationHalfSize,(int)yPhone-stationHalfSize,null);

        //graphics2D.drawRect(xStation1-stationHalfSize,yStation1-stationHalfSize,stationHalfSize*2,stationHalfSize*2);
    }
}
