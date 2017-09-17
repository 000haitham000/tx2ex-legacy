/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import exceptions.IdentifierNotFoundException;
import core.NodeRecognizedInfo;
import core.VariablesManager;
import exceptions.NonIntegralIndexException;
import java.util.ArrayList;
import java.util.List;
import core.Token;
import core.Utils;
import exceptions.EvaluationException;
import java.util.Objects;

/**
 *
 * @author haitham
 */
public class VariableNode extends AbstractNode {

    private VariablesManager varManager;
    private String varLabel;

    public VariableNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    public VariableNode(AbstractNode parent, List<Token> allTokens, VariablesManager varManager, String varLabel) {
        super(parent, allTokens);
        this.varManager = varManager;
        this.varLabel = varLabel;
    }

    @Override
    public String toString() {
        return String.format("%s", varLabel);
    }

    @Override
    public double evaluate() throws IdentifierNotFoundException {
        Double identifierValue = varManager.get(varLabel);
        if (identifierValue == null) {
            throw new IdentifierNotFoundException(this, "Variable not found.");
        } else {
            return identifierValue;
        }
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
                if (targetTokens.get(i).getString().matches("[a-zA-Z_$][a-zA-Z\\d_$]*")) {
                    if (i == targetTokens.size() - 1
                            || !targetTokens.get(i + 1).getString().equals("[")) {
                        // Return
                        return new NodeRecognizedInfo(
                                i,
                                i,
                                new VariableNode(
                                        parent,
                                        allTokens,
                                        vm,
                                        targetTokens.get(i).getString()),
                                null);
                    }
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VariableNode other = (VariableNode) obj;
        if (this.varLabel != other.varLabel) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.varLabel);
        return hash;
    }
}
