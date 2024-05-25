import java.awt.Color;

import entities.*;


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
		
		EnemyProjectiles e_projectiles = new EnemyProjectiles();

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
						
			if(p.getState() == ACTIVE){
				
				/* colisões player - projeteis (inimigo) */
				
				p.verifyCollisions(e_projectiles);
			
				/* colisões player - inimigos */
							
				for(int i = 0; i < enemies1.nEnemies(); i++){
					
					double dx = enemies1.getX(i) - p.getX();
					double dy = enemies1.getY(i) - p.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (p.getRadius() + enemies1.getRadius()) * 0.8){
						
						p.explode();
					}
				}
				
				for(int i = 0; i < enemy2_states.length; i++){
					
					double dx = enemy2_X[i] - p.getX();
					double dy = enemy2_Y[i] - p.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (p.getRadius() + enemy2_radius) * 0.8){
						
						p.explode();
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(int k = 0; k < projectile_states.length; k++){
				
				for(int i = 0; i < enemies1.nEnemies(); i++){
										
					double dx = enemies1.getX(i) - projectile_X[k];
					double dy = enemies1.getY(i) - projectile_Y[k];
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < enemies1.getRadius()){
						
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
			
			
			e_projectiles.updateStates(delta);
			
			// inimigos tipo 1  TODO: botar isso dentro de Enemies1 
			
			enemies1.updatePositions(delta, e_projectiles, p);
			
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
			if(p.getState() == EXPLODING){
				
				if(currentTime > p.getExplosionEnd()){
					
					p.setState(EXPLODING);	
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			if(p.getState() == ACTIVE){
				
				p.updatePosition(delta);
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(p.canShoot()){
						
						int free = findFreeIndex(projectile_states);
												
						if(free < projectile_states.length){
							
							projectile_X[free] = p.getX();
							projectile_Y[free] = p.getY() - 2 * p.getRadius();
							projectile_VX[free] = 0.0;
							projectile_VY[free] = -1.0;
							projectile_states[free] = 1;
							p.updateNextShot();
						}
					}
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			if(p.getX() < 0.0) p.setX(0);
			if(p.getX() >= GameLib.WIDTH) p.setX(GameLib.WIDTH - 1);
			if(p.getY() < 25.0) p.setY(25.0);
			if(p.getY() >= GameLib.HEIGHT) p.setY(GameLib.HEIGHT - 1);

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
			
			if(p.getState() == EXPLODING){
				
				double alpha = (currentTime - p.getExplosionStart()) / (p.getExplosionEnd() - p.getExplosionStart());
				GameLib.drawExplosion(p.getX(), p.getY(), alpha);
			}
			else{
				
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(p.getX(), p.getY(), p.getRadius());
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
		
			for(int i = 0; i < e_projectiles.nProjectiles(); i++){
				
				GameLib.setColor(Color.RED);
				GameLib.drawCircle(e_projectiles.getX(i), e_projectiles.getY(i), e_projectiles.getRadius(i));

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
					GameLib.drawCircle(enemies1.getX(i), enemies1.getY(i), enemies1.getRadius());
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
