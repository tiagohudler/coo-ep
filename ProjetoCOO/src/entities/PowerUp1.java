package entities;

public class PowerUp1 extends PowerUp{
    void applyPowerUp (Player p){
        p.setPowerup(1);
        p.setExplosion_end(System.currentTimeMillis()+5000);
    }
}
