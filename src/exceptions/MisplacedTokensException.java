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
public class MisplacedTokensException extends ParsingException {

    private final List<Token> allTokens;
    private final List<Integer> ignoredTokensIndicesList;
    
    public MisplacedTokensException(
            List<Integer> ignoredTokensIndicesList, 
            List<Token> tokens) {
        this.ignoredTokensIndicesList = ignoredTokensIndicesList;
        this.allTokens = tokens;
    }

    @Override
    public String getMessage() {
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(String.format("Misplaced tokens, usually caused by "
                + "missing operators, missing operands, extra operators, extra "
                + "operands and/or typos. Check the following tokens (spaces "
                + "not counted):%n"));
        for (int i = 0; i < allTokens.size(); i++) {
            if(ignoredTokensIndicesList.contains(i)) {
                String quotedSnippet = '\'' + allTokens.get(i).getString() + '\'';
                int startCharIndex = Utils.getFirstCharIndex(allTokens.get(i), allTokens);
                errorMessage.append(String.format("%-10s at position %d%n",
                        quotedSnippet, startCharIndex));
            }
        }
        return errorMessage.toString();
    }
}
