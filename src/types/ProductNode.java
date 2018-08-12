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
public class ProductNode extends AbstractMultiOpNode {

    private VariablesManager varManager;
    private String indexLabel;

    public ProductNode(AbstractNode parent, List<Token> allTokens, VariablesManager varManager) {
        super(parent, allTokens);
        this.varManager = varManager;
    }

    public ProductNode(
            AbstractNode parent,
            List<Token> allTokens,
            VariablesManager varManager,
            String indexLabel) {
        super(parent, allTokens);
        this.varManager = varManager;
        this.indexLabel = indexLabel;
    }

    @Override
    public double evaluate() throws EvaluationException {
        double indexStart = getChild(0).evaluate();
        double indexEnd = getChild(1).evaluate();
        AbstractNode stepChild = null;
        AbstractNode expressionChild;
        if (getChildrenCount() == 3) {
            expressionChild = getChild(2);
        } else {
            // Reaching this point means that the summation operator being
            // evaluated has four children i.e. five arguments (remember that
            // the first argument - which is the index label - is not added as
            // a child during parsing because it needed no evaluation). In such
            // case the third child is the step (not the expression) and the
            // fourth child is the expression.
            stepChild = getChild(2);
            expressionChild = getChild(3);
        }
        double prod = 1;
        if (stepChild == null) {
            for (double i = indexStart; i <= indexEnd; i++) {
                varManager.set(String.valueOf(getIndexLabel()), i);
                prod *= expressionChild.evaluate();
                varManager.remove(String.valueOf(getIndexLabel()));
            }
        } else {
            if (indexStart <= indexEnd) {
                for (double i = indexStart; i <= indexEnd; i += stepChild.evaluate()) {
                    varManager.set(String.valueOf(getIndexLabel()), i);
                    prod *= expressionChild.evaluate();
                    varManager.remove(String.valueOf(getIndexLabel()));
                }
            } else {
                for (double i = indexStart; i >= indexEnd; i += stepChild.evaluate()) {
                    varManager.set(String.valueOf(getIndexLabel()), i);
                    prod *= expressionChild.evaluate();
                    varManager.remove(String.valueOf(getIndexLabel()));
                }
            }
        }
        return prod;
    }

    /**
     * @return the indexLabel
     */
    public String getIndexLabel() {
        return indexLabel;
    }

    /**
     * @param indexLabel the indexLabel to set
     */
    public void setIndexLabel(String indexLabel) {
        this.indexLabel = indexLabel;
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
                if (targetTokens.get(i).getString().equalsIgnoreCase("prod")) {
                    // the keyword "prod" must be followed by "{"
                    if (targetTokens.get(i + 1).getString().equals("{")) {
                        // Look for "}"
                        int last = i;
                        // The following loop looks for the summation closing
                        // curly bracket. This loop is modified from an earlier
                        // version looking for the directly next closing curly
                        // bracket regardless of it being surrounded or not.
                        // That older version did not support nesting
                        // summations, products, mins, maxs or any other
                        // operator that uses curly brackets inside a summation
                        // operator. The new version version looks for the
                        // closing curly bracket that is not surrounded. This
                        // means that the new version will not mistaken
                        // a closing curly bracket of a nested operator (e.g.
                        // a product) with the closing curly bracket of the
                        // outer operator (in our case summation).
                        while (last < targetTokens.size()) {
                            if (targetTokens.get(last).getString().equals("}")
                                    && !Utils.surrounded(targetTokens.get(last),
                                            targetTokens.subList(i + 2, targetTokens.size()))) {
                                break;
                            }
                            last++;
                        }
                        // Now (last) points to the location of the "}" token
                        if (last == targetTokens.size()) {
                            // This means that the closing "}" is missing
                            throw new InvalidFormatException(
                                    targetTokens.get(i),
                                    allTtokens,
                                    "Product operator closing bracket '}' is missing.");
                        } else {
                            // Get the four arguments between "{" and "}"
                            // which should be separated by three comma tokens ","
                            // First we need to know the position of each comma ","
                            int comma1Pos = -1;
                            int comma2Pos = -1;
                            int comma3Pos = -1;
                            int comma4Pos = -1;
                            for (int j = i + 2;
                                    j < last /* used to be targetTokens.size(), now changed to last since we should be looking only between { and } */;
                                    j++) {
                                if ( // The first condition can be removed (unlike the case of min and max 
                                        // operators). This is because the number of commas in the summation
                                        // operator is fixed (3) and they all precede any other commas that might
                                        // exist in the last argument of the sum operator. However, this condition
                                        // is left here for convenience and consistency with the definitions of
                                        // other operators (like min and max) however it has no functional value.
                                        !Utils.surrounded(targetTokens.get(j), targetTokens.subList(i + 2, last))
                                        && targetTokens.get(j).getString().equals(",")) {
                                    if (comma1Pos == -1) {
                                        comma1Pos = j;
                                    } else if (comma2Pos == -1) {
                                        comma2Pos = j;
                                    } else if (comma3Pos == -1) {
                                        comma3Pos = j;
                                    } else if (comma4Pos == -1) {
                                        comma4Pos = j;
                                    }
                                }
                            }
                            // If you are not able to find three commas then the
                            // format is invalid and some operator is missing.
                            if (comma1Pos == -1 || comma2Pos == -1 || comma3Pos == -1) {
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "Product operator needs four operands (index label, start value, end value and expression), separated by three commas.");
                            }
                            if (comma1Pos - (i + 1) < 2) {
                                // Missing first operand (index label)
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "The first operand of the summation operator (index label) is missing.");
                            }
                            if (comma1Pos - (i + 1) > 2) {
                                // Too many tokens for first operand (index label)
                                // Remember that the first operand is an identifier
                                // which means it should be only one token.
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "Product operator first operand must be only one token, which is a valid identifier representing index label.");
                            }
                            if (comma2Pos - comma1Pos < 2) {
                                // Missing second operand (index start)
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "The second operand of the product operator (index start value) is missing.");
                            }
                            if (comma3Pos - comma2Pos < 2) {
                                // Missing third operand (index end)
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "The third operand of the product operator (index end value) is missing.");
                            }
                            // Check for fourth and fifth operators existence
                            // depends on whether the product operator being
                            // parsed has four or five arguments.
                            if (comma4Pos == -1) {
                                // If the product operator being parsed has
                                // only four arguments, then the fourth argument
                                // should be the expression.
                                if (last - comma3Pos < 2) {
                                    // Missing fourth operand (index end)
                                    throw new InvalidFormatException(
                                            targetTokens.get(i),
                                            allTtokens,
                                            "The fourth operand of the product operator (expression) is missing.");
                                }
                            } else {
                                // If the product operator being parsed has
                                // five arguments, then the fourth argument
                                // should be the step and the fifth should be
                                // the expression.
                                if (comma4Pos - comma3Pos < 2) {
                                    // Missing fourth operand (index end)
                                    throw new InvalidFormatException(
                                            targetTokens.get(i),
                                            allTtokens,
                                            "The fourth operand of the product operator (step) is missing.");
                                }
                                if (last - comma4Pos < 2) {
                                    // Missing fourth operand (index end)
                                    throw new InvalidFormatException(
                                            targetTokens.get(i),
                                            allTtokens,
                                            "The fisth operand of the product operator (expression) is missing.");
                                }
                            }
                            if (!targetTokens.get(i + 2).getString().matches("[a-zA-Z_$][a-zA-Z\\d_$]*")) {
                                // This means that the indexLabel is not a valid identifier
                                throw new InvalidFormatException(
                                        targetTokens.get(i),
                                        allTtokens,
                                        "Product operator index label must be a valid identifier.");
                            }
                            // Now let's do the actual parsing! (0_0)
                            // List of lists of tokens (will contain three
                            // lists here as product has three children
                            // expressions i.e. indexStart, indexEnd and the
                            // expression of to be multiplied)
                            List<List<Token>> childrenTokendsLists = new ArrayList<>();
                            // tokens representing index start
                            List<Token> child1 = new ArrayList<>();
                            for (int j = comma1Pos + 1; j < comma2Pos; j++) {
                                child1.add(targetTokens.get(j));
                            }
                            childrenTokendsLists.add(child1);
                            // tokens representing index end
                            List<Token> child2 = new ArrayList<>();
                            for (int j = comma2Pos + 1; j < comma3Pos; j++) {
                                child2.add(targetTokens.get(j));
                            }
                            childrenTokendsLists.add(child2);
                            // If a fourth comma exists (i.e. a fifth argument
                            // exists), add it to the children list. Otherwise,
                            // the fourth argument if the last child
                            if (comma4Pos != -1) {
                                // tokens representing the step of summation
                                List<Token> child3 = new ArrayList<>();
                                for (int j = comma3Pos + 1; j < comma4Pos; j++) {
                                    child3.add(targetTokens.get(j));
                                }
                                childrenTokendsLists.add(child3);
                                // tokens representing the expression to be summed
                                List<Token> child4 = new ArrayList<>();
                                for (int j = comma4Pos + 1; j < last; j++) {
                                    child4.add(targetTokens.get(j));
                                }
                                childrenTokendsLists.add(child4);
                            } else {
                                // tokens representing the expression to be summed
                                List<Token> child3 = new ArrayList<>();
                                for (int j = comma3Pos + 1; j < last; j++) {
                                    child3.add(targetTokens.get(j));
                                }
                                childrenTokendsLists.add(child3);
                            }
                            // Return
                            return new NodeRecognizedInfo(
                                    i,
                                    last,
                                    new ProductNode(
                                            parent,
                                            allTtokens,
                                            vm,
                                            targetTokens.get(i + 2).getString()),
                                    childrenTokendsLists);
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
        sb.append(String.format("Prod{%s=[%s,%s],", indexLabel, getChild(0).toString(), getChild(1).toString()));
        if(getChildrenCount() == 3) {
            sb.append(String.format("%s", getChild(2).toString()));
        } else {
            sb.append(String.format("step=%s,", getChild(2).toString()));
            sb.append(String.format("%s", getChild(3).toString()));
        }
        sb.append("}");
        return sb.toString();
    }
}
