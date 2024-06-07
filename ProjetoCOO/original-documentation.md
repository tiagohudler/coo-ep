# Documentação e Críticas

## O player

As características do player são ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação de uma classe que representa o player, com todos os atributos e métodos necessários para sua manipulação. 

A classe, além dos atributos básicos de qualquer entidade como coordenadas e velocidade, possui vidas e um identificador de power-up. Este identificador é modificado pelas classes de power-ups ao haver colisão do player com algum power-up durante o jogo, e dependendo do seu valor muda algumas características do comportamento do player (veja seção "Os power-ups").

## Collidable Arrays

CollidableArray é a interface que junta todos os administradores de entidades. A interface reúne todos os métodos que os administradores devem ter, como:
- verifyCollisions(): verifica colisões com outro CollidableArray ou com o player;
- size(): retorna quantas entidades o array está armazenando;
- remove(int i): remove o elemento i do array;
- draw(): desenha todos os elementos guardados.


## Os inimigos

Os inimigos eram administrados por meio de diversos arrays, onde cada índice representava um inimigo. Isso abre espaço para má administração dos arrays e erros de manipulação de difícil correção. Além disso, os arrays devem ser inicializados manualmente. 

Isso é resolvido criando classes que representam unidades de um tipo de inimigo e classes que representam todo o conjunto de um tipo de inimigos, que implementam CollidableArray (por exemplo, a classe Enemies1 representa o conjunto de inimigos da classe Enemy1). 

As classes que representam unidades de inimigos são invisíveis para a Main, de forma que não há como acessar unidades na Main sem ser através dos administradores de unidades. Por consequência, os métodos para manipulação de inimigos são então encapsulados nas classes que representam os conjuntos, garantindo que todas as manipulações sejam bem reguladas. Não é necessária inicialização manual dos valores de um novo objeto inimigo, pois as classes de unidades já são instanciadas com os valores iniciais corretos. 

Os administradores armazenam uma ArrayList do tipo de inimigo que representam, mantendo uma estrutura flexível, versátil e de fácil administração.

### Enemy 3

O inimigo novo implementado funciona de um jeito totalmente diferente dos inimigos 1 e 2. Para spawnar, ele seleciona aleatóriamente um dos lados da tela, uma altura Y e um X alvo. Então ele se move horizontalmente até o X alvo, atira uma barragem de projéteis por 2 segundos, e sai da tela horizontalemente.

Detalhes da implementação:

- O spawn aleatório é feito usando o método Math.random(). 
- Para parar no X alvo, foi necessário considerar um intervalo de parada em volta do valor exato do X alvo, pois é possível que a coordenada exata seja "pulada" durante a movimentação do inimigo, por conta do delta relativamente grande que causa "saltos" no movimento. 
- O tempo de tiro é implementado usando o atributo explosionEnd, economizando assim valiosos 8 bytes por instância.
- Ao chegar no X alvo, um novo alvo fora da tela é imediatamente selecionado de acordo com o spawn da instância.


## Os projéteis

Os projéteis também eram administrados por uma série de arrays desconexos, portanto eles também receberam uma classe administradora que implementa CollectibleArray. Porém, o funcionamento dos projéteis funciona de forma diferente em relação às outras classes administradoras. A class Projeteis pode guardar qualquer tipo de projétil, pois na classe em si não há nenhuma diferença entre projéteis de player ou de inimigos. Assim, fica por parte dos métodos shoot() das entidades modelar o projétil a ser atirado, oferecendo flexibilidade para criar inimigos diferentes que atiram outros tipos de projétil.  

Porém, também fica por conta do usuário da classe criar objetos Projectiles diferentes para projéteis de inimigos e de player. Para instanciar um objeto Projectiles de player, é necessário passar como parâmetro um objeto player, o que serve como garantia para boa utilização do objeto.

A classe Projeteis também é responsável por verificar colisões de projéteis com entidades. Portanto, além dos dois métodos verifyCollisions determinados por CollidableArray, Projeteis também possui um verifyCollisions que recebe 3 CollidableArrays como parâmetros, por questão de eficiência na verificação. Esse método poderia ser melhorado passando como parâmetro um array de CollidableArrays, assim tornando possível a verficação de colisões com um número de entidades diferentes a ser determinado em tempo de execução.
