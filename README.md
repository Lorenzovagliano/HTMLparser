EASTER_EGG_URLS 

(Gostei bastante do easter egg. Muito criativo!)

# HTML Analyzer

## Como Usar

- Compilação: javac HtmlAnalyzer.java
- Execução: java HtmlAnalyzer inserir-url-aqui

## Comentários sobre o Problema

A modelagem deste problema foi inspirada em um aspecto de uma Árvore de Expressão (Expression Tree) para expressões matemáticas na notação infixa, com o que já tive a oportunidade de trabalhar em: https://github.com/Lorenzovagliano/DSA/tree/main/TP1. 

Esse aspecto é o uso de parentêses para definir a precedência entre subexpressões de uma expressão matemática. A diferença neste problema é que ao invés de utilizarmos parentêses, utilizamos as tags de uma estrutura HTML para construir uma árvore.

Essa construção também é similar a como o problema do "parsing" foi tratado em linguagens como LISP antes do avanço de gramáticas na ciência da computação: com muitos e muitos parentêses para auxiliar na construção da árvore de execução de um programa. Essa imagem do xkcd descreve isso muito bem: https://imgs.xkcd.com/comics/lisp_cycles.png.

## Implementação

A solução para este problema começa recuperando o conteúdo HTML do documento a partir de uma URL. Em seguida, o conteúdo HTML é dividido em linhas, onde cada linha representa uma parte do código HTML.

Para construir a árvore, o algoritmo itera sobre cada linha do HTML. Quando uma linha contém uma tag de abertura <, um novo nó é criado na árvore com essa tag. Se a tag não tiver uma correspondência de fechamento na mesma linha, o nó é adicionado à pilha. Se a tag tiver uma correspondência de fechamento </, o nó correspondente é removido da pilha.

Durante o processo de construção da árvore, é mantida uma pilha de nós visitados. Isso ajuda a determinar a relação pai-filho entre os nós. A cada nova tag de abertura, o nó correspondente é adicionado como filho do nó no topo da pilha, que representa o nó pai atual. Quando uma tag de fechamento é encontrada, o nó correspondente é removido da pilha, indicando o término do elemento correspondente.

Ao longo do processo, alguns casos de estruturas HTML mal-formadas também são testados.

Ao final do processo, a árvore completa é construída, representando a estrutura hierárquica do documento HTML. Cada nó da árvore representa uma tag HTML, e os nós filhos representam elementos aninhados dentro dessas tags.
