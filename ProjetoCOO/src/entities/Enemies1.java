package entities;
import java.util.*;

public class Enemies1 {
    private States states = new States();
    private final double radius = 9.0;
    private List<Enemy1> enemies = new ArrayList<Enemy1>();
    private long nextEnemy = System.currentTimeMillis() + 2000;

    public int nEnemies (){
        return this.enemies.size();
    }
    
    public void spawnEnemy() {
        if(System.currentTimeMillis() > this.nextEnemy && this.enemies.size() < 10){
            this.enemies.add(new Enemy1());
            this.nextEnemy = System.currentTimeMillis() + 500;
        }
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

    public void updatePositions (long delta, EnemyProjectiles ep, Player p) {
        //TODO: declarar delta aq dentro
        for (int i = 0; i < this.enemies.size(); i++){
            if(this.enemies.get(i).getState() == states.EXPLODING){
					
                if(System.currentTimeMillis() > this.enemies.get(i).getExplosionEnd()){
                    
                    this.remove(i);
                    continue;
                }
            }

            /* verificando se inimigo saiu da tela */
            if(this.enemies.get(i).getY() > GameLib.HEIGHT + 10) {
                
                this.enemies.remove(i);
            } 
            else {
            
                this.enemies.get(i).updatePosition(delta);
                
                if(this.enemies.get(i).canShoot(p)){
                        
                    this.enemies.get(i).shoot(ep);
                    
                }
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
}
