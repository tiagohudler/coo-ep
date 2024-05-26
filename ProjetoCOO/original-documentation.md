# Documentação e Críticas

## O player

As características do player são ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação de uma classe que representa o player, com todos os atributos e métodos necessários para sua manipulação.


## Os inimigos

Os inimigos eram administrados por meio de diversos arrays, onde cada índice representava um inimigo. Isso abre espaço para má administração dos arrays e erros de manipulação de difícil correção. Além disso, os arrays devem ser inicializados manualmente. <br>

Isso é resolvido criando uma classe abstrata para representar inimigos (Enemy), classes que representam unidades de um tipo de inimigo e estendem Enemy, e classes que representam todo o conjunto de um tipo de inimigos (por exemplo, a classe Enemies1 representa o conjunto de inimigos da classe Enemy1). 

As classes que representam unidades de inimigos são invisíveis para a Main, de forma que não há como acessar unidades na Main sem ser através dos administradores de unidades. Por consequência, os métodos para manipulação de inimigos são então encapsulados nas classes que representam os conjuntos, garantindo que todas as manipulações sejam bem reguladas. Não é necessária inicialização manual dos valores de um novo objeto inimigo, pois as classes de unidades já são instanciadas com os valores iniciais corretos. <br>

Os administradores armazenam uma ArrayList do tipo de inimigo que representam, mantendo uma estrutura flexível, versátil e de fácil administração.

## Os projéteis

Os projéteis também são administrados por uma série de arrays desconexos. 