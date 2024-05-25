package entites;
import java.util.*;

public class Enemies <T extends Enemy> {
    //TODO: radius na classe do inimigo
    double radius = 9.0;
    List<T> enemies = new ArrayList<T>();
    long nextEnemy = System.currentTimeMillis() + 2000;

    public int nEnemies (){
        return this.enemies.size();
    }
    public void updateNextEnemy (){
        this.nextEnemy = System.currentTimeMillis() + 500;
    }
    public void spawnEnemy() {
        if(System.currentTimeMillis() > this.nextEnemy && this.nEnemies() < 10){
            this.enemies.add(new Enemy1());
            updateNextEnemy();
        }
    }

    public double getX(int i) {
        return enemies.get(i).getX();
    }

    public double getY(int i) {
        return enemies.get(i).getY();
    }

    public double getAngle(int i) {
        return enemies.get(i).angle;
    }

    public void updatePosition (int i, long delta) {
        //TODO: setX
        enemies.get(i).setX(delta);
        enemies.get(i).Y += enemies.get(i).V * Math.sin(enemies.get(i).angle) * delta * (-1.0);
        enemies.get(i).angle += enemies.get(i).RV * delta;
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
    public void updateNextShot (int i){
        this.enemies.get(i).updateNextShot();
    }

    public void remove (int i){
        this.enemies.remove(i);
    }

}
