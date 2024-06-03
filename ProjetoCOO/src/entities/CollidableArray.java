package entities;

interface CollidableArray {
    public int size ();
    public int getState(int i);
    public double getX(int i);
    public double getY(int i);
    public void remove (int i);
    public double getRadius ();
    public void verifyCollisions(Player p);
    public void verifyCollisions(CollidableArray p);
    public void explode (int i);
}
