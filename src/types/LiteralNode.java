/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.NodeRecognizedInfo;
import core.VariablesManager;
import java.util.List;
import core.Token;
import core.Utils;

/**
 *
 * @author haitham
 */
public class LiteralNode extends AbstractNode {

    double value;

    public LiteralNode(AbstractNode parent, List<Token> allTokens, double value) {
        super(parent, allTokens);
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }

    public static NodeRecognizedInfo findLastOccurence(
            List<Token> targetTokens,
            List<Token> allTokens,
            AbstractNode parent,
            VariablesManager vm) {
        // Loop over all tokens from the end to the beginning
        for (int i = targetTokens.size() - 1; i >= 0; i--) {
            // Check if the token is enclosed in brackets, braces or parentheses
            if (!Utils.surrounded(targetTokens.get(i), targetTokens)) {
                // Try to match the operator
                if (targetTokens.get(i).getString().matches("[\\d]+(\\.\\d+)?")) {
                    double value = Double.parseDouble(targetTokens.get(i).getString());
                    // Return
                    return new NodeRecognizedInfo(
                            i,
                            i,
                            new LiteralNode(parent, allTokens, value),
                            null);
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
