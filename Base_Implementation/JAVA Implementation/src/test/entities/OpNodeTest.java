package test.entities;

import org.junit.jupiter.api.Test;

import entities.ExprNode;
import entities.OpNode;
import entities.NumNode;

import static org.junit.jupiter.api.Assertions.*;

class OpNodeTest {

    //Verifica o nó da esquerda
    @Test
    void testGetLeft() {
        ExprNode leftNode = new NumNode(4);
        ExprNode rightNode = new NumNode(2);
        OpNode opNode = new OpNode(leftNode, rightNode, "+");

        assertEquals(leftNode, opNode.getLeft());
    }

    //Verifica o nó da direita
    @Test
    void testGetRight() {
        ExprNode leftNode = new NumNode(4);
        ExprNode rightNode = new NumNode(2);
        OpNode opNode = new OpNode(leftNode, rightNode, "+");

        assertEquals(rightNode, opNode.getRight());
    }

    //Verifica o operador
    @Test
    void testGetOperator() {
        ExprNode leftNode = new NumNode(4);
        ExprNode rightNode = new NumNode(2);
        OpNode opNode = new OpNode(leftNode, rightNode, "+");

        assertEquals("+", opNode.getOperator());
    }

    //Verifica uma adição simples
    @Test
    void testToStringSimpleAddition() {
        ExprNode leftNode = new NumNode(4);
        ExprNode rightNode = new NumNode(2);
        OpNode opNode = new OpNode(leftNode, rightNode, "+");

        assertEquals("(4 + 2)", opNode.toString());
    }

    //Verifica uma expressão complexa
    @Test
    void testToStringComplexExpression() {
        ExprNode leftNode = new OpNode(new NumNode(1), new NumNode(2), "+");
        ExprNode rightNode = new NumNode(3);
        OpNode opNode = new OpNode(leftNode, rightNode, "*");

        assertEquals("((1 + 2) * 3)", opNode.toString());
    }

    //Verifica uma multiplicação
    @Test
    void testToStringSubtraction() {
        ExprNode leftNode = new NumNode(10);
        ExprNode rightNode = new NumNode(5);
        OpNode opNode = new OpNode(leftNode, rightNode, "-");

        assertEquals("(10 - 5)", opNode.toString());
    }

    //Verifica uma divisão
    @Test
    void testToStringDivision() {
        ExprNode leftNode = new NumNode(8);
        ExprNode rightNode = new NumNode(4);
        OpNode opNode = new OpNode(leftNode, rightNode, "/");

        assertEquals("(8 / 4)", opNode.toString());
    }
}
