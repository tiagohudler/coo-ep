package entities;

import java.util.ArrayList;

class Projectile {
    double X;				// coordenadas x
    double Y;				// coordenadas y
    double VX;			// velocidade no eixo x
    double VY;			// velocidade no eixo y
    boolean explosive = false;
	boolean bouncy = false;

    public double getX (){
		return this.X;
	}
	

	public double getY() {
		return this.Y;
	}

  void explode (ArrayList <Projectile> ep){
	Projectile p1 = new Projectile();
	p1.X = this.X; p1.Y = this.Y;
	p1.VX = 0;
	p1.VY = 1;

	Projectile p2 = new Projectile();
	p2.X = this.X; p2.Y = this.Y;
	p2.VX = -1;
	p2.VY = 1;

	Projectile p3 = new Projectile();
	p3.X = this.X; p3.Y = this.Y;
	p3.VX = -1;
	p3.VY = 0;

	Projectile p4 = new Projectile();
	p4.X = this.X; p4.Y = this.Y;
	p4.VX = -1;
	p4.VY = -1;

	Projectile p5 = new Projectile();
	p5.X = this.X; p5.Y = this.Y;
	p5.VX = 0;
	p5.VY = -1;

	Projectile p6 = new Projectile();
	p6.X = this.X; p6.Y = this.Y;
	p6.VX = 1;
	p6.VY = -1;

	Projectile p7 = new Projectile();
	p7.X = this.X; p7.Y = this.Y;
	p7.VX = 1;
	p7.VY = 0;

	Projectile p8 = new Projectile();
	p8.X = this.X; p8.Y = this.Y;
	p8.VX = 1;
	p8.VY = 0;

	ep.add(p1);
	ep.add(p2);
	ep.add(p3);
	ep.add(p4);
	ep.add(p5);
	ep.add(p6);
	ep.add(p7);
	ep.add(p8);
  }
}
