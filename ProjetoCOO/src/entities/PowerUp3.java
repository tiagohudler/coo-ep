package entities;

public class PowerUp3 extends PowerUp{
    void applyPowerUp (Player p){
        p.setPowerup(3);
        p.setExplosion_end(System.currentTimeMillis()+5000);
    }
}
