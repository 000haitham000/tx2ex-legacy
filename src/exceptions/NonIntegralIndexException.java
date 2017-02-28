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
 * @author haitham
 */
public class NonIntegralIndexException extends EvaluationException {

    private final AbstractNode problematicNode;
    private final String specificErrorMessage;

    public NonIntegralIndexException(
            AbstractNode problematicNode,
            String message) {
        super();
        this.problematicNode = problematicNode;
        this.specificErrorMessage = message;
    }

    @Override
    public String getMessage() {
        String snippet = Utils.getSnippet(problematicNode.getToken(0), problematicNode.getAllTokens());
        int firstCharIndex = Utils.getFirstCharIndex(problematicNode.getToken(0), problematicNode.getAllTokens());
        String specErrMsg = (this.specificErrorMessage == null) ? "" : this.specificErrorMessage;
        return String.format(
                "Non-integral vector index at position %d (ignoring spaces): \'%s\'. "
                        + "Revise your index expression or increase the value "
                        + "of Variable.INDEX_ACCURACY_THRESHOLD. %s",
                firstCharIndex,
                snippet,
                specErrMsg);
    }
}
