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
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author seadahai
 */
public class VectorElementNodeTest {

    private static VariablesManager varManager;
    private static String label = "x12_$09";

    @BeforeClass
    public static void setUpClass() {
        varManager = new VariablesManager();
        varManager.setVector(label, new double[]{1023, -1.032, 0});
    }

    /**
     * Test of evaluate method, of class VectorElementNode.
     */
    @Test
    public void testEvaluate() {
        System.out.println("Testing VariableNode:evaluate()");
        for (int index = 0; index < varManager.getVector(label).length; index++) {
            System.out.println("\tTest Case: " + (index + 1));
            VectorElementNode node = new VectorElementNode(null, null, varManager, label);
            new LiteralNode(node, null, index + 1);
            assertEquals(varManager.getVector(label)[index], node.evaluate(), 1e-10);
        }
    }

    /**
     * Test of findLastOccurence method, of class VectorElementNode.
     */
    @Test
    public void testFindLastOccurence() throws Exception {
        System.out.println("Testing VariableElementNode:findLastOccurence()");
        List<Token> targetTokens = Utils.getTokens(label + "[2]");
        List<Token> allTtokens = Utils.getTokens("10.25 - " + label + "[2] + x");;
        NodeRecognizedInfo expResult = new NodeRecognizedInfo(
                0,
                3,
                new VectorElementNode(
                        null,
                        null,
                        varManager,
                        label),
                null);
        NodeRecognizedInfo result = VectorElementNode.findLastOccurence(targetTokens, allTtokens, null, varManager);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class VectorElementNode.
     */
    @Test
    public void testToString() {
        System.out.println("Testing VariableElementNode:toString()");
        VectorElementNode node = new VectorElementNode(null, null, varManager, label);
        new LiteralNode(node, null, 1);
        assertEquals(label + "[1.0]", node.toString());
    }

}
