# Documentação e Críticas

## O player

As características do player são ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação 


## Os inimigos

Os inimigos eram administrados por meio de diversos arrays, onde cada índice representava um inimigo. Isso abre espaço para má administração dos arrays e erros de manipulação de difícil correção. 
Isso é resolvido criando uma classe para representar inimigos, e outra que representa especificamente um tipo de inimigo. Os métodos para menipulação de inimigos são então encapsulados nesta classe, garantindo que todas as manipulações sejam bem reguladas.