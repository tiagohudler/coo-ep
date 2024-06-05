# Documentação e Críticas

## O player

As características do player são ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação de uma classe que representa o player, com todos os atributos e métodos necessários para sua manipulação. 

A classe, além dos atributos básicos de qualquer entidade como coordenadas e velocidade, possui vidas e um identificador de power-up. Este identificador é modificado pelas classes de power-ups ao haver colisão do player com algum power-up durante o jogo, e dependendo do seu valor muda algumas características do comportamento do player (veja seção "Os power-ups").

## Collidable Arrays

CollidableArray é a interface que junta todos os administradores de entidades. A interface reúne todos os métodos que os administradores devem ter, como:
- verifyCollisions(): verifica colisões com outro CollidableArray ou com o player;
- size(): retorna quantas entidades o array está armazenando;
- remove(i): remove o elemento i do array;
- draw(): desenha todos os elementos guardados.


## Os inimigos

Os inimigos eram administrados por meio de diversos arrays, onde cada índice representava um inimigo. Isso abre espaço para má administração dos arrays e erros de manipulação de difícil correção. Além disso, os arrays devem ser inicializados manualmente. 

Isso é resolvido criando classes que representam unidades de um tipo de inimigo e classes que representam todo o conjunto de um tipo de inimigos, que implementam CollidableArray (por exemplo, a classe Enemies1 representa o conjunto de inimigos da classe Enemy1). 

As classes que representam unidades de inimigos são invisíveis para a Main, de forma que não há como acessar unidades na Main sem ser através dos administradores de unidades. Por consequência, os métodos para manipulação de inimigos são então encapsulados nas classes que representam os conjuntos, garantindo que todas as manipulações sejam bem reguladas. Não é necessária inicialização manual dos valores de um novo objeto inimigo, pois as classes de unidades já são instanciadas com os valores iniciais corretos. 

Os administradores armazenam uma ArrayList do tipo de inimigo que representam, mantendo uma estrutura flexível, versátil e de fácil administração.


## Os projéteis

Os projéteis também são administrados por uma série de arrays desconexos. 