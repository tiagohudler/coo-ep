package entities;

public class Player{
    private States states = new States();
    private int state = states.ACTIVE;								// estado
    private double X = GameLib.WIDTH / 2;					// coordenada x
    private double Y = GameLib.HEIGHT * 0.90;				// coordenada y
    private double VX = 0.25;								// velocidade no eixo x
    private double VY = 0.25;								// velocidade no eixo y
    final double radius = 12.0;						// raio (tamanho aproximado do player)
    private double explosion_start = 0;						// instante do início da explosão
    private double explosion_end = 0;						// instante do final da explosão
    private long nextShot;									// instante a partir do qual pode haver um próximo tiro

    public Player (long time) {
        this.nextShot = time;
    }

    public void updateNextShot (){
        this.nextShot = System.currentTimeMillis()+100;
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
		this.state = states.EXPLODING;
		this.explosion_start = System.currentTimeMillis();
		this.explosion_end = System.currentTimeMillis()+2000;
	}

    public void updatePosition (long delta) {
        if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.Y -= delta * this.VY;
        if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.Y += delta * this.VY;
        if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.X -= delta * this.VX;
        if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.X += delta * this.VY;
    }

    public boolean canShoot (){
        return System.currentTimeMillis() > this.nextShot ? true : false;
    }

    public void verifyCollisions (EnemyProjectiles ep){
        for(int i = 0; i < ep.nProjectiles(); i++){
					
            double dx = ep.getX(i) - this.X;
            double dy = ep.getY(i) - this.Y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if(dist < (this.radius + ep.getRadius(i)) * 0.8){
                
                this.explode();
            }
        }
    }

}
