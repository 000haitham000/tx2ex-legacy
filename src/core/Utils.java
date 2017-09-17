/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import exceptions.InvalidFormatException;
import exceptions.TooManyDecimalPointsException;
import java.util.ArrayList;
import java.util.List;
import types.AbstractNode;

/**
 *
 * @author haitham
 */
public class Utils {

    public static List<Token> getTokens(String exp) throws TooManyDecimalPointsException {
        // Trim left and right white spaces
        exp = exp.trim();
        // Keep the original expression intact for error reporting
        String originalTrimmedExpressions = exp;
        // Keep track of the index of the chatracter being processed
        int charIndex = 0;
        // Divide into tokens
        List<String> listOfStrings = new ArrayList<>();
        while (exp.length() > 0) {
            boolean decimalPointEncountered = false;
            // Skip white spaces
            while (Character.isWhitespace(exp.charAt(0))) {
                exp = exp.substring(1);
                charIndex++;
            }
            StringBuilder token = new StringBuilder().append(exp.charAt(0));
            exp = exp.substring(1);
            charIndex++;
            if (token.charAt(0) == '.') {
                decimalPointEncountered = true;
            }
            while (true) {
                //boolean sameFamily = areSameFamily(tokenString.charAt(tokenString.length() - 1), noSpacesExp.charAt(0));
                if (exp.length() != 0
                        && (Character.isDigit(token.charAt(0)) || token.charAt(0) == '.')
                        && (Character.isDigit(exp.charAt(0)) || exp.charAt(0) == '.')) {
                    // This section is for numbers (both integers and reals)
                    if (exp.charAt(0) == '.') {
                        if (decimalPointEncountered) {
                            // Throw an exception
                            throw new TooManyDecimalPointsException(originalTrimmedExpressions, charIndex);
                        } else {
                            decimalPointEncountered = true;
                        }
                    }
                    token.append(exp.charAt(0));
                    exp = exp.substring(1);
                    charIndex++;
                } else if (exp.length() != 0
                        && Character.isJavaIdentifierStart(token.charAt(0))
                        && Character.isJavaIdentifierPart(exp.charAt(0))) {
                    // This section is for identifiers.
                    token.append(exp.charAt(0));
                    exp = exp.substring(1);
                    charIndex++;
                } else {
                    listOfStrings.add(token.toString());
                    break;
                }
            }
        }
        // Convert the list of strings to a list of tokenString objects
        List<Token> tokensList = new ArrayList<>(listOfStrings.size());
        for (String tokenString : listOfStrings) {
            if (Character.isJavaIdentifierStart(tokenString.charAt(0))) {
                tokensList.add(new Token(tokenString, Token.IDENTIFIER));
            } else if (Character.isDigit(tokenString.charAt(0)) || tokenString.charAt(0) == '.') {
                tokensList.add(new Token(tokenString, Token.NUMERAL));
            } else {
                tokensList.add(new Token(tokenString, Token.SYMBOL));
            }
        }
        // Return the final list of tokens
        return tokensList;
    }

    public static boolean surrounded(Token token, List<Token> tokens) {
        int parenthesesPairCount = 0; // ()
        int bracketsPairsCount = 0; // []
        int bracesPairsCount = 0; // {}
        for (Token t : tokens) {
            if (t == token) {
                if (parenthesesPairCount == 0
                        && bracketsPairsCount == 0
                        && bracesPairsCount == 0) {
                    return false;
                } else {
                    return true;
                }
            } else {
                switch (t.getString()) {
                    case "(":
                        parenthesesPairCount++;
                        break;
                    case ")":
                        parenthesesPairCount--;
                        break;
                    case "[":
                        bracketsPairsCount++;
                        break;
                    case "]":
                        bracketsPairsCount--;
                        break;
                    case "{":
                        bracesPairsCount++;
                        break;
                    case "}":
                        bracesPairsCount--;
                        break;
                }
            }
        }
        // The code should not reach this point because this means that the
        // object 'token' does not even exist among the objects of the list
        // 'tokens'.
        StringBuilder sb = new StringBuilder(tokens.size());
        for (Token t : tokens) {
            sb.append(t.getString());
        }
        throw new IllegalArgumentException(String.format(
                "'%s' is not a token in: %s",
                token,
                sb.toString()));
    }

    public static List<Integer> getIgnoredTokensIndices(List<Token> tokens) {
        List<Integer> ignoredTokensIndices = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (!tokens.get(i).isRecognized()) {
                ignoredTokensIndices.add(i);
            }
        }
        return ignoredTokensIndices;
    }

    public static int getTokenIndex(Token token, List<Token> allTokens) {
        for (int i = 0; i < allTokens.size(); i++) {
            if (token == allTokens.get(i)) {
                return i;
            }
        }
        // If token is not found
        return -1;
    }

    public static String removeWhiteSpaces(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * THE FOLLOWING COMMENTED METHOD REPRESENTS A VERY NICE PEICE OF CODE THAT
     * ALLOWS FOR FINDING THE INDEX OF THE STARTING TOKEN OF SOME NODE WITHOUT
     * STORING THE TOKEN INSIDE THE NODE ITSELF WHILE PARSING. UNFORTUNATELY,
     * THIS METHOD ONLY WORKS IF THE TEXT REPRESENTING EACH CHILD IS NEVER
     * REPEATING WITHIN THE EXPRESSION WHICH IS NOT GUARANTEED. CONSEQUENTLY,
     * THIS METHOD IS REMOVED AND THE TOKENS CORRESPONDING TO EACH NODE ARE NOW
     * STORED INSIDE THE NODE OBJECT WHILE PARSING.
     */
//    public static int getStartTokenIndex(AbstractNode node) throws TooManyDecimalPointsException {
//        AbstractNode currentNode = node;
//        int globalIndex = 0;
//        while (currentNode.getParent() != null) {
//            // Get the parent
//            AbstractNode parent = currentNode.getParent();
//            // Get all left siblings
//            List<AbstractNode> leftSiblings = new ArrayList<>();
//            for (int i = 0; i < parent.getChildrenCount(); i++) {
//                if (parent.getChild(i) == currentNode) {
//                    break;
//                }
//                leftSiblings.add(parent.getChild(i));
//            }
//            // match the text of each left siblings in order with the parent until
//            // reaching the current node. Learn the relative start index of the 
//            // current node within the overall text of the parent.
//            String parentText = removeWhiteSpaces(parent.toString());
//            int relativeIndex = 0;
//            for (AbstractNode leftSibling : leftSiblings) {
//                String siblingText = Utils.removeWhiteSpaces(leftSibling.toString());
//                relativeIndex += parentText.substring(relativeIndex).indexOf(siblingText) + siblingText.length();
//            }
//            relativeIndex += parentText.substring(relativeIndex).indexOf(removeWhiteSpaces(currentNode.toString()));
//            // Move to the next parent and repeat while adding the relative indices.
//            currentNode = currentNode.getParent();
//            // Once the root is reached the summation of relative indices becomes
//            // the final index of the text of the designated node within the text
//            // of the overall expression.
//            globalIndex += relativeIndex;
//        }
//        // Split the overall text into tokens. (remember that currentNode now
//        // represents the root of the whole expression)
//        List<Token> allTokens = getTokens(currentNode.toString());
//        // Search for the token whose first character has the same index as the
//        // one calculated above.
//        int currentTokenIndex = 0;
//        for (int i = 0; i < allTokens.size(); i++) {
//            if (currentTokenIndex == globalIndex) {
//                // Return the index of this token.
//                return i;
//            } else {
//                currentTokenIndex += allTokens.get(i).getString().length();
//            }
//        }
//        // If the code reached this point, this means that the text of the
//        // designated node is not found withini the text of the overall
//        // expression. This should never happen, so an exception is thrown.
//        throw new IllegalArgumentException("The text of the designated not "
//                + "cannot be found within the text of the overall expression.");
//    }
    public static int getFirstCharIndex(Token token, List<Token> tokenList) {
        int index = 0;
        for (Token currentToken : tokenList) {
            if (token == currentToken) {
                return index;
            } else {
                index += currentToken.getString().length();
            }
        }
        return -1;
    }

    public static final int SNIPPET_TOKENS_COUNT = 10;

    public static String getSnippet(Token problematicToken, List<Token> allTokens) {
        int startTokenIndex = Utils.getTokenIndex(problematicToken, allTokens);
        // Leading dots
        String dotsBefore;
        if (startTokenIndex == 0) {
            dotsBefore = "";
        } else {
            dotsBefore = "...";
        }
        // Displayed section of the whole expression
        StringBuilder displayedSection = new StringBuilder();
        int displayedTokensCount = 0;
        int tokenIndex = startTokenIndex;
        while (tokenIndex < allTokens.size() && displayedTokensCount < SNIPPET_TOKENS_COUNT) {
            displayedSection.append(allTokens.get(tokenIndex).getString());
            displayedTokensCount++;
            tokenIndex++;
            if (tokenIndex < allTokens.size() - 1 && tokenIndex < SNIPPET_TOKENS_COUNT - 1) {
                displayedSection.append(" ");
            }
        }
        // Trailing dots
        String dotsAfter;
        if (tokenIndex < allTokens.size()) {
            dotsAfter = "...";
        } else {
            dotsAfter = "";
        }
        String dottedSection = String.format("%s %s %s",
                dotsBefore,
                displayedSection.toString(),
                dotsAfter);
        return dottedSection;
    }

    public static String getSnippet(String exp, int charIndex) {
        // Dots before
        String dotsBefore;
        if (charIndex == 0) {
            dotsBefore = "";
        } else {
            dotsBefore = "...";
        }
        // Dots after
        StringBuilder displayedSection = new StringBuilder();
        int displayedCharCount = 0;
        int index = charIndex;
        while (index < exp.length() && displayedCharCount < 3) {
            displayedSection.append(exp.charAt(index));
            displayedCharCount++;
            index++;
            if (index < exp.length() - 1 && index < 3 - 1) {
                displayedSection.append(" ");
            }
        }
        // Trailing dots
        String dotsAfter;
        if (index < exp.length()) {
            dotsAfter = "...";
        } else {
            dotsAfter = "";
        }
        // Return the dotted snippet
        String dottedSection = String.format("%s %s %s",
                dotsBefore,
                displayedSection.toString(),
                dotsAfter);
        return dottedSection;
    }

    /**
     * Makes sure that the number of closings ),],} are not more than the number
     * of openings and that they are in proper order. For example the expression
     * ')(' is invalid although the number of openings is equivalent to the
     * number of closings. It is worth noting that by default, this method does
     * NOT check for the opposite erroneous situation when the expression has
     * more openings than closings. This check is performed elsewhere. For more
     * information about this issue and how to change it, read the internal
     * comments.
     *
     * @param allTokens All tokens of the expression
     * @throws InvalidFormatException If the number of closing is larger than
     * the number of openings or they are not in the proper order.
     */
    public static void checkBalancing(List<Token> allTokens) throws InvalidFormatException {
        int parenthesesCount = 0;
        int bracketsCount = 0;
        int bracesCount = 0;
        for (int i = 0; i < allTokens.size(); i++) {
            String tokenString = allTokens.get(i).getString();
            switch (tokenString) {
                case "(":
                    parenthesesCount++;
                    break;
                case ")":
                    if (parenthesesCount == 0) {
                        throw new InvalidFormatException(
                                allTokens.get(i),
                                allTokens,
                                "Missing opening parenthesis '('");
                    }
                    parenthesesCount--;
                    break;
                case "[":
                    bracketsCount++;
                    break;
                case "]":
                    if (bracketsCount == 0) {
                        throw new InvalidFormatException(
                                allTokens.get(i),
                                allTokens,
                                "Missing opening bracket '['");
                    }
                    bracketsCount--;
                    break;
                case "{":
                    bracesCount++;
                    break;
                case "}":
                    if (bracesCount == 0) {
                        throw new InvalidFormatException(
                                allTokens.get(i),
                                allTokens,
                                "Missing opening brace '{'");
                    }
                    bracesCount--;
                    break;
            }
        }
        /*
        The following check is left intentionally for the corresponding classes.
        In other words, this function does NOT take care of the case when the
        expression has more openings than closings. It only takes care of the
        opposite case (having more closings than openings). This is because in
        the former case, the corresponding classes (e.g. ParenthesesNode) are 
        more able to provide detailed error messages (like the exact location of
        the opening).
        However, you can provide a generic error message by uncommenting this
        part.
         */
//        if (parenthesesCount != 0 || bracesCount != 0 || bracketsCount != 0) {
//            throw new InvalidFormatException(allTokens, "More opennings (,[,{ than closings ),],}");
//        }
    }

    /**
     * Performs any necessary fixations required before parsing. Usually, this
     * method handles special cases, like the mutual relations between
     * operators. For example, the expression '2*+3' should be equivalent to
     * '2*(3)' because the symbol '+' in this case signifies a positive sign
     * instead of an addition operator. But, because the precedence of addition
     * is lower than multiplication, '+' will go higher in the prase tree.
     * Consequently, the expression will be treated as if it is '(2*)+3' which
     * is not true (and is not even a valid expression). In such a case this
     * method removes the redundant '+' token, leaving the expression at '2*3'
     * removing any disambiguation. An even more interesting situation arises
     * with the negation operator. For example the expression '2*-3' should be
     * interpreted as '2*(-3)', however because of a similar problem to that
     * with addition it will be treated as if it is '(2*)-3', which is again
     * wrong. The correct handling of such a situation is to covert th original
     * expression '2*-3' to the form (2*(-1)*3). Notice that just adding a zero
     * in front of the negative sign is WRONG as it will result in the
     * expression '2*0-3' which is of course different from the original '2*-3'
     * expression.
     *
     * @param tokens All the tokens of the expression to be fixed.
     */
    public static void fix(List<Token> tokens) {
        boolean modified = false;
        for (int i = 0; i < tokens.size() - 1; i++) {
            String currentTokenString = tokens.get(i).getString();
            String nextTokenString = tokens.get(i + 1).getString();
            if (currentTokenString.equals("*")
                    || currentTokenString.equals("/")
                    || currentTokenString.equals("+")
                    || currentTokenString.equals("-")) {
                if (nextTokenString.equals("+")) {
                    // Remove redundant positive sign
                    tokens.remove(i + 1);
                    modified = true;
                    System.out.println("Expression altered to remove a "
                            + "redundant positive sign.");
                    break;
                } else if (nextTokenString.equals("-")) {
                    // Convert the single-operand negation operator to the
                    // bi-operand subtraction operator i.e. -x becomes (0-1)*x
                    tokens.add(i + 1, new Token("(", Token.SYMBOL));
                    tokens.add(i + 3, new Token("1", Token.NUMERAL));
                    tokens.add(i + 4, new Token(")", Token.SYMBOL));
                    tokens.add(i + 5, new Token("*", Token.SYMBOL));
                    modified = true;
                    System.out.println("Expression altered to reflect the "
                            + "effect of a negative sign following a binary "
                            + "operator.");
                    break;
                }
            }
        }
        if (modified) {
            fix(tokens);
        }
    }

    /**
     * Find if two consecutive integers exist in consecutive positions in the
     * provided list.
     * @param list the list of integers to be checked
     * @return true if two consecutive integers exist in consecutive positions
     * e.g. [1,5,6,10], and false otherwise e.g. [1,5,10].
     */
    public static boolean twoConsecutiveValuesExist(List<Integer> list) {
        for (int j = 0; j < list.size() - 1; j++) {
            if (list.get(j + 1) - list.get(j) == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes sure that the token passed as an argument is a valid identifier.
     * @param token the token to be checked
     * @return true if the token is a valid identifier, and false otherwise.
     */
    public static boolean isIdentifier(Token token) {
        return token.getString().matches("[a-zA-Z_$][a-zA-Z\\d_$]*");
    }

    /**
     * Parses the parameter string into a double array.
     * @param csv comma separated values
     * @return a double array
     */
    public static double[] getNumArrayFromCSV(String csv) {
        String[] splits = csv.split(",");
        double[] arr = new double[splits.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Double.parseDouble(splits[i].trim());
        }
        return arr;
    }
}
