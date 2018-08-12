/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author haitham
 */
public class Token implements Serializable {
    
    /**
     * A constant used to mark tokens representing valid identifiers.
     */
    public static final int IDENTIFIER = 0;
    /**
     * A constant used to mark tokens representing valid numbers (integer and
     * floating-point).
     */
    public static final int NUMERAL = 1;
    /**
     * A constant used to mark tokens representing special symbols.
     */
    public static final int SYMBOL = 2;
    
    private final String tokenString;
    private final int type;
    private boolean recognized;

    public Token(String tokenString, int type) {
        this.tokenString = tokenString;
        this.type = type;
    }

    /**
     * @return the string represented by the token
     */
    public String getString() {
        return tokenString;
    }

    /**
     * @return the type of the token
     */
    public int getType() {
        return type;
    }

    /**
     * @return true if the token has already been recognized during the parsing.
     */
    public boolean isRecognized() {
        return recognized;
    }

    /**
     * Mark the token as recognized/un-recognized during parsing.
     * @param recognized true if the token has been recognized (matched) during 
     * parsing.
     */
    public void setRecognized(boolean recognized) {
        this.recognized = recognized;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.tokenString);
        hash = 59 * hash + this.type;
        return hash;
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
        final Token other = (Token) obj;
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.tokenString, other.tokenString)) {
            return false;
        }
        return true;
    }
}
