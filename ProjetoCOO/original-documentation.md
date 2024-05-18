# Documentação e Críticas

## O player

As características do player são ditadas por vários arrays e variáveis disconexos, cuja manutenção está completamente na mão do usuário. Por exemplo, a inicialização das variáveis do player é feita manualmente no início do método main. Esse problema é resolvido com a criação de uma classe que representa o player, quem contém todas as informações referentes a ele e encapsula todas as operações que envolvem o player.