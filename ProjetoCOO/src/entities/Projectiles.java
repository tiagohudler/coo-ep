package entities;

import java.util.ArrayList;
import java.util.List;

import gamelib.GameLib;

import java.awt.Color;

public class Projectiles implements CollidableArray {
    private List <Projectile> projectiles = new ArrayList<Projectile>();
    private double radius = 2.0;
    private boolean player_projectiles = false;

    public Projectiles (Player p){
        this.player_projectiles = true;
    }

    public Projectiles (){
    }

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
                continue;              
            }

            else {
            
                this.projectiles.get(i).X += this.projectiles.get(i).VX * delta;
                this.projectiles.get(i).Y += this.projectiles.get(i).VY * delta;
            }

            if(this.projectiles.get(i).bouncy){
                if(this.projectiles.get(i).X <= 0 || this.projectiles.get(i).X >= GameLib.WIDTH) this.projectiles.get(i).VX *= -1; 
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
        if (p.getPowerUp() != 1) {
            for (Projectile e : this.projectiles){
                dx = e.getX() - p.getX();
                dy = e.getY() - p.getY();
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < (p.getRadius() + this.radius) * 0.8){
        
                    p.explode();
                }
            }
        }
        
    }

    public void verifyCollisions(CollidableArray obj) {
        ArrayList <Projectile> aux = new ArrayList<Projectile>();
        double dx, dy, dist;
        for (Projectile e : this.projectiles){
            for (int i = 0;  i < obj.size(); i++){
                dx = e.getX() - obj.getX(i);
                dy = e.getY() - obj.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj.getRadius() && obj.getState(i) == 1){
							
                    obj.explode(i);
                    if (e.explosive){
                        e.explode(aux);
                    }
                    
                }
            }    
        }
        this.projectiles.addAll(aux);
    }

    public void verifyCollisions(CollidableArray obj1, CollidableArray obj2, CollidableArray obj3) {
        ArrayList <Projectile> aux = new ArrayList<Projectile>();
        double dx, dy, dist;
        for (Projectile e : this.projectiles){
            for (int i = 0;  i < obj1.size(); i++){
                dx = e.getX() - obj1.getX(i);
                dy = e.getY() - obj1.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj1.getRadius() && obj1.getState(i) == 1){
							
                    obj1.explode(i);
                    if (e.explosive){
                        e.explode(aux);
                    }
                    
                }
            }
            for (int i = 0;  i < obj2.size(); i++){
                dx = e.getX() - obj2.getX(i);
                dy = e.getY() - obj2.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj2.getRadius() && obj2.getState(i) == 1){
							
                    obj2.explode(i);
                    if (e.explosive){
                        e.explode(aux);
                    }
                }
            }
            for (int i = 0;  i < obj3.size(); i++){
                dx = e.getX() - obj3.getX(i);
                dy = e.getY() - obj3.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < obj3.getRadius() && obj3.getState(i) == 1){
							
                    obj3.explode(i);
                    if (e.explosive){
                        e.explode(aux);
                    }
                }
            }     
        }
        this.projectiles.addAll(aux);
    }

    public void explode (int i) {

    }

    public int getState (int i) {
        if (i < this.size()) return 1;
        else return 0;
    }

    public void draw (){
        /* desenhando projeteis (player) */
        if(this.player_projectiles){
            for(Projectile e : this.projectiles){
                
                GameLib.setColor(Color.GREEN);
                GameLib.drawLine(e.X, e.Y - 5, e.X, e.Y + 5);
                GameLib.drawLine(e.X - 1, e.Y - 3, e.X - 1, e.Y + 3);
                GameLib.drawLine(e.X + 1, e.Y - 3, e.X + 1, e.Y + 3);
            
            }
        }
        /* desenhando projeteis (inimigos) */
        else{
            for(Projectile e : this.projectiles){
                
                GameLib.setColor(Color.RED);
                GameLib.drawCircle(e.X, e.Y, this.radius);

            }
        }
    }
}
