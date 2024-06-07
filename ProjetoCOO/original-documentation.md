# Documentação e Críticas

## O player

As características do player eram ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação de uma classe que representa o player, com todos os atributos e métodos necessários para sua manipulação. 

A classe, além dos atributos básicos de qualquer entidade como coordenadas e velocidade, possui vidas e um identificador de power-up. Este identificador é modificado pelas classes de power-ups ao haver colisão do player com algum power-up durante o jogo, e dependendo do seu valor muda algumas características do comportamento do player (veja seção "Os power-ups").

### O sistema de vidas

Foi implementado um sistema de 3 vidas para o player, que só explode ao final das 3. Ao ser atingido e ainda possuir vidas, o player fica invulnerável por 1,5 segundos, e seu indicador pisca para indicar a invulnerabilidade e que o player recebeu dano. Durante esse período, o player está livre para se movimentar, mas é impossibilitado de atirar ou coletar power-ups.
Essa funcionalidade está implementada pelos métodos explode() e shouldDraw() do player em conjunto. São usados os atributos explosionEnd e explosionStart para manter o tempo de invulnerabilidade do player e controlar o desenho respectivamente, economizando assim valiosos 16 bytes de memória.

As vidas são representadas por corações desenhados no canto inferior direito da tela. Ao explodir, o player recebe as 3 vidas de volta.

## Collidable Arrays

CollidableArray é a interface que junta todos os administradores de entidades. A interface reúne todos os métodos que os administradores devem ter, como:
- verifyCollisions(CollidableArray ca ou Player p): verifica colisões com outro CollidableArray ou com o player;
- size(): retorna quantas entidades o array está armazenando;
- remove(int i): remove o elemento i do array;
- draw(): desenha todos os elementos guardados.


## Os inimigos

Os inimigos eram administrados por meio de diversos arrays, onde cada índice representava um inimigo. Isso abre espaço para má administração dos arrays e erros de manipulação de difícil correção. Além disso, os arrays devem ser inicializados manualmente. 

Isso é resolvido criando classes que representam unidades de um tipo de inimigo e classes que representam todo o conjunto de um tipo de inimigos, que implementam CollidableArray (por exemplo, a classe Enemies1 representa o conjunto de inimigos da classe Enemy1). 

As classes que representam unidades de inimigos são invisíveis para a Main, de forma que não há como acessar unidades na Main sem ser através dos administradores de unidades. Por consequência, os métodos para manipulação de inimigos são então encapsulados nas classes que representam os conjuntos, garantindo que todas as manipulações sejam bem reguladas. Não é necessária inicialização manual dos valores de um novo objeto inimigo, pois as classes de unidades já são instanciadas com os valores iniciais corretos. 

Os administradores armazenam uma ArrayList do tipo de inimigo que representam, mantendo uma estrutura flexível, versátil e de fácil administração.

### Enemy 3

O inimigo novo implementado funciona de um jeito totalmente diferente dos inimigos 1 e 2. Para spawnar, ele seleciona aleatóriamente um dos lados da tela, uma altura Y e um X alvo. Então ele se move horizontalmente até o X alvo, atira uma barragem de projéteis por 2 segundos, e sai da tela horizontalmente.

Detalhes da implementação:

- O spawn aleatório é feito usando o método Math.random(). 
- Para parar no X alvo, foi necessário considerar um intervalo de parada em volta do valor exato do X alvo, pois é possível que a coordenada exata seja "pulada" durante a movimentação do inimigo, por conta do delta relativamente grande que causa "saltos" no movimento. 
- O tempo de tiro é implementado usando o atributo explosionEnd, economizando assim valiosos 8 bytes por instância.
- Ao chegar no X alvo, um novo alvo fora da tela é imediatamente selecionado de acordo com o spawn da instância, usando o atributo "angulo". Assim, inimigos 3 entram e saem da tela sempre pelo mesmo lado.

As principais funcionalidades estão implementadas nos métodos updatePosition() da classe Enemy3 em conjunto com updatePositions() da classe Enemies3. Primeiramente, é importante notar que o método updatePosition() só é chamado se canShoot() retorna falso, portanto durante o período de tiro não há mudança no estado do inimigo. 

Ao chegar ao intervalo alvo, a velocidade é colocada em zero, e o atributo explosionEnd recebe o tempo atual + 2000 ms. Agora, canShoot retorna verdadeiro até o tempo atual passar de explosionEnd. Quando esse período acaba, o novo X alvo é configurado e a velocidade sai de zero, permitindo a movimentação do inimigo para sair da tela.


## Os projéteis

Os projéteis também eram administrados por uma série de arrays desconexos, portanto eles também receberam uma classe administradora que implementa CollectibleArray. Porém, o funcionamento dos projéteis funciona de forma diferente em relação às outras classes administradoras. A class Projeteis pode guardar qualquer tipo de projétil, pois na classe em si não há nenhuma diferença entre projéteis de player ou de inimigos. Assim, fica por parte dos métodos shoot() das entidades modelar o projétil a ser atirado, oferecendo flexibilidade para criar inimigos diferentes que atiram outros tipos de projétil.  

Porém, também fica por conta do usuário da classe criar objetos Projectiles diferentes para projéteis de inimigos e de player. Para instanciar um objeto Projectiles de player, é necessário passar como parâmetro um objeto player, o que serve como garantia para boa utilização do objeto.

A classe Projeteis também é responsável por verificar colisões de projéteis com entidades. Portanto, além dos dois métodos verifyCollisions determinados por CollidableArray, Projeteis também possui um verifyCollisions que recebe 3 CollidableArrays como parâmetros, por questão de eficiência na verificação. Esse método poderia ser melhorado passando como parâmetro um array de CollidableArrays, assim tornando possível a verficação de colisões com um número de entidades diferentes a ser determinado em tempo de execução.

## Os power-ups

Os power-ups, assim como todas as outras entidades do jogo (tirando o player), possuem um CollidableArray que os representa. As classes que representam os power-ups individuais extendem a classe abstrata PowerUp, pois o único método que varia entre os três é a aplicação do power-up em si, assim há reaproveitamento de código.

Todos os power-ups são representados por uma estrela de David, cada uma com sua respectiva cor. 

Quando o player está com um power-up ativo, uma estrela da cor correspondente é desenhada no meio do indicador do player. Todos os power-ups duram 5 segundos.

### PowerUp1

O power-up 1 (o amarelo) fornece invulnerabilidade, tanto para colisões com projéteis como com inimigos.

A funcionalidade do power-up 1, diferentemente dos demais, está implementada nos métodos verifyCollisions() dos CollidableArrays.

### PowerUp2

O power-up 2 (o rosa) torna todos os projéteis disparados pelo player explosivos. Ao contato, eles explodem em mais 8 projéteis, distribuídos nos ângulos de 0, 45, 90, 135, 180, 225 , 270 e 315 graus. Os projéteis criados pela explosão também são explosivos.

A funcionalidade do power-up 2 está implementada nos métodos verifyCollisions() da classe Projectiles e explode() da classe Projectile.

### PowerUp3

O power-up 3 (o azul) torna todos os projéteis disparados pelo player ricocheteantes, e adiciona mais 2 projéteis, com ângulos de 45 e 135 graus, por tiro. Ao contato com um dos lados da tela, os projéteis ricocheteantes rebatem de volta para dentro, alternando entre os ângulos de 135 e 45 graus.

A funcionalidade do power-up 3 está implementada nos métodos shoot() do player em conjunto com updateStates() da classe Projectiles.