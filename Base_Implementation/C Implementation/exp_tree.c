#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "exp_tree.h"

#define ARRAYSIZE(a) (sizeof(*a) / sizeof(**a))

pointer plant() {
    // Funciona!
    return NULL;
}

int empty(pointer tree) {
    // Funciona!
    return (tree == NULL) ? TRUE : FALSE;
}

pointer bloom(char x[]) {
    // Funciona!
    pointer node = (pointer) malloc(sizeof(struct sNo));
    if(node == NULL) {
        fprintf(stderr, "malloc failed for node in bloom.\n");
        exit(EXIT_FAILURE);
    }
    node->info = (char *) malloc(strlen(x) + 1);
    if (node->info == NULL) {
        free(node);
        fprintf(stderr, "malloc failed for node->info in bloom.\n");
        exit(EXIT_FAILURE);
    }
    strcpy(node->info, x);
    node->dir = NULL;
    node->esq = NULL;
    
    return node;
}

int is_operator(const char *c) {
    // Funciona!
    return (strcmp(c, "*") == 0
        || strcmp(c, "/") == 0
        || strcmp(c, "+") == 0
        || strcmp(c, "-") == 0);
}
int is_number(char *c) {
    // Funciona!
    char str[MAX_SIZE];
    int num = atoi(c);
    sprintf(str, "%d", num);
    return (strcmp(str, c) == 0);
}
int check_precedence(char *c) {
    // Funciona!
    if(strcmp(c, "*") == 0 || strcmp(c, "/") == 0) return 2;
    else if(strcmp(c, "+") == 0 || strcmp(c, "-") == 0) return 1;
    return 0;
}

int add_to_tree(pointer *tree, char *queue[], int *line) {
    // Funciona!
    if(((*line) >= 0) && (is_operator(queue[*line]))) {
        (*tree) = bloom(queue[*line]);
        queue[(*line)--] = "\0";
        add_to_tree(&((*tree)->dir), queue, line);
        add_to_tree(&((*tree)->esq), queue, line);
    } else if (((*line) >= 0) && (is_number(queue[*line]))) {
        (*tree) = bloom(queue[*line]);
        queue[(*line)--] = "\0";
    }

    return TRUE;
}

int parser(pointer *tree, char *string[], int size) {
    // Funciona!
    int i = 0, j = 0, k = -1;
    char **opStack = malloc(size * sizeof(char *));
    if(opStack == NULL) {
        fprintf(stderr, "malloc failed for opStack in parser.\n");
        exit(EXIT_FAILURE);
    }
    char **outputQueue = malloc(size * sizeof(char *));
    if(outputQueue == NULL) {
        fprintf(stderr, "malloc failed for outputQueue in parser.\n");
        exit(EXIT_FAILURE);
    }

    for(i = 0; i < size; i++) {
        if(is_number(string[i])) {
            outputQueue[j++] = string[i];
        } else if(is_operator(string[i])) {
            while(k >= 0 && check_precedence(opStack[k]) >= check_precedence(string[i])) {
                outputQueue[j++] = opStack[k--];
            }
            opStack[++k] = string[i];
        } else if(strcmp(string[i], "(") == 0) {
            opStack[++k] = string[i];
        } else if(strcmp(string[i], ")") == 0) {
            while(k >= 0 && strcmp(opStack[k], "(") != 0) {
                outputQueue[j++] = opStack[k--];
            }
            if(k >= 0 && strcmp(opStack[k], "(") == 0) k--; // Pop "(" de opStack
        }
    }
    while(k >= 0) {
        outputQueue[j++] = opStack[k--];
    }
    --j;
    add_to_tree(tree, outputQueue, &j);

    free(opStack);
    free(outputQueue);

    return TRUE;
}

pointer eval_step(pointer tree) {
    // Funciona!
    if (tree == NULL) return NULL;

    if (is_number(tree->esq->info) && is_number(tree->dir->info)) {
        int left_value = atoi(tree->esq->info);
        int right_value = atoi(tree->dir->info);
        int result;

        if (strcmp(tree->info, "+") == 0) result = left_value + right_value;
        else if (strcmp(tree->info, "-") == 0) result = left_value - right_value;
        else if (strcmp(tree->info, "*") == 0) result = left_value * right_value;
        else if (strcmp(tree->info, "/") == 0) {
            if (right_value != 0) result = left_value / right_value;
            else {
                fprintf(stderr, "error: tried to divide by zero.\n");
                exit(EXIT_FAILURE);
            }
        }

        free(tree->esq->info);
        free(tree->dir->info);
        free(tree->esq);
        free(tree->dir);

        char result_str[MAX_SIZE];
        sprintf(result_str, "%d", result);

        free(tree->info);
        tree->info = strdup(result_str);
        tree->esq = NULL;
        tree->dir = NULL;
    } else {
        if (tree->esq != NULL && is_operator(tree->esq->info)) tree->esq = eval_step(tree->esq);
        if (tree->dir != NULL && is_operator(tree->dir->info)) tree->dir = eval_step(tree->dir);
    }

    return tree;
}

int to_string(pointer tree, char *string) {
    // Funciona!
    if(tree->esq != NULL) {
        if((check_precedence(tree->info) >= check_precedence(tree->esq->info))
                                    && (check_precedence(tree->esq->info) != 0)) {
            strcat(string, "(");
            to_string(tree->esq, string);
            strcat(string, ")");
        } else {
            to_string(tree->esq, string);
        }
    }
    strcat(string, tree->info);
    if(tree->dir != NULL) {
        if((check_precedence(tree->info) >= check_precedence(tree->dir->info))
                                    && (check_precedence(tree->dir->info) != 0)) {
            strcat(string, "(");
            to_string(tree->dir, string);
            strcat(string, ")");
        } else {
            to_string(tree->dir, string);
        }
    }
    
    return TRUE;
}

void print_tree(pointer tree) {
    // Funciona!
    if(tree != NULL) {
        print_tree(tree->esq);
        printf("%s ", tree->info);
        print_tree(tree->dir);
    }
}

void cut_down(pointer tree) {
    // Funciona!
    if(tree == NULL) return;
    cut_down(tree->esq);
    cut_down(tree->dir);

    free(tree);
}
