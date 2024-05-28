package entities;

import java.util.ArrayList;
import java.util.List;

public class Enemies2 extends Enemies1 {
    private States states = new States();
    private final double radius = 9.0;
    private long nextEnemy = System.currentTimeMillis() + 2000;
    double spawnX = GameLib.WIDTH * 0.20;
    private List<Enemy2> enemies = new ArrayList<Enemy2>();

    public void spawnEnemy() {
        if (System.currentTimeMillis() > this.nextEnemy){
            if (this.nEnemies() < 10){
                enemies.add(new Enemy2(this.spawnX));
                this.nextEnemy = System.currentTimeMillis()+120;
            }
            else{
                this.spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
                this.nextEnemy = (long) (System. currentTimeMillis() + 3000 + Math.random() * 3000);
            }
        }
    }

    public void updatePositions (long delta, EnemyProjectiles ep, Player p) {
        for (int i = 0; i < this.enemies.size(); i++){
            if(this.enemies.get(i).getState() == states.EXPLODING){
					
                if(System.currentTimeMillis() > this.enemies.get(i).getExplosionEnd()){
                    
                    this.remove(i);
                    continue;
                }
            }

            /* verificando se inimigo saiu da tela */
            if(this.enemies.get(i).getX() < -10 || this.enemies.get(i).getX() > GameLib.HEIGHT + 10) {
                this.enemies.remove(i);
            } 
            else {
            
                this.enemies.get(i).updatePosition(delta);
                
                if(this.enemies.get(i).canShoot()){
                        
                    this.enemies.get(i).shoot(ep);
                    
                }
            }
        }
    }
}
