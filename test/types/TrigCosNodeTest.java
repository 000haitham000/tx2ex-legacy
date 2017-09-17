/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.MathExpressionParser;
import core.NodeRecognizedInfo;
import core.Token;
import core.Utils;
import core.VariablesManager;
import exceptions.InvalidFormatException;
import exceptions.MisplacedTokensException;
import exceptions.TooManyDecimalPointsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author seadahai
 */
public class TrigCosNodeTest {

    /**
     * Test of evaluate method, of class TrigCosNode.
     */
    @Test
    public void testEvaluate() throws TooManyDecimalPointsException {
        TrigCosNode node;
        System.out.println("Testing TrigCosNode:evaluate() (Test 1)");
        for (int i = 0; i < 10; i++) {
            System.out.println("\tTest Case: " + (i + 1));
            node = new TrigCosNode(null, null);
            double rand = new Random().nextDouble() * Math.PI;
            new LiteralNode(node, null, rand);
            assertEquals(Math.cos(rand), node.evaluate(), 1e-10);
        }
        System.out.println("Testing TrigCosNode:evaluate() (Test 2)");
        node = new TrigCosNode(null, null);
        new LiteralNode(node, null, 0.0);
        assertEquals(1.0, node.evaluate(), 1e-10);
        System.out.println("Testing TrigCosNode:evaluate() (Test 3)");
        node = new TrigCosNode(null, null);
        new LiteralNode(node, null, Math.PI / 2);
        assertEquals(0.0, node.evaluate(), 1e-10);
        System.out.println("Testing TrigCosNode:evaluate() (Test 4)");
        node = new TrigCosNode(null, null);
        new LiteralNode(node, null, Math.PI);
        assertEquals(-1.0, node.evaluate(), 1e-10);
        System.out.println("Testing TrigCosNode:evaluate() (Test 5)");
        node = new TrigCosNode(null, null);
        new LiteralNode(node, null, 3.0 / 2.0 * Math.PI);
        assertEquals(0.0, node.evaluate(), 1e-10);
    }

    /**
     * Test of findLastOccurence method, of class TrigCosNode.
     */
    @Test
    public void testFindLastOccurence() throws Exception {
        System.out.println("Testing TrigCosNode:findLastOccurence()");
        List<Token> targetTokens = Utils.getTokens("cos(25.5)");
        List<Token> allTtokens = Utils.getTokens("10.25 - cos(25.5) + x");;
        //VariablesManager vm = null;
        List<List<Token>> childrenTokensLists = new ArrayList<>();
        List<Token> childTokens = new ArrayList<>();
        childrenTokensLists.add(childTokens);
        childTokens.add(new Token("25.5", 1));
        NodeRecognizedInfo expResult = new NodeRecognizedInfo(
                0,
                3,
                new TrigCosNode(null, allTtokens),
                childrenTokensLists);
        NodeRecognizedInfo result = TrigCosNode.findLastOccurence(targetTokens, allTtokens, null, null);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class TrigCosNode.
     */
    @Test
    public void testToString() //            throws IllegalAccessException,
    //            IllegalArgumentException,
    //            TooManyDecimalPointsException,
    //            MisplacedTokensException,
    //            MisplacedTokensException,
    //            InvalidFormatException,
    //            NoSuchMethodException,
    //            Throwable
    {
//        MathExpressionParser.parse("10.3 + cos(25.69) + x", vm);        
        System.out.println("Testing TrigCosNode:toString()");
        TrigCosNode node = new TrigCosNode(null, null);
        new LiteralNode(node, null, 25.5);
        assertEquals("cos(25.5)", node.toString());
    }
}