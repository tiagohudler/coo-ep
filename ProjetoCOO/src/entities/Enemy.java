package entities;
abstract public class Enemy {
    int state;
    double X;
    double Y;
    double V;
    double angle;
    double RV;
    double explosion_start;
    double explosion_end;
    long nextShoot;

    abstract int getState();
    
    public abstract double getX();

    public abstract double getY ();

    public abstract double getAngle ();

    abstract void updatePosition (long delta);

    abstract double getExplosionEnd();
    abstract double getExplosionStart();

    abstract void setState (int state);

    abstract void explode ();

    abstract boolean canShoot (Player p);

    abstract void shoot (Projectiles ep);
}
