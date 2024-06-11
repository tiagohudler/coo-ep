package background;

import java.awt.Color;

import gamelib.GameLib;

public class Background {
    double [] X;
    double [] Y;
    double speed;
    double count = 0.0;

    public Background (int size, double speed){
        X = new double[size];
        Y = new double[size];
        this.speed = speed; 

        for(int i = 0; i < size; i++){
			
			X[i] = Math.random() * GameLib.WIDTH;
			Y[i] = Math.random() * GameLib.HEIGHT;
		}
    }

    public void drawFar (long delta){
        GameLib.setColor(Color.DARK_GRAY);
        count += speed * delta;
        
        for(int i = 0; i < X.length; i++){
            
            GameLib.fillRect(X[i], (Y[i] + count) % GameLib.HEIGHT, 2, 2);
        }
    }

    public void drawNear (long delta){
        GameLib.setColor(Color.GRAY);
			count += speed * delta;
			
			for(int i = 0; i < X.length; i++){
				
				GameLib.fillRect(X[i], (Y[i] + count) % GameLib.HEIGHT, 3, 3);
			}
    }
}
