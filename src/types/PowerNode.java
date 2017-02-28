/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.NodeRecognizedInfo;
import core.VariablesManager;
import exceptions.NonIntegralIndexException;
import java.util.ArrayList;
import java.util.List;
import core.Token;
import core.Utils;
import exceptions.EvaluationException;
import exceptions.InvalidFormatException;

/**
 *
 * @author haitham
 */
public class PowerNode extends AbstractBinaryOpNode {

    public PowerNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    public PowerNode(
            AbstractNode parent, List<Token> allTokens,
            AbstractNode op1, AbstractNode op2) {
        super(parent, allTokens, op1, op2);
    }

    @Override
    public double evaluate() throws EvaluationException {
        return Math.pow(getChild(0).evaluate(), getChild(1).evaluate());
    }

    public static NodeRecognizedInfo findLastOccurence(
            List<Token> targetTokens,
            List<Token> allTtokens,
            AbstractNode parent,
            VariablesManager vm) throws InvalidFormatException {
        // Loop over all tokens from the end to the beginning
        for (int i = targetTokens.size() - 1; i >= 0; i--) {
            // Check if the token is enclosed in brackets, braces or parentheses
            if (!Utils.surrounded(targetTokens.get(i), targetTokens)) {
                // Try to match the operator
                if (targetTokens.get(i).getString().equals("^")) {
                    List<List<Token>> childrenTokendsLists = new ArrayList<>();
                    // Create left child tokens
                    List<Token> leftChildTokens = new ArrayList<>();
                    for (int j = 0; j < i; j++) {
                        leftChildTokens.add(targetTokens.get(j));
                    }
                    // The following error is generated if no left child is
                    // found. (remember that this library supports a generic 
                    // fail-safe error checking for missing operands, however it
                    // does not tell details like which operand is missing. 
                    // That's why we provide here this additional error checking
                    // tailored specifically for the case in hand)
                    if (leftChildTokens.isEmpty()) {
                        throw new InvalidFormatException(
                                targetTokens.get(i), 
                                allTtokens, 
                                "Power operator (^) left operand is missing.");
                    }
                    childrenTokendsLists.add(leftChildTokens);
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
                                allTtokens, 
                                "Power operator (^) right operand is missing.");
                    }
                    childrenTokendsLists.add(rightChildTokens);
                    // Return
                    return new NodeRecognizedInfo(
                            i,
                            i,
                            new PowerNode(parent, allTtokens),
                            childrenTokendsLists);
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public String toString() {
        return String.format("(%s ^ %s)", getChild(0).toString(), getChild(1).toString());
    }
}
