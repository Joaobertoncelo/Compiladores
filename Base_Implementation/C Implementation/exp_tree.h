#ifndef EXP_TREE_H_INCLUDED
#define EXP_TREE_H_INCLUDED

#define MAX_SIZE 100

enum BOOL{FALSE, TRUE};

typedef struct sNo *pointer;
struct sNo {
    char *info;
    pointer esq, dir;
};

pointer plant();
/*
 * Inicializa a árvore
 */

int empty(pointer tree);
/*
 * Retorna TRUE caso a árvore esteja vazia, caso contrário FALSE
 */

pointer bloom(char x[]);
/*
 * Aloca memória para um nó,
 * insere x no nó e
 * retorna-o
 */

// funções auxiliares do parser
int is_operator(const char *c);
int is_number(char *c);

int parser(pointer *tree, char *string[], int size);
/*
 * Recebe uma string e devolve uma árvore que representa a expressão
 * Retorna TRUE se realizada com sucesso
 */

pointer eval_step(pointer tree);
/*
 * Recebe uma árvore que representa uma expressão e devolve uma árvore com um operador avaliado
 */

int to_string(pointer tree, char *string);
/*
 * Recebe uma árvore que representa um expressão e devolve uma string que representa a mesma expressão
 * Retorna TRUE se realizada com sucesso
 */

void print_tree(pointer tree);
/*
 * Imprime a árvore
 */

void cut_down(pointer tree);
/*
 * Libera a memória alocada pela árvore
 */

#endif // EXP_TREE_H_INCLUDED
