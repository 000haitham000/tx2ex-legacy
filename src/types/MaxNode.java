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

/**
 *
 * @author haitham
 */
public class MaxNode extends AbstractMultiOpNode {

    private VariablesManager varManager;

    public MaxNode(
            AbstractNode parent,
            List<Token> allTokens,
            VariablesManager varManager) {
        super(parent, allTokens);
        this.varManager = varManager;
    }

    @Override
    public double evaluate() throws EvaluationException {
        double max = getChild(0).evaluate();
        for (int i = 1; i < getChildrenCount(); i++) {
            double childValue = getChild(i).evaluate();
            if (childValue > max) {
                max = childValue;
            }
        }
        return max;
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
                if (targetTokens.get(i).getString().equalsIgnoreCase("max")) {
                    // the keyword "max" must be followed by "("
                    if (targetTokens.get(i + 1).getString().equals("(")) {
                        // The following commented block used to prevent this
                        // operator from accept other nested operators that have
                        // parentheses in them e.g. tan(). So, we replaced it by
                        // the following block (borrowed from TrigTanNode)
//                      // Look for ")"
//                      int last = i;
//                      while (last < targetTokens.size()
//                              && !targetTokens.get(last).getString().equals(")")) {
//                          last++;
//                      }
                        // Look for ")" ... This is the block borrowed from TrigTanNode
                        int j = i;
                        int last = -1;
                        while (j < targetTokens.size()) {
                            if (targetTokens.get(j).getString().equals(")")) {
                                last = j;
                            }
                            j++;
                        }
                        // Now (last) points to the location of the ")" token
                        if (last == targetTokens.size()) {
                            // This means that the closing ")" is missing
                            throw new InvalidFormatException(
                                    targetTokens.get(i),
                                    allTtokens,
                                    "Max operator closing bracket ')' is missing.");
                        } else {
                            // Get all the arguments between "(" and ")"
                            // which should be separated by comma tokens ","
                            // unless only one expression is provided inside the
                            // max operator (which although useless is permitted).
                            // First, collect commas indices
                            List<Integer> commaIndices = new ArrayList<>();
                            for (int k = i + 2; k < last; k++) {
                                if (
                                        // The first condition must be there to support nesting other operators
                                        // involving commas (like summations, products, min, other max operators,
                                        // etc.) inside max operator.
                                        !Utils.surrounded(targetTokens.get(k), targetTokens.subList(i+2, last))
                                        && targetTokens.get(k).getString().equals(",")) {
                                    commaIndices.add(k);
                                }
                            }
                            // Check the pattern of the commas to see if some 
                            // child is missing e.g. max(,b) or max(a,) or
                            // max(a,,c).
                            if (commaIndices.get(0) == i + 2) {
                                // First child is missing, like in max(,b)
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "The first operand of the max operator is missing, max(?,...).");
                            } else if (Utils.twoConsecutiveValuesExist(commaIndices)) {
                                // A child is missing somewhere in the middle,
                                // like in max(a,,b)
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "Some operand of the max operator is missing, max(...,?,...).");
                            } else if (commaIndices.get(commaIndices.size() - 1) == last - 1) {
                                // Last child is missing, like in max(a,)
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "The last operand of the max operator is missing, max(...,?).");
                            } else {
                                // Reaching this point means that we have the
                                // correct format i.e. max(a,b,c,...)
                                // Now let's do the actual parsing! (0_0)
                                // Create a list of lists of tokens (will 
                                // contain an arbitrary number of lists as max
                                // can have any number of operands including 1)
                                List<List<Token>> childrenTokensLists = new ArrayList<>();
                                // Add a child for each operand
                                for (int k = -1; k < commaIndices.size(); k++) {
                                    int childFirstTokenIndex, childLastTokenIndex;
                                    if (k == -1) {
                                        childFirstTokenIndex = i + 2;
                                        childLastTokenIndex = commaIndices.get(0) - 1;
                                    } else if (k == commaIndices.size() - 1) {
                                        childFirstTokenIndex = commaIndices.get(commaIndices.size() - 1) + 1;
                                        childLastTokenIndex = last - 1;
                                    } else {
                                        childFirstTokenIndex = commaIndices.get(k) + 1;
                                        childLastTokenIndex = commaIndices.get(k + 1) - 1;
                                    }
                                    List<Token> child = new ArrayList<>();
                                    for (int kk = childFirstTokenIndex; kk <= childLastTokenIndex; kk++) {
                                        child.add(targetTokens.get(kk));
                                    }
                                    childrenTokensLists.add(child);
                                }
                                // Return
                                return new NodeRecognizedInfo(
                                        i,
                                        last,
                                        new MaxNode(
                                                parent,
                                                allTtokens,
                                                vm),
                                        childrenTokensLists);
                            }
                        }
                    }
                }
            }
        }
        // Return null if the operator was not found
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Min(");
        for (int i = 0; i < getChildrenCount(); i++) {
            sb.append(String.format("%s", getChild(i).toString()));
            if (i != getChildrenCount() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
