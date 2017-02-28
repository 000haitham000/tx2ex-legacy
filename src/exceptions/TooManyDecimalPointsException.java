/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import core.Utils;

/**
 *
 * @author haitham
 */
public class TooManyDecimalPointsException extends ParsingException {

    private final String text;
    private final int problematicCharIndex;
    
    public TooManyDecimalPointsException(
            String text, 
            int problematicCharIndex) {
        this.text = text;
        this.problematicCharIndex = problematicCharIndex;
    }

    @Override
    public String getMessage() {
        return String.format(
                "Only one decimal point is allowed within a number: (%s) "
                        + "Additional point found at position %d "
                        + "(intermediate spaces counted)",
                Utils.getSnippet(text, problematicCharIndex),
                problematicCharIndex);
    }
}
