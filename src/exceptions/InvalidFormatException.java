/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import core.Token;
import core.Utils;
import java.util.List;

/**
 *
 * @author Haitham
 */
public class InvalidFormatException extends ParsingException {

    private Token problematicToken;
    private final List<Token> allTokens;
    private final String specificErrorMessage;

    public InvalidFormatException(
            Token problematicToken,
            List<Token> allTokens,
            String message) {
        super();
        this.problematicToken = problematicToken;
        this.allTokens = allTokens;
        this.specificErrorMessage = message;
    }

    public InvalidFormatException(
            List<Token> allTokens,
            String message) {
        super();
        this.allTokens = allTokens;
        this.specificErrorMessage = message;
    }

    @Override
    public String getMessage() {
        if (problematicToken != null) {
            String snippet = Utils.getSnippet(problematicToken, allTokens);
            int firstCharIndex = Utils.getFirstCharIndex(problematicToken, allTokens);
            String specErrMsg = (this.specificErrorMessage == null) ? "" : this.specificErrorMessage;
            return String.format(
                    "Unable to parse the expression \'%s\' starting at position %d (ignoring spaces): \'%s\'. %s",
                    problematicToken.getString(),
                    firstCharIndex,
                    snippet,
                    specErrMsg);
        } else {
            String specErrMsg = (this.specificErrorMessage == null) ? "" : this.specificErrorMessage;
            return String.format(
                    "Unable to parse the expression : %s",
                    specErrMsg);
        }
    }
}
