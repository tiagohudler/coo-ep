import java.awt.Color;
import java.util.*;


public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	

	/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Encontra e devolve o primeiro índice do  */
	/* array referente a uma posição "inativa". */
	
	public static int findFreeIndex(int [] stateArray){
		
		int i;
		
		for(i = 0; i < stateArray.length; i++){
			
			if(stateArray[i] == INACTIVE) break;
		}
		
		return i;
	}
	
	/* Encontra e devolve o conjunto de índices (a quantidade */
	/* de índices é defnida através do parâmetro "amount") do */
	/* array, referentes a posições "inativas".               */ 

	public static int [] findFreeIndex(int [] stateArray, int amount){

		int i, k;
		int [] freeArray = { stateArray.length, stateArray.length, stateArray.length };
		
		for(i = 0, k = 0; i < stateArray.length && k < amount; i++){
				
			if(stateArray[i] == INACTIVE) { 
				
				freeArray[k] = i; 
				k++;
			}
		}
		
		return freeArray;
	}

	interface Enemy {
		int state = ACTIVE;
		double X = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
		double Y = -10.0;
		double V = 0.20 + Math.random() * 0.15;
		double angle = 3 * Math.PI / 2;
		double RV = 0.0;
		double explosion_start = 0;
		double explosion_end = 0;
		long nextShoot = System.currentTimeMillis() + 500;
	}

	static class Player {
		int player_state = ACTIVE;								// estado
		double player_X = GameLib.WIDTH / 2;					// coordenada x
		double player_Y = GameLib.HEIGHT * 0.90;				// coordenada y
		double player_VX = 0.25;								// velocidade no eixo x
		double player_VY = 0.25;								// velocidade no eixo y
		final double radius = 12.0;						// raio (tamanho aproximado do player)
		double player_explosion_start = 0;						// instante do início da explosão
		double player_explosion_end = 0;						// instante do final da explosão
		private long player_nextShot;									// instante a partir do qual pode haver um próximo tiro

		Player (long time) {
			this.player_nextShot = time;
		}

		void setNextShot (long time){
			this.player_nextShot = time;
		}
		
		void setX (double x){
			this.player_X = x;
		}

		void movePlayer (long delta) {
			if(GameLib.iskeyPressed(GameLib.KEY_UP)) this.player_Y -= delta * this.player_VY;
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) this.player_Y += delta * this.player_VY;
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) this.player_X -= delta * this.player_VX;
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) this.player_X += delta * this.player_VY;
		}

	}

	static class Enemy1 implements Enemy {
		int state = ACTIVE;
		double X = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
		double Y = -10.0;
		double V = 0.20 + Math.random() * 0.15;
		double angle = 3 * Math.PI / 2;
		double RV = 0.0;
		double explosion_start = 0;
		double explosion_end = 0;
		long nextShoot = System.currentTimeMillis() + 500;
	}

	static class Enemies1 {
		double radius = 9.0;
		List<Enemy1> enemies = new ArrayList<Enemy1>();
		long nextEnemy = System.currentTimeMillis() + 2000;

		int nEnemies (){
			return this.enemies.size();
		}
		void updateNextEnemy (){
			this.nextEnemy = System.currentTimeMillis() + 500;
		}
		void spawnEnemy() {
			if(System.currentTimeMillis() > this.nextEnemy && this.nEnemies() < 10){
				this.enemies.add(new Enemy1());
				updateNextEnemy();
			}
		}

		double getX(int i) {
			return enemies.get(i).X;
		}

		double getY(int i) {
			return enemies.get(i).Y;
		}

		double getAngle(int i) {
			return enemies.get(i).angle;
		}

		void updatePosition (int i, long delta) {

			enemies.get(i).X += enemies.get(i).V * Math.cos(enemies.get(i).angle) * delta;
			enemies.get(i).Y += enemies.get(i).V * Math.sin(enemies.get(i).angle) * delta * (-1.0);
			enemies.get(i).angle += enemies.get(i).RV * delta;
		}

		int getState(int i){
			return this.enemies.get(i).state;
		}

		double getExplosionEnd(int i){
			return this.enemies.get(i).explosion_end;
		}
		double getExplosionStart(int i){
			return this.enemies.get(i).explosion_start;
		}

		void setState (int i, int state){
			this.enemies.get(i).state = state;
		}

		void explode (int i){
			this.enemies.get(i).state = EXPLODING;
			this.enemies.get(i).explosion_start = System.currentTimeMillis();
			this.enemies.get(i).explosion_end = System.currentTimeMillis()+500;
		}

		boolean canShoot (int i) {
			return System.currentTimeMillis() > this.enemies.get(i).nextShoot ? true : false; 
		}
		void updateNextShot (int i){
			this.enemies.get(i).nextShoot = (long) (System.currentTimeMillis() + 200 + Math.random() * 500);
		}

		void remove (int i){
			this.enemies.remove(i);
		}
	}
	
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variáveis do player */
		
		Player p = new Player(currentTime);

		/* variáveis dos projéteis disparados pelo player */
		
		int [] projectile_states = new int[10];					// estados
		double [] projectile_X = new double[10];				// coordenadas x
		double [] projectile_Y = new double[10];				// coordenadas y
		double [] projectile_VX = new double[10];				// velocidades no eixo x
		double [] projectile_VY = new double[10];				// velocidades no eixo y

		/* variáveis dos inimigos tipo 1 */

		Enemies1 enemies1 = new Enemies1();
		
		/* variáveis dos inimigos tipo 2 */
		
		int [] enemy2_states = new int[10];						// estados
		double [] enemy2_X = new double[10];					// coordenadas x
		double [] enemy2_Y = new double[10];					// coordenadas y
		double [] enemy2_V = new double[10];					// velocidades
		double [] enemy2_angle = new double[10];				// ângulos (indicam direção do movimento)
		double [] enemy2_RV = new double[10];					// velocidades de rotação
		double [] enemy2_explosion_start = new double[10];		// instantes dos inícios das explosões
		double [] enemy2_explosion_end = new double[10];		// instantes dos finais das explosões
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do próximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formação de voo")
		double enemy2_radius = 12.0;							// raio (tamanho aproximado do inimigo 2)
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		
		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];			// velocidade no eixo x
		double [] e_projectile_VY = new double[200];			// velocidade no eixo y
		double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)
		
		/* estrelas que formam o fundo de primeiro plano */
		
		double [] background1_X = new double[20];
		double [] background1_Y = new double[20];
		double background1_speed = 0.070;
		double background1_count = 0.0;
		
		/* estrelas que formam o fundo de segundo plano */
		
		double [] background2_X = new double[50];
		double [] background2_Y = new double[50];
		double background2_speed = 0.045;
		double background2_count = 0.0;
		
		/* inicializações */
		
		for(int i = 0; i < projectile_states.length; i++) projectile_states[i] = INACTIVE;
		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
		
		for(int i = 0; i < background1_X.length; i++){
			
			background1_X[i] = Math.random() * GameLib.WIDTH;
			background1_Y[i] = Math.random() * GameLib.HEIGHT;
		}
		
		for(int i = 0; i < background2_X.length; i++){
			
			background2_X[i] = Math.random() * GameLib.WIDTH;
			background2_Y[i] = Math.random() * GameLib.HEIGHT;
		}
						
		/* iniciado interface gráfica */
		
		GameLib.initGraphics();
		
		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/*                                                                                               */
		/* O main loop do jogo possui executa as seguintes operações:                                    */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu desde a última atualização     */
		/*    e no timestamp atual: posição e orientação, execução de disparos de projéteis, etc.        */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/
		
		while(running){
		
			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projéteis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a última atualização.             */
			
			delta = System.currentTimeMillis() - currentTime;
			
			/* Já a variável "currentTime" nos dá o timestamp atual.  */
			
			currentTime = System.currentTimeMillis();
			
			/***************************/
			/* Verificação de colisões */
			/***************************/
						
			if(p.player_state == ACTIVE){
				
				/* colisões player - projeteis (inimigo) */
				
				for(int i = 0; i < e_projectile_states.length; i++){
					
					double dx = e_projectile_X[i] - p.player_X;
					double dy = e_projectile_Y[i] - p.player_Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (p.radius + e_projectile_radius) * 0.8){
						
						p.player_state = EXPLODING;
						p.player_explosion_start = currentTime;
						p.player_explosion_end = currentTime + 2000;
					}
				}
			
				/* colisões player - inimigos */
							
				for(int i = 0; i < enemies1.nEnemies(); i++){
					
					double dx = enemies1.getX(i) - p.player_X;
					double dy = enemies1.getY(i) - p.player_Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (p.radius + enemies1.radius) * 0.8){
						
						p.player_state = EXPLODING;
						p.player_explosion_start = currentTime;
						p.player_explosion_end = currentTime + 2000;
					}
				}
				
				for(int i = 0; i < enemy2_states.length; i++){
					
					double dx = enemy2_X[i] - p.player_X;
					double dy = enemy2_Y[i] - p.player_Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (p.radius + enemy2_radius) * 0.8){
						
						p.player_state = EXPLODING;
						p.player_explosion_start = currentTime;
						p.player_explosion_end = currentTime + 2000;
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(int k = 0; k < projectile_states.length; k++){
				
				for(int i = 0; i < enemies1.nEnemies(); i++){
										
					double dx = enemies1.getX(i) - projectile_X[k];
					double dy = enemies1.getY(i) - projectile_Y[k];
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < enemies1.radius){
						
						enemies1.explode(i);
						
					}
				}
				
				for(int i = 0; i < enemy2_states.length; i++){
					
					if(enemy2_states[i] == ACTIVE){
						
						double dx = enemy2_X[i] - projectile_X[k];
						double dy = enemy2_Y[i] - projectile_Y[k];
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy2_radius){
							
							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;
						}
					}
				}
			}
				
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			for(int i = 0; i < projectile_states.length; i++){
				
				if(projectile_states[i] == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(projectile_Y[i] < 0) {
						
						projectile_states[i] = INACTIVE;
					}
					else {
					
						projectile_X[i] += projectile_VX[i] * delta;
						projectile_Y[i] += projectile_VY[i] * delta;
					}
				}
			}
			
			/* projeteis (inimigos) */
			
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(e_projectile_Y[i] > GameLib.HEIGHT) {
						
						e_projectile_states[i] = INACTIVE;
					}
					else {
					
						e_projectile_X[i] += e_projectile_VX[i] * delta;
						e_projectile_Y[i] += e_projectile_VY[i] * delta;
					}
				}
			}
			
			// inimigos tipo 1  TODO: botar isso dentro de Enemies1 
			
			for(int i = 0; i < enemies1.nEnemies(); i++){
				
				if(enemies1.getState(i) == EXPLODING){
					
					if(currentTime > enemies1.getExplosionEnd(i)){
						
						enemies1.remove(i);
					}
				}
				
				if(enemies1.getState(i) == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemies1.getY(i) > GameLib.HEIGHT + 10) {
						
						enemies1.remove(i);
					}
					else {
					
						enemies1.updatePosition(i, delta);
						
						if(enemies1.canShoot(i) && enemies1.getY(i) < p.player_Y){
																							
							int free = findFreeIndex(e_projectile_states);
							
							if(free < e_projectile_states.length){
								
								e_projectile_X[free] = enemies1.getX(i);
								e_projectile_Y[free] = enemies1.getY(i);
								e_projectile_VX[free] = Math.cos(enemies1.getAngle(i)) * 0.45;
								e_projectile_VY[free] = Math.sin(enemies1.getAngle(i)) * 0.45 * (-1.0);
								e_projectile_states[free] = 1;
								
								enemies1.updateNextShot(i);
							}
						}
					}
				}
			}
			
			/* inimigos tipo 2 */
			
			for(int i = 0; i < enemy2_states.length; i++){
				
				if(enemy2_states[i] == EXPLODING){
					
					if(currentTime > enemy2_explosion_end[i]){
						
						enemy2_states[i] = INACTIVE;
					}
				}
				
				if(enemy2_states[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(	enemy2_X[i] < -10 || enemy2_X[i] > GameLib.WIDTH + 10 ) {
						
						enemy2_states[i] = INACTIVE;
					}
					else {
						
						boolean shootNow = false;
						double previousY = enemy2_Y[i];
												
						enemy2_X[i] += enemy2_V[i] * Math.cos(enemy2_angle[i]) * delta;
						enemy2_Y[i] += enemy2_V[i] * Math.sin(enemy2_angle[i]) * delta * (-1.0);
						enemy2_angle[i] += enemy2_RV[i] * delta;
						
						double threshold = GameLib.HEIGHT * 0.30;
						
						if(previousY < threshold && enemy2_Y[i] >= threshold) {
							
							if(enemy2_X[i] < GameLib.WIDTH / 2) enemy2_RV[i] = 0.003;
							else enemy2_RV[i] = -0.003;
						}
						
						if(enemy2_RV[i] > 0 && Math.abs(enemy2_angle[i] - 3 * Math.PI) < 0.05){
							
							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 3 * Math.PI;
							shootNow = true;
						}
						
						if(enemy2_RV[i] < 0 && Math.abs(enemy2_angle[i]) < 0.05){
							
							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 0.0;
							shootNow = true;
						}
																		
						if(shootNow){

							double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
							int [] freeArray = findFreeIndex(e_projectile_states, angles.length);

							for(int k = 0; k < freeArray.length; k++){
								
								int free = freeArray[k];
								
								if(free < e_projectile_states.length){
									
									double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
									double vx = Math.cos(a);
									double vy = Math.sin(a);
										
									e_projectile_X[free] = enemy2_X[i];
									e_projectile_Y[free] = enemy2_Y[i];
									e_projectile_VX[free] = vx * 0.30;
									e_projectile_VY[free] = vy * 0.30;
									e_projectile_states[free] = 1;
								}
							}
						}
					}
				}
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			//TODO: criar um método encapsulado para add inimigos
					
			enemies1.spawnEnemy();
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			if(currentTime > nextEnemy2){
				
				int free = findFreeIndex(enemy2_states);
								
				if(free < enemy2_states.length){
					
					enemy2_X[free] = enemy2_spawnX;
					enemy2_Y[free] = -10.0;
					enemy2_V[free] = 0.42;
					enemy2_angle[free] = (3 * Math.PI) / 2;
					enemy2_RV[free] = 0.0;
					enemy2_states[free] = ACTIVE;

					enemy2_count++;
					
					if(enemy2_count < 10){
						
						nextEnemy2 = currentTime + 120;
					}
					else {
						
						enemy2_count = 0;
						enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
						nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
					}
				}
			}
			
			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(p.player_state == EXPLODING){
				
				if(currentTime > p.player_explosion_end){
					
					p.player_state = ACTIVE;
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			if(p.player_state == ACTIVE){
				
				p.movePlayer(delta);
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(currentTime > p.player_nextShot){
						
						int free = findFreeIndex(projectile_states);
												
						if(free < projectile_states.length){
							
							projectile_X[free] = p.player_X;
							projectile_Y[free] = p.player_Y - 2 * p.radius;
							projectile_VX[free] = 0.0;
							projectile_VY[free] = -1.0;
							projectile_states[free] = 1;
							p.player_nextShot = currentTime + 100;
						}
					}
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			if(p.player_X < 0.0) p.player_X = 0.0;
			if(p.player_X >= GameLib.WIDTH) p.player_X = GameLib.WIDTH - 1;
			if(p.player_Y < 25.0) p.player_Y = 25.0;
			if(p.player_Y >= GameLib.HEIGHT) p.player_Y = GameLib.HEIGHT - 1;

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo distante */
			
			GameLib.setColor(Color.DARK_GRAY);
			background2_count += background2_speed * delta;
			
			for(int i = 0; i < background2_X.length; i++){
				
				GameLib.fillRect(background2_X[i], (background2_Y[i] + background2_count) % GameLib.HEIGHT, 2, 2);
			}
			
			/* desenhando plano de fundo próximo */
			
			GameLib.setColor(Color.GRAY);
			background1_count += background1_speed * delta;
			
			for(int i = 0; i < background1_X.length; i++){
				
				GameLib.fillRect(background1_X[i], (background1_Y[i] + background1_count) % GameLib.HEIGHT, 3, 3);
			}
						
			/* desenhando player */
			
			if(p.player_state == EXPLODING){
				
				double alpha = (currentTime - p.player_explosion_start) / (p.player_explosion_end - p.player_explosion_start);
				GameLib.drawExplosion(p.player_X, p.player_Y, alpha);
			}
			else{
				
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(p.player_X, p.player_Y, p.radius);
			}
				
			
			/* deenhando projeteis (player) */
			
			for(int i = 0; i < projectile_states.length; i++){
				
				if(projectile_states[i] == ACTIVE){
					
					GameLib.setColor(Color.GREEN);
					GameLib.drawLine(projectile_X[i], projectile_Y[i] - 5, projectile_X[i], projectile_Y[i] + 5);
					GameLib.drawLine(projectile_X[i] - 1, projectile_Y[i] - 3, projectile_X[i] - 1, projectile_Y[i] + 3);
					GameLib.drawLine(projectile_X[i] + 1, projectile_Y[i] - 3, projectile_X[i] + 1, projectile_Y[i] + 3);
				}
			}
			
			/* desenhando projeteis (inimigos) */
		
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
	
					GameLib.setColor(Color.RED);
					GameLib.drawCircle(e_projectile_X[i], e_projectile_Y[i], e_projectile_radius);
				}
			}
			
			/* desenhando inimigos (tipo 1) */
			//TODO: encaplusar os desenhos
			
			for(int i = 0; i < enemies1.nEnemies(); i++){
				
				if(enemies1.getState(i) == EXPLODING){
					
					double alpha = (currentTime - enemies1.getExplosionStart(i)) / (enemies1.getExplosionEnd(i) - enemies1.getExplosionStart(i));
					GameLib.drawExplosion(enemies1.getX(i), enemies1.getY(i), alpha);
				}
				
				if(enemies1.getState(i) == ACTIVE){
			
					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemies1.getX(i), enemies1.getY(i), enemies1.radius);
				}
			}
			
			/* desenhando inimigos (tipo 2) */
			
			for(int i = 0; i < enemy2_states.length; i++){
				
				if(enemy2_states[i] == EXPLODING){
					
					double alpha = (currentTime - enemy2_explosion_start[i]) / (enemy2_explosion_end[i] - enemy2_explosion_start[i]);
					GameLib.drawExplosion(enemy2_X[i], enemy2_Y[i], alpha);
				}
				
				if(enemy2_states[i] == ACTIVE){
			
					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy2_X[i], enemy2_Y[i], enemy2_radius);
				}
			}
			
			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */
			
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
