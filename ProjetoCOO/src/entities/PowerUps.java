package entities;

import java.util.ArrayList;
import java.util.List;

public class PowerUps implements CollidableArray {
    private double radius = 15.0;
    private long nextPU = System.currentTimeMillis() + 5000;
    private List <PowerUp> powerups = new ArrayList<PowerUp>();

    public int size (){
        return this.powerups.size();
    }

    public double getX (int i){
        return this.powerups.get(i).getX();
    }

    public double getY (int i){
        return this.powerups.get(i).getY();
    }

    public int getState (int i) {
        return this.powerups.get(i).getState();
    }

    public void updatePositions (long delta){
        for (int i = 0; i < this.powerups.size(); i++){
            this.powerups.get(i).updatePosition(delta);
            if (this.powerups.get(i).getY() > 730) this.remove(i);
        }
    }

    public void verifyCollisions (Player p){
        double dx, dy, dist;
        boolean hitou = false;

        for (PowerUp e : this.powerups){
            dx = e.getX() - p.getX();
            dy = e.getY() - p.getY();
            dist = Math.sqrt(dx * dx + dy * dy);
            if(dist < (p.getRadius() + this.radius) * 0.8){
                e.applyPowerUp(p);
                this.nextPU = System.currentTimeMillis()+6000;
                hitou = true;
                break;
            }
        }
        if (hitou) this.powerups.clear();
    }

    public double getRadius (){
        return this.radius;
    }

    public void remove (int i){
        this.powerups.remove(i);
    }

    public void spawnPowerUp (){
        if (System.currentTimeMillis() > this.nextPU && this.size() == 0) {
            //double randomizer = Math.random();
            if (true)  {
                this.powerups.add(new PowerUp1());
                this.nextPU += 5000;
            }
            // if (randomizer >= 0.3 && randomizer < 0.6)  {
            //     this.powerups.add(new PowerUp2());
            //     this.nextPU += 5000;
            // }
            // if (randomizer >= 0.6)  {
            //     this.powerups.add(new PowerUp2());
            //     this.nextPU += 5000;
            // }
        }
    }

    public void explode (int i){}

    public void verifyCollisions (CollidableArray arr){}

    
}
