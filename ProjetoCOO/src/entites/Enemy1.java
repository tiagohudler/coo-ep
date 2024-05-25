package entites;

public class Enemy1 implements Enemy{
    protected int state = ACTIVE;
	protected double X = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
	protected double Y = -10.0;
	protected double V = 0.20 + Math.random() * 0.15;
	protected double angle = 3 * Math.PI / 2;
	protected double RV = 0.0;
	protected double explosion_start = 0;
	protected double explosion_end = 0;
	protected long nextShoot = System.currentTimeMillis() + 500;

	public double getX (){
		return this.X;
	}
	public void setX(long delta){
		this.X += this.V * Math.cos(this.angle) * delta;
	}

	double getY() {
		
	}

	public void updatePosition (long delta) {

		this.X += this.V * Math.cos(this.angle) * delta;
		this.Y += this.V * Math.sin(this.angle) * delta * (-1.0);
		this.angle += this.RV * delta;
		
	}

	public double getExplosionEnd () {
		return this.explosion_end;
	}
	public double getExplosionStart() {
		return this.explosion_start;
	}

	public int getState(){
		return this.state;
	}
	public void setState (int state){
		this.state = state;
	}

	public void explode (){
		this.state = EXPLODING;
		this.explosion_start = System.currentTimeMillis();
		this.explosion_end = System.currentTimeMillis()+500;
	}

	public boolean canShoot (Player p){
		return (System.currentTimeMillis() > this.nextShoot && this.Y < p.Y) ? true : false;
	}
	public void updateNextShot (){
		this.nextShoot = (long) (System.currentTimeMillis() + 200 + Math.random() * 500);
	}
}
