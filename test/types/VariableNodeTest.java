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
import exceptions.IdentifierNotFoundException;
import exceptions.TooManyDecimalPointsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author seadahai
 */
public class VariableNodeTest {
    
    private static VariablesManager varManager;
    private static VariableNode node;
    
    @BeforeClass
    public static void setUpClass() {
        varManager = new VariablesManager();
        node = new VariableNode(null, null, varManager, "x12_$09");
        varManager.set(node.toString(), 123.2e-2);
    }

    /**
     * Test of toString method, of class VariableNode.
     */
    @Test
    public void testToString() {
        System.out.println("Testing VariableNode:toString()");
        assertEquals("x12_$09", node.toString());
    }

    /**
     * Test of evaluate method, of class VariableNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing VariableNode:evaluate()");
        assertEquals(1.232, node.evaluate(), 1e-10);
    }

    /**
     * Test of evaluate method, of class VariableNode for IdentifierNotFoundException.
     */
    @Test(expected=IllegalArgumentException.class)
    public void testEvaluateIdentifierNotFound() {
        System.out.println("Testing VariableNode:evaluate() for IdentifierNotFoundException");
        // The following node is not added to the variable manager
        VariableNode node = new VariableNode(null, null, varManager, "x");
        assertEquals(1.232, node.evaluate(), 1e-10);
    }

    /**
     * Test of findLastOccurence method, of class VariableNode.
     */
    @Test
    public void testFindLastOccurence() throws TooManyDecimalPointsException {
        System.out.println("Testing VariableNode:findLastOccurence()");
        List<Token> targetTokens = Utils.getTokens("X_6$ab2");
        List<Token> allTtokens = Utils.getTokens("10.25 - X_6$ab2 + x");;
        //List<List<Token>> childrenTokensLists = new ArrayList<>();
        //List<Token> childTokens = new ArrayList<>();
        //childrenTokensLists.add(childTokens);
        //childTokens.add(new Token("25.5", 1));
        NodeRecognizedInfo expResult = new NodeRecognizedInfo(
                0,
                0,
                new VariableNode(null, allTtokens, varManager, "X_6$ab2"),
                null);
        NodeRecognizedInfo result = VariableNode.findLastOccurence(targetTokens, allTtokens, null, varManager);
        assertEquals(expResult, result);
    }
    
}
