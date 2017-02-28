/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import core.Utils;
import java.util.List;
import types.AbstractNode;

/**
 *
 * @author Haitham
 */
public class IdentifierNotFoundException extends EvaluationException {

    private final AbstractNode problematicNode;
    private final String specificErrorMessage;

    public IdentifierNotFoundException(
            AbstractNode problematicNode,
            String message) {
        super();
        this.problematicNode = problematicNode;
        this.specificErrorMessage = message;
    }

    @Override
    public String getMessage() {
        String snippet = Utils.getSnippet(problematicNode.getToken(0), problematicNode.getAllTokens());
        return String.format(
                "Unable to find the identifier '%s' starting at position %d (ignoring spaces): '%s'. An identifier must be added to the variables manager before being retreived. %s",
                problematicNode.getToken(0).getString(),
                Utils.getFirstCharIndex(problematicNode.getToken(0), problematicNode.getAllTokens()),
                snippet,
                specificErrorMessage);
    }

}
