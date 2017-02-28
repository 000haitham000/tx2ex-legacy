/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.NodeRecognizedInfo;
import core.VariablesManager;
import java.util.ArrayList;
import java.util.List;
import core.Token;
import core.Utils;
import exceptions.EvaluationException;
import exceptions.InvalidFormatException;
import exceptions.NonIntegralIndexException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author haitham
 */
public class AssignmentNode extends AbstractBinaryOpNode {

    VariablesManager varManager;

    public AssignmentNode(
            AbstractNode parent,
            List<Token> allTokens,
            VariablesManager varManager) {
        super(parent, allTokens);
        this.varManager = varManager;
    }

    public AssignmentNode(
            AbstractNode parent, List<Token> allTokens,
            AbstractNode op1, AbstractNode op2) {
        super(parent, allTokens, op1, op2);
    }

    @Override
    public double evaluate() throws EvaluationException {
        // The notion of evaluating a command is a bit confusing. Commands are
        // not evaluated, they are however executed. We utilize
        // the evaluation function to execute then return the right hand side
        // value of the assignment. Executing an assignment command involves
        // adding its left/right hand side pair to the variables manager. On the
        // other hand evaluating it just returns the value of the right hand
        // side expression. By doing this, an assignment can be used in the
        // middle of any expression. Remember that using assignment
        // Add the new constant to the variables manager. 
        varManager.setConstant(getChild(0).toString(), getChild(1).evaluate());
        return getChild(1).evaluate();
    }

    public static NodeRecognizedInfo findLastOccurence(
            List<Token> targetTokens,
            List<Token> allTokens,
            AbstractNode parent,
            VariablesManager vm) throws InvalidFormatException {
        // Loop over all tokens from the end to the beginning
        for (int i = targetTokens.size() - 1; i >= 0; i--) {
            // Check if the token is enclosed in brackets, braces or parentheses
            if (!Utils.surrounded(targetTokens.get(i), targetTokens)) {
                // Try to match the operator
                if (targetTokens.get(i).getString().equals("=")) {
                    List<List<Token>> childrenTokensLists = new ArrayList<>();
                    // Create left child tokens
                    List<Token> leftChildTokens = new ArrayList<>();
                    for (int j = 0; j < i; j++) {
                        leftChildTokens.add(targetTokens.get(j));
                    }
                    if (leftChildTokens.isEmpty()) {
                        // There must be a left hand side operand
                        throw new InvalidFormatException(
                                targetTokens.get(i),
                                allTokens,
                                "Assignment operator (=) left hand side is missing.");
                    } else if (leftChildTokens.size() != 1 || !Utils.isIdentifier(leftChildTokens.get(0))) {
                        // Only one token on the left hand side (an identifier)
                        throw new InvalidFormatException(
                                targetTokens.get(i),
                                allTokens,
                                "The left hand side of the assignment operator (=) "
                                + "must be only an identifier.");
                    }
                    childrenTokensLists.add(leftChildTokens);
                    // Create right child tokens
                    List<Token> rightChildTokens = new ArrayList<>();
                    for (int j = i + 1; j < targetTokens.size(); j++) {
                        rightChildTokens.add(targetTokens.get(j));
                    }
                    // The following error is generated if no right child is
                    // found. (remember that this library provides a generic 
                    // fail-safe error checking for missing operands elsewhere,
                    // however it does not tell details like which operand is 
                    // missing. That's why we provide here this additional error
                    // checking, tailored specifically for the case in hand)
                    if (rightChildTokens.isEmpty()) {
                        throw new InvalidFormatException(
                                targetTokens.get(i),
                                allTokens,
                                "Assignment operator (=) right hand side is missing.");
                    }
                    childrenTokensLists.add(rightChildTokens);
                    // Return
                    return new NodeRecognizedInfo(
                            i,
                            i,
                            new AssignmentNode(parent, allTokens, vm),
                            childrenTokensLists);
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public String toString() {
        return String.format("(%s = %s)", getChild(0).toString(), getChild(1).toString());
    }
}
