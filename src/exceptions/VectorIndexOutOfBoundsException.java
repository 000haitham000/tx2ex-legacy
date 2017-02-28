/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import core.Utils;
import types.AbstractNode;

/**
 *
 * @author Haitham
 */
public class VectorIndexOutOfBoundsException extends EvaluationException {

    private final AbstractNode problematicNode;
    private final String specificErrorMessage;
    private final int index;
    private final int largestPossibleIndex;

    public VectorIndexOutOfBoundsException(
            AbstractNode problematicNode,
            String message,
            int index,
            int largestPossibleIndex) {
        super();
        this.problematicNode = problematicNode;
        this.specificErrorMessage = message;
        this.index = index;
        this.largestPossibleIndex = largestPossibleIndex;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the largestPossibleIndex
     */
    public int getLargestPossibleIndex() {
        return largestPossibleIndex;
    }

    @Override
    public String getMessage() {
        String snippet = Utils.getSnippet(problematicNode.getToken(0), problematicNode.getAllTokens());
        int firstCharIndex = Utils.getFirstCharIndex(problematicNode.getToken(0), problematicNode.getAllTokens());
        String specErrMsg = (this.specificErrorMessage == null) ? "" : this.specificErrorMessage;
        return String.format(
                "Vector index %d at position %d (ignoring spaces) is out of bounds: \'%s\'. The maximum possible index of this vector is %d. %s",
                index,
                firstCharIndex,
                snippet,
                largestPossibleIndex,
                specErrMsg);
    }
}
