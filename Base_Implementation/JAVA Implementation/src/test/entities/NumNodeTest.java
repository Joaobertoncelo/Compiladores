package test.entities;

import org.junit.jupiter.api.Test;

import entities.NumNode;

import static org.junit.jupiter.api.Assertions.*;

class NumNodeTest {

    //Verifica o valor do nó
    @Test
    void testGetValue() {
        NumNode numNode = new NumNode(42);
        assertEquals(42, numNode.getValue());
    }

    //Verifica a representação do nó
    @Test
    void testToString() {
        NumNode numNode = new NumNode(123);
        assertEquals("123", numNode.toString());
    }
}
