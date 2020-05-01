import javax.swing.*;
import java.awt.*;

public class CustomDrawingPanel extends JPanel {

    private int xRoomDimension;
    private int yRoomDimension;

    private int xStation1;
    private int yStation1;
    private int xStation2;
    private int yStation2;
    private int xStation3;
    private int yStation3;

    public CustomDrawingPanel(int xroom,int yroom,int xstation1,int ystation1,int xstation2, int ystation2,int xstation3,int ystation3){
        xRoomDimension = xroom;
        yRoomDimension = yroom;
        xStation1 = xstation1;
        yStation1 = ystation1;
        xStation2 = xstation2;
        yStation2 = ystation2;
        xStation3 = xstation3;
        yStation3 = ystation3;

        this.setPreferredSize(new Dimension(1000,1000));
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D)g;
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.drawLine(10,10,10,990);
        graphics2D.drawLine(10,990,990,990);

        int stationHalfSize = 25;

        graphics2D.drawRect(xStation1-stationHalfSize,yStation1-stationHalfSize,stationHalfSize*2,stationHalfSize*2);
    }
}
