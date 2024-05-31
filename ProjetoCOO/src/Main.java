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

		Projectiles p_projectiles = new Projectiles();
		
		/* variáveis dos inimigos tipo 1 */

		Enemies1 enemies1 = new Enemies1();
		
		/* variáveis dos inimigos tipo 2 */

		Enemies2 enemies2 = new Enemies2();
		
		
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		
		Projectiles e_projectiles = new Projectiles();

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
				
				for(int i = 0; i < enemies2.nEnemies(); i++){
					
					double dx = enemies2.getX(i) - p.getX();
					double dy = enemies2.getY(i) - p.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (p.getRadius() + enemies2.getRadius()) * 0.8){
						
						p.explode();
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(int k = 0; k < p_projectiles.nProjectiles(); k++){
				for(int i = 0; i < enemies1.nEnemies(); i++){
										
					double dx = enemies1.getX(i) - p_projectiles.getX(k);
					double dy = enemies1.getY(i) - p_projectiles.getY(k);
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < enemies1.getRadius()){
						
						enemies1.explode(i);
						
					}
				}
				
				for(int i = 0; i < enemies2.nEnemies(); i++){
					
						
					double dx = enemies2.getX(i) - p_projectiles.getX(k);
					double dy = enemies2.getY(i) - p_projectiles.getY(k);
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < enemies2.getRadius()){
						
						enemies2.explode(i);
						
					}
				}
			}
				
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			p_projectiles.updateStates(delta);

			/* projeteis (inimigos) */
			
			
			e_projectiles.updateStates(delta);
			
			// inimigos tipo 1  TODO: botar isso dentro de Enemies1 
			
			enemies1.updatePositions(delta, e_projectiles, p);
			
			/* inimigos tipo 2 */
			
			enemies2.updatePositions(delta, e_projectiles, p);
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
					
			enemies1.spawnEnemy();
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			enemies2.spawnEnemy();
			
			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(p.getState() == EXPLODING){
				
				if(currentTime > p.getExplosionEnd()){
					
					p.setState(ACTIVE);	
				}
			}
			if(p.getState() == INACTIVE){
				
				if(currentTime > p.getExplosionEnd()){
					
					p.setState(ACTIVE);	
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			if(p.getState() != EXPLODING){
				
				p.updatePosition(delta);
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(p.canShoot() && p.getState() == ACTIVE){
						
						p.shoot(p_projectiles);

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
			
			if (p.getState() != ACTIVE){
				if(p.getState() == EXPLODING){
				
					double alpha = (currentTime - p.getExplosionStart()) / (p.getExplosionEnd() - p.getExplosionStart());
					GameLib.drawExplosion(p.getX(), p.getY(), alpha);
				}
				if (p.getState() == INACTIVE && p.shouldDraw()){
					GameLib.setColor(Color.BLUE);
					GameLib.drawPlayer(p.getX(), p.getY(), p.getRadius());
				}
			}
			if (p.getState() == ACTIVE) {
				
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(p.getX(), p.getY(), p.getRadius());
			}
				
			
			/* deenhando projeteis (player) */
			
			for(int i = 0; i < p_projectiles.nProjectiles(); i++){
				
					
				GameLib.setColor(Color.GREEN);
				GameLib.drawLine(p_projectiles.getX(i), p_projectiles.getY(i) - 5, p_projectiles.getX(i), p_projectiles.getY(i) + 5);
				GameLib.drawLine(p_projectiles.getX(i) - 1, p_projectiles.getY(i) - 3, p_projectiles.getX(i) - 1, p_projectiles.getY(i) + 3);
				GameLib.drawLine(p_projectiles.getX(i) + 1, p_projectiles.getY(i) - 3, p_projectiles.getX(i) + 1, p_projectiles.getY(i) + 3);
			
			}
			
			/* desenhando projeteis (inimigos) */
		
			for(int i = 0; i < e_projectiles.nProjectiles(); i++){
				
				GameLib.setColor(Color.RED);
				GameLib.drawCircle(e_projectiles.getX(i), e_projectiles.getY(i), e_projectiles.getRadius(i));

			}
			
			/* desenhando inimigos (tipo 1) */
			
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
			
			for(int i = 0; i < enemies2.nEnemies(); i++){
				
				if(enemies2.getState(i) == EXPLODING){
					
					double alpha = (currentTime - enemies2.getExplosionStart(i)) / (enemies2.getExplosionEnd(i) - enemies2.getExplosionStart(i));
					GameLib.drawExplosion(enemies2.getX(i), enemies2.getY(i), alpha);
				}
				
				if(enemies2.getState(i) == ACTIVE){
			
					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemies2.getX(i), enemies2.getY(i), enemies2.getRadius());
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
