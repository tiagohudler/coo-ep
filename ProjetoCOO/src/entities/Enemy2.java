package entities;

class Enemy2 extends Enemy1  {
    States states = new States();

    private int state = states.ACTIVE;
	private double X;
	private double Y = -10.0;
	private double V;
	private double angle = 3 * Math.PI / 2;
	private double RV = 0.0;
	private double explosion_start = 0;
	private double explosion_end = 0;
	private long nextShoot = System.currentTimeMillis() + 500;

    Enemy2 (double spawnX){

        this.X = spawnX;
        this.V = 0.42;
    }
}
