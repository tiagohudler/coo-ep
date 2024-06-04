package entities;

abstract class PowerUp {
    States states = new States();
    private double X = Math.random() * GameLib.WIDTH;
    private double Y = -5.0;
    private double V = 0.20 + Math.random() * 0.15;
    private int state = states.INACTIVE;

    void applyPowerUp (Player p){
        
    }

    void updatePosition (long delta){
        this.Y += this.V*delta;
    }
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getState () {
        return this.state;
    }
}
