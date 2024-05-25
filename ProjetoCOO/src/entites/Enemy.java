package entites;

interface Enemy {
    int state = ACTIVE;
    double X = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
    double Y = -10.0;
    double V = 0.20 + Math.random() * 0.15;
    double angle = 3 * Math.PI / 2;
    double RV = 0.0;
    double explosion_start = 0;
    double explosion_end = 0;
    long nextShoot = System.currentTimeMillis() + 500;

    int getState();
    
    double getX();
    void setX(long delta);

    double getY ();

    void updatePosition (long delta);

    double getExplosionEnd();
    double getExplosionStart();

    void setState (int state);

    void explode ();

    boolean canShoot (Player p);
    void updateNextShot ();

}
