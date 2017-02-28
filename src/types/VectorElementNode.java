/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.IndexPrecisionThresholdWatcher;
import core.NodeRecognizedInfo;
import core.VariablesManager;
import exceptions.NonIntegralIndexException;
import java.util.ArrayList;
import java.util.List;
import core.Token;
import core.Utils;
import exceptions.EvaluationException;
import exceptions.IdentifierNotFoundException;
import exceptions.InvalidFormatException;
import exceptions.VectorIndexOutOfBoundsException;

/**
 *
 * @author haitham
 */
public class VectorElementNode extends AbstractNode implements IndexPrecisionThresholdWatcher {

    private VariablesManager varManager;
    private String varLabel;

    public VectorElementNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    public VectorElementNode(AbstractNode parent, List<Token> allTokens, VariablesManager varManager, String varLabel) {
        super(parent, allTokens);
        this.varManager = varManager;
        this.varLabel = varLabel;
    }

    @Override
    public double evaluate() throws EvaluationException {
        double[] variableVector = varManager.getVector(varLabel);
        if(variableVector == null) {
            throw new IdentifierNotFoundException(this, "Array not found.");
        }
        double index = getChild(0).evaluate();
        if (Math.abs(index - Math.round(index)) < INDEX_ACCURACY_THRESHOLD) {
            if((index - 1) < varManager.getVector(varLabel).length) {
                return varManager.getVector(varLabel)[(int) index - 1];
            } else {
                throw new VectorIndexOutOfBoundsException(
                        this, 
                        null, 
                        (int) index, 
                        varManager.getVector(varLabel).length);
            }
        } else {
            throw new NonIntegralIndexException(getChild(0), "Vector index must be an integer value.");
        }
    }

    public static NodeRecognizedInfo findLastOccurence(
            List<Token> targetTokens,
            List<Token> allTokens,
            AbstractNode parent,
            VariablesManager vm) throws InvalidFormatException {
        // Loop over all tokens from the end to the beginning
        for (int i = targetTokens.size() - 2; i >= 0; i--) {
            // Check if the token is enclosed in brackets, braces or parentheses
            if (!Utils.surrounded(targetTokens.get(i), targetTokens)) {
                // Try to match the operator
                if (targetTokens.get(i).getString().matches("[a-zA-Z_$][a-zA-Z\\d_$]*")
                        && targetTokens.get(i + 1).getString().equals("[")) {
                    // Look for "]"
                    int last = i;
                    while (last < targetTokens.size()
                            && !targetTokens.get(last).getString().equals("]")) {
                        last++;
                    }
                    // Now j points to the location of the "]" token
                    if (last == targetTokens.size()) {
                        // This means that the closing "]" is missing
                        throw new InvalidFormatException(
                                targetTokens.get(i),
                                allTokens,
                                "Vector closing bracket ']' is missing.");
                    } else {
                        // List of lists of tokens (will only contain one
                        // list here as VectorElementNode has only one child)
                        List<List<Token>> childrenTokensLists = new ArrayList<>();
                        // Create the tokens of the single child of the this operator
                        List<Token> childTokens = new ArrayList<>();
                        for (int j = i + 2; j < last; j++) {
                            childTokens.add(targetTokens.get(j));
                        }
                        childrenTokensLists.add(childTokens);
                        // Return
                        return new NodeRecognizedInfo(
                                i,
                                last,
                                new VectorElementNode(
                                        parent,
                                        allTokens,
                                        vm,
                                        targetTokens.get(i).getString()),
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
        return String.format("%s[%s]", varLabel, getChild(0).toString());
    }
}
