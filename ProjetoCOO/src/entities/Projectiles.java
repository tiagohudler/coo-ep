package entities;

import java.util.ArrayList;
import java.util.List;

public class Projectiles implements CollidableArray {
    private List <Projectile> projectiles = new ArrayList<Projectile>();
    private double radius = 2.0;

    void addProjectile (Projectile p){
        projectiles.add(p);
    }

    public int size (){
        return this.projectiles.size();
    }

    public void updateStates (long delta){
        for (int i  = 0; i < this.projectiles.size(); i++){
            if(this.projectiles.get(i).Y > GameLib.HEIGHT) {
                this.projectiles.remove(i);                
            }
            else {
            
                this.projectiles.get(i).X += this.projectiles.get(i).VX * delta;
                this.projectiles.get(i).Y += this.projectiles.get(i).VY * delta;
            }
        }
    }

    public double getX (int i){
        return projectiles.get(i).X;
    }
    public double getY (int i){
        return projectiles.get(i).Y;
    }
    public double getRadius (){
        return this.radius;
    }
    public void remove (int i){
        this.projectiles.remove(i);
    }

    public void verifyCollisions(Player p) {
        double dx, dy, dist;
        for (Projectile e : this.projectiles){
            dx = e.getX() - p.getX();
            dy = e.getY() - p.getY();
            dist = Math.sqrt(dx * dx + dy * dy);
            if(dist < (p.getRadius() + this.radius) * 0.8){
    
                p.explode();
            }
        }
    }

    public void verifyCollisions(CollidableArray obj) {
        double dx, dy, dist;
        for (Projectile e : this.projectiles){
            for (int i = 0;  i < obj.size(); i++){
                dx = e.getX() - obj.getX(i);
                dy = e.getY() - obj.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj.getRadius() && obj.getState(i) == 1){
							
                    obj.explode(i);
                    
                }
            }    
        }
    }

    public void verifyCollisions(CollidableArray obj1, CollidableArray obj2, CollidableArray obj3) {
        double dx, dy, dist;
        for (Projectile e : this.projectiles){
            for (int i = 0;  i < obj1.size(); i++){
                dx = e.getX() - obj1.getX(i);
                dy = e.getY() - obj1.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj1.getRadius() && obj1.getState(i) == 1){
							
                    obj1.explode(i);
                    
                }
            }
            for (int i = 0;  i < obj2.size(); i++){
                dx = e.getX() - obj2.getX(i);
                dy = e.getY() - obj2.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj2.getRadius() && obj2.getState(i) == 1){
							
                    obj2.explode(i);
                    
                }
            }
            for (int i = 0;  i < obj3.size(); i++){
                dx = e.getX() - obj3.getX(i);
                dy = e.getY() - obj3.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj3.getRadius() && obj3.getState(i) == 1){
							
                    obj3.explode(i);
                    
                }
            }     
        }
    }

    public void explode (int i) {

    }

    public int getState (int i) {
        if (i < this.size()) return 1;
        else return 0;
    }
}
