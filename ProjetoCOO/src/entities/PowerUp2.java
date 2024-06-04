package entities;

public class PowerUp2 extends PowerUp{
    void applyPowerUp (Player p){
        p.setPowerup(2);
        p.setExplosion_end(System.currentTimeMillis()+3500);
    }
}
