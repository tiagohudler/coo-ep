package entities;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class Enemies2 implements CollidableArray {
    private States states = new States();
    private final double radius = 12.0;
    private int spawned = 0;
    private long nextEnemy = System.currentTimeMillis() + 2000;
    private double spawnX = GameLib.WIDTH * 0.20;
    private List<Enemy2> enemies = new ArrayList<Enemy2>();

    public int size (){
        return this.enemies.size();
    }

    public double getX(int i) {
        return enemies.get(i).getX();
    }

    public double getY(int i) {
        return enemies.get(i).getY();
    }

    public double getAngle(int i) {
        return enemies.get(i).getAngle();
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

    public void explode (int i){
        this.enemies.get(i).explode();
    }

    public void remove (int i){
        this.enemies.remove(i);
    }

    public double getRadius (){
        return this.radius;
    }

    public void spawnEnemy() {
        if (System.currentTimeMillis() > this.nextEnemy){
            if (spawned < 10){
                enemies.add(new Enemy2(this.spawnX));
                this.nextEnemy = System.currentTimeMillis()+120;
                spawned++;
            }
            else{
                this.spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
                this.nextEnemy = (long) (System. currentTimeMillis() + 3000 + Math.random() * 3000);
                this.spawned = 0;
            }
        }
    }

    public void updatePositions (long delta, Projectiles ep, Player p) {
        for (int i = 0; i < this.enemies.size(); i++){
            if(this.enemies.get(i).getState() == states.EXPLODING){
					
                if(System.currentTimeMillis() > this.enemies.get(i).getExplosionEnd()){
                    
                    this.remove(i);
                    continue;
                }
            }
            else {
                
                this.enemies.get(i).updatePosition(delta, ep);
                
                
            }

            /* verificando se inimigo saiu da tela */
            if(this.enemies.get(i).getX() < -10 || this.enemies.get(i).getX() > GameLib.HEIGHT + 10) {
                this.enemies.remove(i);
            } 
            
        }
    }

    public void verifyCollisions(Player p) {
        if (p.getPowerUp() == 1) return;
        double dx, dy, dist;
        for (Enemy2 e : this.enemies){
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
        for (Enemy2 e : this.enemies){
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
        for(Enemy2 e : this.enemies){
				
            if(e.getState() == states.EXPLODING){
                
                double alpha = (System.currentTimeMillis() - e.getExplosionStart()) / (e.getExplosionEnd() - e.getExplosionStart());
                GameLib.drawExplosion(e.getX(), e.getY(), alpha);
            }
            
            if(e.getState() == states.ACTIVE){
        
                GameLib.setColor(Color.MAGENTA);
                GameLib.drawDiamond(e.getX(), e.getY(), this.radius);
            }
        }
    }
}
