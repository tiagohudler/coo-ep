# Documentação e Críticas

## O player

As características do player são ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação 


## Os inimigos

Os inimigos eram administrados por meio de diversos arrays, onde cada índice representava um inimigo. Isso abre espaço para má administração dos arrays e erros de manipulação de difícil correção. Além disso, os arrays devem ser inicializados manualmente.
Isso é resolvido criando uma interface para representar inimigos (Enemy), classes que representam unidades de um tipo de inimigo e implementam Enemy, e classes que representam todo o conjunto de um tipo de inimigos. Os métodos para manipulação de inimigos são então encapsulados nas classes que representam os conjuntos, garantindo que todas as manipulações sejam bem reguladas. Não é necessária inicialização manual dos valores de um novo objeto inimigo, pois a interface Enemy garante que os valores serão tratados na criação do objeto.