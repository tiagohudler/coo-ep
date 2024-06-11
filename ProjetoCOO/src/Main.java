
import background.Background;
import entities.*;
import gamelib.GameLib;


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

		Projectiles p_projectiles = new Projectiles(p);
		
		/* variáveis dos inimigos tipo 1 */

		Enemies1 enemies1 = new Enemies1();
		
		/* variáveis dos inimigos tipo 2 */

		Enemies2 enemies2 = new Enemies2();
		
		/* variáveis dos inimigos tipo 2 */

		Enemies3 enemies3 = new Enemies3();
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		
		Projectiles e_projectiles = new Projectiles();

		//powerups

		PowerUps powerups = new PowerUps();

		/* estrelas que formam o fundo de primeiro plano */

		Background background1 = new Background(20, 0.070);
		
		/* estrelas que formam o fundo de segundo plano */
		
		Background background2 = new Background(50, 0.045);


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
				
				e_projectiles.verifyCollisions(p);
			
				/* colisões player - inimigos */
							
				enemies1.verifyCollisions(p);
				
				enemies2.verifyCollisions(p);

				enemies3.verifyCollisions(p);
			}
			
			/* colisões projeteis (player) - inimigos */
			
			p_projectiles.verifyCollisions(enemies1, enemies2, enemies3);

			// colisões player - powerups

			powerups.verifyCollisions(p);
				
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			p_projectiles.updateStates(delta);

			/* projeteis (inimigos) */
			
			
			e_projectiles.updateStates(delta);
			
			// inimigos tipo 1   
			
			enemies1.updatePositions(delta, e_projectiles, p);
			
			/* inimigos tipo 2 */
			
			enemies2.updatePositions(delta, e_projectiles, p);

			/* inimigos tipo 3 */
			
			enemies3.updatePositions(delta, e_projectiles, p);

			//powerups

			powerups.updatePositions(delta);
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
					
			enemies1.spawnEnemy();
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			enemies2.spawnEnemy();

			/* verificando se novos inimigos (tipo 3) devem ser "lançados" */
			
			enemies3.spawnEnemy();

			// verificando se novos powerups devem ser "lançados"

			powerups.spawnPowerUp();
			
			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */

			p.updateStates();
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
				
			p.updatePosition(delta);
			if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
				
				p.shoot(p_projectiles);

			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo distante */
			
			background2.drawFar(delta);
			
			/* desenhando plano de fundo próximo */
			
			background1.drawNear(delta);
						
			/* desenhando player */
			
			p.draw();
			
			p.drawLives();
			
			/* desenhando projeteis (player) */
			
			p_projectiles.draw();
			
			/* desenhando projeteis (inimigos) */
		
			e_projectiles.draw();
			
			/* desenhando inimigos (tipo 1) */
			
			enemies1.draw();
			
			/* desenhando inimigos (tipo 2) */
			
			enemies2.draw();

			/* desenhando inimigos (tipo 3) */
			
			enemies3.draw();

			// desenhando powerups

			powerups.draw();

			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */
			
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
