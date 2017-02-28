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

/**
 *
 * @author haitham
 */
public class AdditionBinaryOpNode extends AbstractBinaryOpNode {

    public AdditionBinaryOpNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    public AdditionBinaryOpNode(
            AbstractNode parent, List<Token> allTokens,
            AbstractNode op1, AbstractNode op2) {
        super(parent, allTokens, op1, op2);
    }

    @Override
    public double evaluate() throws EvaluationException {
        if (getChildrenCount() == 2) {
            // A normal subtraction
            return getChild(0).evaluate() + getChild(1).evaluate();
        } else {
            // A negative sign
            return 0 + getChild(0).evaluate();
        }
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
                if (targetTokens.get(i).getString().equals("+")) {
                    List<List<Token>> childrenTokensLists = new ArrayList<>();
                    // Create left child tokens
                    List<Token> leftChildTokens = new ArrayList<>();
                    for (int j = 0; j < i; j++) {
                        leftChildTokens.add(targetTokens.get(j));
                    }
                    if (!leftChildTokens.isEmpty()) {
                        // The presence of some tokens on the left hand side
                        // means that this is a normal bioperand subtraction
                        // operator. Otherwise it will be a unary negative sign.
                        childrenTokensLists.add(leftChildTokens);
                    }
                    // Create right child tokens
                    List<Token> rightChildTokens = new ArrayList<>();
                    for (int j = i + 1; j < targetTokens.size(); j++) {
                        rightChildTokens.add(targetTokens.get(j));
                    }
                    // The following error is generated if no right child is
                    // found. (remember that this library supports a generic 
                    // fail-safe error checking for missing operands, however it
                    // does not tell details like which operand is missing. 
                    // That's why we provide here this additional error checking
                    // tailored specifically for the case in hand)
                    if (rightChildTokens.isEmpty()) {
                        throw new InvalidFormatException(
                                targetTokens.get(i), 
                                allTokens, 
                                "Addition operator (+) right operand is missing.");
                    }
                    childrenTokensLists.add(rightChildTokens);
                    // Return
                    return new NodeRecognizedInfo(
                            i, 
                            i, 
                            new AdditionBinaryOpNode(parent, allTokens), 
                            childrenTokensLists);
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public String toString() {
        if (getChildrenCount() == 2) {
            // Normal subtraction
            return String.format("(%s + %s)", getChild(0).toString(), getChild(1).toString());
        } else {
            // Just a negative sign
            return String.format("(+ %s)", getChild(0).toString());
        }
    }
}
