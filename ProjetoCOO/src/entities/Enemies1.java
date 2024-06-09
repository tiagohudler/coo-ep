package entities;
import java.util.*;
import java.awt.Color;

public class Enemies1 implements CollidableArray {
    private final double radius = 9.0;
    private List<Enemy1> enemies = new ArrayList<Enemy1>();
    private long nextEnemy = System.currentTimeMillis() + 2000;

    public int size (){
        return this.enemies.size();
    }
    
    
    public void spawnEnemy() {
        if(System.currentTimeMillis() > this.nextEnemy && this.enemies.size() < 10){
            this.enemies.add(new Enemy1());
            this.nextEnemy = System.currentTimeMillis() + 650;
        }
    }

    public double getX(int i) {
        return enemies.get(i).getX();
    }

    public double getY(int i) {
        return enemies.get(i).getY();
    }

    public void updatePositions (long delta, Projectiles ep, Player p) {
        for (int i = 0; i < this.enemies.size(); i++){
            if(this.enemies.get(i).getState() == States.EXPLODING){
					
                if(System.currentTimeMillis() > this.enemies.get(i).getExplosionEnd()){
                    
                    this.remove(i);
                    continue;
                }
            }
            else {
            
                this.enemies.get(i).updatePosition(delta);
                
                if(this.enemies.get(i).canShoot(p)){
                        
                    this.enemies.get(i).shoot(ep);
                    
                }
            }

            /* verificando se inimigo saiu da tela */
            if(this.enemies.get(i).getY() > GameLib.HEIGHT + 10) {
                
                this.enemies.remove(i);
            } 
            
        }
    }

    public int getState(int i){
        return this.enemies.get(i).getState();
    }

    public double getExplosionEnd(int i){
        return this.enemies.get(i).getExplosionEnd();
    }
    public double getExplosionStart(int i){
        return this.enemies.get(i).getExplosionStart();
    }

    public void setState (int i, int state){
        this.enemies.get(i).setState(state);;
    }

    public void explode (int i){
        this.enemies.get(i).explode();
    }

    public boolean canShoot (int i, Player p) {
        return this.enemies.get(i).canShoot(p);
    }
    

    public void remove (int i){
        this.enemies.remove(i);
    }

    public double getRadius (){
        return this.radius;
    }

    public void verifyCollisions(Player p) {
        if (p.getPowerUp() == 1) return;
        double dx, dy, dist;
        for (Enemy1 e : this.enemies){
            dx = e.getX() - p.getX();
            dy = e.getY() - p.getY();
            dist = Math.sqrt(dx * dx + dy * dy);
            if(dist < (p.getRadius() + this.radius) * 0.8){
    
                p.explode();
            }
        }
    }

    public void verifyCollisions(CollidableArray obj) {
        if (obj instanceof Projectiles){
            obj.verifyCollisions(this);
            return;
        }
        double dx, dy, dist;
        for (Enemy1 e : this.enemies){
            for (int i = 0;  i < obj.size(); i++){
                dx = e.getX() - obj.getX(i);
                dy = e.getY() - obj.getY(i);
                dist = Math.sqrt(dx * dx + dy * dy);
                if(dist < this.radius){
							
                    e.explode();
                    
                }
            }    
        }
    }

    public void draw (){
        for(Enemy1 e : this.enemies){
				
            if(e.getState() == States.EXPLODING){
                
                double alpha = (System.currentTimeMillis() - e.getExplosionStart()) / (e.getExplosionEnd() - e.getExplosionStart());
                GameLib.drawExplosion(e.getX(), e.getY(), alpha);
            }
            
            if(e.getState() == States.ACTIVE){
        
                GameLib.setColor(Color.CYAN);
                GameLib.drawCircle(e.getX(), e.getY(), this.radius);
            }
        }
    }
}


