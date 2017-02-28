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
public class TrigAtanNode extends AbstractNode {

    public TrigAtanNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    @Override
    public double evaluate() throws EvaluationException {
        return Math.atan(getChild(0).evaluate());
    }

    public static NodeRecognizedInfo findLastOccurence(
            List<Token> targetTokens,
            List<Token> allTtokens,
            AbstractNode parent,
            VariablesManager vm) throws InvalidFormatException {
        // Loop over all tokens from the end to the beginning
        for (int i = targetTokens.size() - 2; i >= 0; i--) {
            // Check if the token is enclosed in brackets, braces or parentheses
            if (!Utils.surrounded(targetTokens.get(i), targetTokens)) {
                // Try to match the operator
                if (targetTokens.get(i).getString().equalsIgnoreCase("atan")
                        && targetTokens.get(i + 1).getString().equals("(")) {
                    // Look for ")"
                    int j = i;
                    int last = -1;
                    while (j < targetTokens.size()) {
                        if (targetTokens.get(j).getString().equals(")")) {
                            last = j;
                        }
                        j++;
                    }
                    // Now (last) points to the location of the ")" token
                    if (last == -1) {
                        // This means that the closing ")" is missing
                        throw new InvalidFormatException(
                                targetTokens.get(i),
                                allTtokens,
                                "Un-balanced parentheses, ArcTan closing bracket ')' is missing.");
                    } else {
                        // List of lists of tokens (will only contain one
                        // list here as TrigAtanNode has only one child)
                        List<List<Token>> childrenTokensLists = new ArrayList<>();
                        // Create the tokens of the single child of the this operator
                        List<Token> childTokens = new ArrayList<>();
                        for (int k = i + 2; k < last; k++) {
                            childTokens.add(targetTokens.get(k));
                        }
                        childrenTokensLists.add(childTokens);
                        // Return
                        return new NodeRecognizedInfo(
                                i,
                                last,
                                new TrigAtanNode(parent, allTtokens),
                                childrenTokensLists);
                    }
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public String toString() {
        return String.format("atan(%s)", getChild(0).toString());
    }

}
