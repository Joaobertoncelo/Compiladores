#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>
#include "exp_tree.h"

int isValid(char string[]);
/*
 * Checa se a string inserida é uma expressão válida ou não
 *  Retorna TRUE se é válida, FALSE caso contrário
 */
int lexer(char string[], char *arr_token[]);
/*
 * Recebe uma string e devolve um arranjo de tokens
 * Retorna o tamanho do arranjo de tokens
 */

int main() {
    setlocale(LC_ALL, "");

    char exp[MAX_SIZE]; // talvez eu deixe maior pra evitar qualquer coisa
    int check = 0;
    do {
        printf("Insira a expressão: ");
        fgets(exp, sizeof(exp), stdin);
        exp[strcspn(exp, "\n")] = '\0';
        if((isValid(exp)) == FALSE) {
            printf("Expressão inválida!\nTente Novamente...\n\n");
            check = FALSE;
        } else check = TRUE;
    } while(check == FALSE);

    pointer exp_tree = plant();
    char *arr_token[MAX_SIZE] = {NULL};
    int size = lexer(exp, arr_token);
    
    int i = 0;
    printf("Tokens: [");
    while(i <= size-2) {
        printf("\"%s\", ", arr_token[i]);
        i++;
    }
    printf("\"%s\"]\n", arr_token[i]);

    parser(&exp_tree, arr_token, size);

    printf("Árvore inicial: ( ");
    print_tree(exp_tree);
    printf(")\n");

    printf("Passos de avaliação:\n");
    while (!(exp_tree->esq == NULL && exp_tree->dir == NULL && is_number(exp_tree->info))) {
        exp_tree = eval_step(exp_tree);
        print_tree(exp_tree);
        printf("\n");
    }
    
    cut_down(exp_tree);

    return 0;
}

int isValid(char string[]) {
    // Funciona!
    int i = 0, balance = 0;
    while(string[i] != '\0') {
        if(!(string[i] == '(' 
        || string[i] == ')' 
        || string[i] == '*'
        || string[i] == '/'
        || string[i] == '+'
        || string[i] == '-'
        || string[i] == ' '
        || (string[i] >= '0' && string[i] <= '9'))) {
            return FALSE;
        }
        if (string[i] == '(') {
            balance++;
        } else if (string[i] == ')') {
            balance--;
        }
        
        i++;
    }
    if (balance != 0) {
        return FALSE;
    }
    return TRUE;
}

int lexer(char string[], char *arr_token[]) {
    // Funciona!
    int i = 0, j = 0, size = 0;
    arr_token[size] = malloc(MAX_SIZE);
    if (arr_token[size] == NULL) {
        fprintf(stderr, "malloc failed for arr_token in lexer\n");
        exit(EXIT_FAILURE);
    }
    while(i < strlen(string)) {
        if(string[i] == '(' 
        || string[i] == ')' 
        || string[i] == '*'
        || string[i] == '/'
        || string[i] == '+') {
            arr_token[size][j++] = string[i++];
            arr_token[size][j] = '\0';
            size++;
            j = 0;

            if(size < MAX_SIZE) {
                arr_token[size] = malloc(MAX_SIZE);
                if (arr_token[size] == NULL) {
                    fprintf(stderr, "malloc failed for arr_token in lexer\n");
                    exit(EXIT_FAILURE);
                }
            }
        } else if((string[i] == '-') || ((string[i] >= '0') && (string[i] <= '9'))) {
            arr_token[size][j++] = string[i++];
            while (string[i] >= '0' && string[i] <= '9') {
                arr_token[size][j++] = string[i++];
            }
            arr_token[size][j] = '\0';
            size++;
            j = 0;

            if(size < MAX_SIZE) {
                arr_token[size] = malloc(MAX_SIZE);
                if (arr_token[size] == NULL) {
                    fprintf(stderr, "malloc failed for arr_token in lexer\n");
                    exit(EXIT_FAILURE);
                }
            }
        } else i++;
    }

    return size;
}
