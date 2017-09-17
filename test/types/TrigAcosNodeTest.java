/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.NodeRecognizedInfo;
import core.Token;
import core.Utils;
import core.VariablesManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author seadahai
 */
public class TrigAcosNodeTest {
    
    /**
     * Test of evaluate method, of class TrigAcosNode.
     */
    @Test
    public void testEvaluate() {
        TrigAcosNode node;
        System.out.println("Testing TrigAcosNode:evaluate() (Test 1)");
        for (int i = 0; i < 10; i++) {
            System.out.println("\tTest Case: " + (i + 1));
            node = new TrigAcosNode(null, null);
            double rand = new Random().nextDouble() * Math.PI;
            new LiteralNode(node, null, rand);
            assertEquals(Math.acos(rand), node.evaluate(), 1e-10);
        }
        System.out.println("Testing TrigAcosNode:evaluate() (Test 2)");
        node = new TrigAcosNode(null, null);
        new LiteralNode(node, null, 0.0);
        assertEquals(Math.acos(0.0), node.evaluate(), 1e-10);
        System.out.println("Testing TrigAcosNode:evaluate() (Test 3)");
        node = new TrigAcosNode(null, null);
        new LiteralNode(node, null, Math.PI / 2);
        assertEquals(Math.acos(Math.PI / 2), node.evaluate(), 1e-10);
        System.out.println("Testing TrigAcosNode:evaluate() (Test 4)");
        node = new TrigAcosNode(null, null);
        new LiteralNode(node, null, Math.PI);
        assertEquals(Math.acos(Math.PI), node.evaluate(), 1e-10);
        System.out.println("Testing TrigAcosNode:evaluate() (Test 5)");
        node = new TrigAcosNode(null, null);
        new LiteralNode(node, null, 3.0 / 2.0 * Math.PI);
        assertEquals(Math.acos(Math.PI * 3 / 2), node.evaluate(), 1e-10);
    }

    /**
     * Test of findLastOccurence method, of class TrigAcosNode.
     */
    @Test
    public void testFindLastOccurence() throws Exception {
        System.out.println("Testing TrigAcosNode:findLastOccurence()");
        List<Token> targetTokens = Utils.getTokens("acos(25.5)");
        List<Token> allTtokens = Utils.getTokens("10.25 - acos(25.5) + x");;
        List<List<Token>> childrenTokensLists = new ArrayList<>();
        List<Token> childTokens = new ArrayList<>();
        childrenTokensLists.add(childTokens);
        childTokens.add(new Token("25.5", 1));
        NodeRecognizedInfo expResult = new NodeRecognizedInfo(
                0,
                3,
                new TrigSinNode(null, allTtokens),
                childrenTokensLists);
        NodeRecognizedInfo result = TrigAcosNode.findLastOccurence(targetTokens, allTtokens, null, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class TrigAcosNode.
     */
    @Test
    public void testToString() {
        System.out.println("Testing TrigAcosNode:toString()");
        TrigTanNode node = new TrigTanNode(null, null);
        new LiteralNode(node, null, 25.5);
        assertEquals("tan(25.5)", node.toString());
    }
    
}
