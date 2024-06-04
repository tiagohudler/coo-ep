package entities;
import java.awt.Color;

public class Player{
    private States states = new States();
    private int state = states.ACTIVE;								// estado
    private int lives = 2;
    private int powerup = 0;
    private double X = GameLib.WIDTH / 2;					// coordenada x
    private double Y = GameLib.HEIGHT * 0.90;				// coordenada y
    private double VX = 0.25;								// velocidade no eixo x
    private double VY = 0.25;								// velocidade no eixo y
    final double radius = 12.0;						// raio (tamanho aproximado do player)
    private double explosion_start = 0;						// instante do início da explosão
    private double explosion_end = 0;						// instante do final da explosão
    private long nextShoot;									// instante a partir do qual pode haver um próximo tiro
    private boolean draw = true;

    public Player (long time) {
        this.nextShoot = time;
    }

    public void updateNextShot (){
        this.nextShoot = System.currentTimeMillis()+100;
    }
    
    public void setX (long x){
        this.X = x;
    }
    public double getX (){
        return this.X;
    }

    public double getY (){
        return this.Y;
    }
    public void setY (double y){
        this.X = y;
    }

    public double getExplosionEnd () {
		return this.explosion_end;
	}
    public void setExplosion_end(double explosion_end) {
        this.explosion_end = explosion_end;
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

    public double getRadius () {
        return this.radius;
    }

    public void explode (){
        if (this.lives > 0){
            this.lives--;
            this.explosion_start = System.currentTimeMillis()+300;
            this.explosion_end = System.currentTimeMillis()+1500;
            this.state = states.INACTIVE;
        }
        else{
            this.state = states.EXPLODING;
            this.lives = 2;
            this.explosion_start = System.currentTimeMillis();
            this.explosion_end = System.currentTimeMillis()+2000;
        }
	}

    public void updatePosition (long delta) {
        if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.Y -= delta * this.VY;
        if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.Y += delta * this.VY;
        if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.X -= delta * this.VX;
        if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.X += delta * this.VY;
    }

    public boolean canShoot (){
        return System.currentTimeMillis() > this.nextShoot ? true : false;
    }

    public int getPowerUp (){
        return this.powerup;
    }

    public void shoot (Projectiles ep){
        
		if(this.powerup != 3){
            Projectile p = new Projectile();
            if (this.powerup == 2) p.explosive = true;
            p.X = this.X;
            p.Y = this.Y - 2 * this.radius;
            p.VX = 0.0;
            p.VY = (-1.0);
            ep.addProjectile(p);
            this.nextShoot = System.currentTimeMillis()+100;
        }
        else {
            Projectile p1 = new Projectile();
            Projectile p2 = new Projectile();
            Projectile p3 = new Projectile();
            p1.bouncy = p2.bouncy = p3.bouncy = true;
            p1.X = p2.X = p3.X = this.X;
            p1.Y = p2.Y = p3.Y = this.Y;
            p1.VY = p2.VY = p3.VY = -1;
            p1.VX = -1;
            p2.VX = 0;
            p3.VX = 1;
            ep.addProjectile(p1);
            ep.addProjectile(p2);
            ep.addProjectile(p3);
            this.nextShoot = System.currentTimeMillis()+100;
        }

	}

    public boolean shouldDraw (){
        if(this.explosion_start < System.currentTimeMillis()){
            this.draw ^= true;
            this.explosion_start += 150;
        }
        return draw;
    }

    public void drawLives () {
        GameLib.setColor(Color.RED);
        int x = 440, y = 680;

        for (int i = 0; i <= this.lives; i++){
            GameLib.drawHeart(x, y);
            x -= 25;
        }
        
    }

    public void setPowerup(int powerup) {
        this.powerup = powerup;
    }

    public void draw(){
        if (this.state != states.ACTIVE){
            if(this.state == states.EXPLODING){
            
                double alpha = (System.currentTimeMillis() - this.explosion_start) / (this.explosion_end - this.explosion_start);
                GameLib.drawExplosion(this.X, this.Y, alpha);
            }
            if (this.state == states.INACTIVE && this.shouldDraw()){
                GameLib.setColor(Color.BLUE);
                GameLib.drawPlayer(this.X, this.Y, this.radius);
            }
        }
        if (this.state == states.ACTIVE) {
            
            GameLib.setColor(Color.BLUE);
            GameLib.drawPlayer(this.X, this.Y, this.radius);

            if(this.powerup == 1){
                GameLib.setColor(Color.ORANGE);
                GameLib.drawStar(X+1, Y, radius-5);
            }

            if(this.powerup == 2){
                GameLib.setColor(Color.PINK);
                GameLib.drawStar(X+1, Y, radius-5);
            }

            if(this.powerup == 3){
                GameLib.setColor(Color.BLUE);
                GameLib.drawStar(X+1, Y, radius-5);
            }
        }
    }
}


