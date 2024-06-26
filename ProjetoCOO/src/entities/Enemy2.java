package entities;

import gamelib.GameLib;
import gamelib.States;

class Enemy2 {

    private int state = States.ACTIVE;
	private double X;
	private double Y = -10.0;
	private double V;
	private double angle = 3 * Math.PI / 2;
	private double RV = 0.0;
	private double explosion_start = 0;
	private double explosion_end = 0;
	

    Enemy2 (double spawnX){
        this.state = States.ACTIVE;
        this.X = spawnX;
        this.V = 0.42;
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

    double getExplosionEnd () {
		return this.explosion_end;
	}
	double getExplosionStart() {
		return this.explosion_start;
	}

	int getState(){
		return this.state;
	}

    void explode (){
		this.state = States.EXPLODING;
		this.explosion_start = System.currentTimeMillis();
		this.explosion_end = System.currentTimeMillis()+500;
	}

    void updatePosition (long delta, Projectiles ep) {

		double previousY = this.Y;
        this.X += this.V * Math.cos(this.angle) * delta;
		this.Y += this.V * Math.sin(this.angle)* delta *(-1);
        this.angle += this.RV * delta;

        double threshold = GameLib.HEIGHT * 0.30;

        if(previousY < threshold && this.Y >= threshold) {
							
            if(this.X < GameLib.WIDTH / 2) this.RV = 0.003;
            else this.RV = -0.003;
        }

        if(this.RV > 0 && Math.abs(this.angle - 3 * Math.PI) < 0.05){
			this.shoot(ep);				
            this.RV = 0.0;
            this.angle = 3 * Math.PI;
        }

        if(this.RV < 0 && Math.abs(this.angle) < 0.05){
            this.shoot(ep);
            this.RV = 0.0;
            this.angle = 0.0;
        }

        
	}

    boolean canShoot (){
        if(this.RV > 0 && Math.abs(this.angle - 3 * Math.PI) < 0.05){
            return true;
        }
        if(this.RV < 0 && Math.abs(this.angle) < 0.05){
							
            return true;
        } 
        return false;
    }

    void shoot (Projectiles ep){
        double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };

        for(int k = 0; k < angles.length; k++){
            Projectile p = new Projectile();

            double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
            double vx = Math.cos(a);
            double vy = Math.sin(a);
                
            p.X = this.X;
            p.Y = this.Y;
            p.VX = vx * 0.30;
            p.VY = vy * 0.30;
            ep.addProjectile(p);
        }
    }
}
