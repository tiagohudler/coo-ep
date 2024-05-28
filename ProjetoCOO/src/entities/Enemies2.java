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
}
