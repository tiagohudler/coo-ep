package entities;

import gamelib.GameLib;
import gamelib.States;

public class Enemy3 {

    private int state = States.ACTIVE;
	private double X;
	private double Y = Math.random() * (GameLib.HEIGHT - 480) + 40.0;
	private double V = 0.20 + Math.random() * 0.15;
	private double angle;
	private double targetX = Math.random() * (GameLib.WIDTH - 40) + 20;
	private double explosion_start = 0;
	private double explosion_end = 0.0;
	private long nextShoot = 0;

    Enemy3(){
        if(Math.random() >= 0.5){
			this.X = -10;
			this.angle = 0;
		}
		else {
			this.X = 490;
			this.angle = Math.PI;
		}
    }

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
		long currentTime = System.currentTimeMillis();
		if(this.X > this.targetX + 3 || this.X < this.targetX - 3){
            this.X += this.V * Math.cos(this.angle) * delta;
            return;
        }

		if (explosion_end == 0){
			this.V = 0.0;
			this.explosion_end = currentTime+2000;
			return;
		}

        if (this.V == 0.0) {
            if(this.angle == Math.PI) {
				this.targetX = 600;
				this.angle = 0;
			}
            else {
				this.targetX = -100;
				this.angle = Math.PI;
			}
			this.V = 0.35;
			return;
        }
		
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
		this.state = States.EXPLODING;
		this.explosion_start = System.currentTimeMillis();
		this.explosion_end = this.explosion_start+500;
	}

	boolean canShoot (Player p){
		
		if (this.V == 0.0 && this.explosion_end > System.currentTimeMillis()) {
            
            return true;
        }
        return false;
	}
	

	void shoot (Projectiles ep){
		if (System.currentTimeMillis() > this.nextShoot){
			Projectile p1 = new Projectile(), p2 = new Projectile(), p3 = new Projectile();
			p1.X = this.X - 5;
			p2.X = this.X + 5;
			p3.X = this.X;
			p1.Y = p2.Y = p3.Y = this.Y;
			p1.VX = p2.VX = p3.VX = 0;
			p1.VY = p2.VY = p3.VY = 0.45;
			ep.addProjectile(p1);
			ep.addProjectile(p2);
			ep.addProjectile(p3);
			this.nextShoot = (long) (System.currentTimeMillis() + 50);
		}
	}
}
