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
        
		Projectile p = new Projectile();
		p.X = this.X;
		p.Y = this.Y - 2 * this.radius;
		p.VX = 0.0;
		p.VY = (-1.0);
		ep.addProjectile(p);
		this.nextShoot = System.currentTimeMillis()+100;;

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
}


