package entities;

import java.util.ArrayList;
import java.util.List;

public class Projectiles {
    private List <Projectile> projectiles = new ArrayList<Projectile>();

    void addProjectile (Projectile p){
        projectiles.add(p);
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

    public int nProjectiles (){
        return this.projectiles.size();
    }

    public double getX (int i){
        return projectiles.get(i).X;
    }
    public double getY (int i){
        return projectiles.get(i).Y;
    }
    public double getRadius (int i){
        return projectiles.get(i).radius;
    }
}
