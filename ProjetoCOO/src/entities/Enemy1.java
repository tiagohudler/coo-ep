package entities;

public class Enemy1 {
	States states = new States();

    private int state = states.ACTIVE;
	private double X = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
	private double Y = -10.0;
	private double V = 0.20 + Math.random() * 0.15;
	private double angle = 3 * Math.PI / 2;
	private double RV = 0.0;
	private double explosion_start = 0;
	private double explosion_end = 0;
	private long nextShoot = System.currentTimeMillis() + 500;

	public double getX (){
		return this.X;
	}
	

	public double getY() {
		return this.Y;
	}
	

	public double getAngle() {
		return angle;
	}

	void updatePosition (long delta) {

		this.X += this.V * Math.cos(this.angle) * delta;
		this.Y += this.V * Math.sin(this.angle) * delta * (-1.0);
		this.angle += this.RV * delta;
		
	}

	double getExplosionEnd () {
		return this.explosion_end;
	}
	double getExplosionStart() {
		return this.explosion_start;
	}

	int getState(){
		return this.state;
	}
	void setState (int state){
		this.state = state;
	}

	void explode (){
		this.state = states.EXPLODING;
		this.explosion_start = System.currentTimeMillis();
		this.explosion_end = System.currentTimeMillis()+500;
	}

	boolean canShoot (Player p){
		return (System.currentTimeMillis() > this.nextShoot && this.Y < p.getY()) ? true : false;
	}
	

	void shoot (Projectiles ep){
		Projectile p = new Projectile();
		p.X = this.X;
		p.Y = this.Y;
		p.VX = Math.cos(this.angle) * 0.45;
		p.VY = Math.sin(this.angle) * 0.45 * (-1.0);
		ep.addProjectile(p);
		this.nextShoot = (long) (System.currentTimeMillis() + 200 + Math.random() * 500);
	}
}
