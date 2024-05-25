package entites;

public class Player {
    //TODO: privatizar
    int state = ACTIVE;								// estado
    double X = GameLib.WIDTH / 2;					// coordenada x
    double Y = GameLib.HEIGHT * 0.90;				// coordenada y
    double VX = 0.25;								// velocidade no eixo x
    double VY = 0.25;								// velocidade no eixo y
    final double radius = 12.0;						// raio (tamanho aproximado do player)
    double explosion_start = 0;						// instante do início da explosão
    double explosion_end = 0;						// instante do final da explosão
    private long nextShot;									// instante a partir do qual pode haver um próximo tiro

    public Player (long time) {
        this.nextShot = time;
    }

    public void setNextShot (long time){
        this.nextShot = time;
    }
    
    public void setX (double x){
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
		this.state = EXPLODING;
		this.explosion_start = System.currentTimeMillis();
		this.explosion_end = System.currentTimeMillis()+2000;
	}

    public void movePlayer (long delta) {
        if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.Y -= delta * this.VY;
        if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.Y += delta * this.VY;
        if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.X -= delta * this.VX;
        if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.X += delta * this.VY;
    }

    public boolean canShoot (){
        return System.currentTimeMillis() > this.nextShot ? true : false;
    }

}
