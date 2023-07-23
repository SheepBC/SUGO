package Sheep_BC.Supporters;

import java.awt.*;

public class Tool {

    public static Color getRandomColor(){
        int R = (int) (Math.random()*256);
        int G = (int) (Math.random()*256);
        int B = (int) (Math.random()*256);
        Color color = new Color(R,G,B);
        return color;
    }

}
