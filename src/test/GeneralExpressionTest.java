/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import core.VariablesManager;
import exceptions.EvaluationException;
import exceptions.NonIntegralIndexException;
import types.AdditionBinaryOpNode;
import types.LiteralNode;
import types.MultiplicationBinaryOpNode;
import types.PowerNode;
import types.ProductNode;
import types.SummationNode;
import types.VariableNode;
import types.VectorElementNode;

/**
 *
 * @author haitham
 */
public class GeneralExpressionTest {
    
    public static void main(String[] args) throws EvaluationException {
        test1();
    }

    private static void test1() throws EvaluationException {
        double[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        double[] y = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        VariablesManager varManager = new VariablesManager();
        varManager.setVector("x", x);
        varManager.setVector("y", y);
        varManager.set("pi", Math.PI);
        // Build expression tree
        AdditionBinaryOpNode plus = new AdditionBinaryOpNode(null, null);
        PowerNode power = new PowerNode(plus, null);
        VectorElementNode x_4 = new VectorElementNode(power, null, varManager, "x");
        LiteralNode lit_4 = new LiteralNode(x_4, null, 4);
        LiteralNode lit_5 = new LiteralNode(power, null, 5);
        MultiplicationBinaryOpNode times1 = new MultiplicationBinaryOpNode(plus, null);
        LiteralNode lit_13_05 = new LiteralNode(times1, null, 13.05);
        MultiplicationBinaryOpNode times2 = new MultiplicationBinaryOpNode(times1, null);
        LiteralNode lit_pi = new LiteralNode(times2, null, varManager.get("pi"));
        SummationNode sum = new SummationNode(times2, null, varManager, "i");
        LiteralNode sum_index_start = new LiteralNode(sum, null, 5);
        LiteralNode sum_index_end = new LiteralNode(sum, null, 7);
        ProductNode prod = new ProductNode(sum, null, varManager, "j");
        LiteralNode prod_index_start = new LiteralNode(prod, null, 1);
        LiteralNode prod_index_end = new LiteralNode(prod, null, 2);
        MultiplicationBinaryOpNode times3 = new MultiplicationBinaryOpNode(prod, null);
        VectorElementNode x_i = new VectorElementNode(times3, null, varManager, "x");
        VariableNode i = new VariableNode(x_i, null, varManager, "i");
        VectorElementNode y_j = new VectorElementNode(times3, null, varManager, "y");
        VariableNode j = new VariableNode(y_j, null, varManager, "j");
        // Evaluate expression tree
        System.out.println(plus.toString());
        System.out.println("Result = " + plus.evaluate());
    }
}
