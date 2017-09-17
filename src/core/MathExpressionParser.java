/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import exceptions.InvalidFormatException;
import types.AbstractNode;
import exceptions.MisplacedTokensException;
import exceptions.TooManyDecimalPointsException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import types.AbsNode;
import types.AdditionBinaryOpNode;
import types.AssignmentNode;
import types.DivisionBinaryOpNode;
import types.FloorNode;
import types.LiteralNode;
import types.MultiplicationBinaryOpNode;
import types.ParenthesesNode;
import types.ProductNode;
import types.SubtractionBinaryOpNode;
import types.SummationNode;
import types.TrigAcosNode;
import types.TrigAsinNode;
import types.TrigAtanNode;
import types.TrigCosNode;
import types.TrigSinNode;
import types.TrigTanNode;
import types.VariableNode;
import types.VectorElementNode;
import types.CeilNode;
import types.MaxNode;
import types.MinNode;
import types.ModuloBinaryOpNode;
import types.PowerNode;
import types.RoundNode;
import types.SqrtNode;

/**
 * This core class is responsible for high-level parsing. This is where all
 * operators are registered and where the order of precedence is defined.
 * Low-level parsing is delegated to the appropriate node classes.
 *
 * @author Haitham Seada
 */
public class MathExpressionParser {

    /**
     * A list of sets of classes representing the order of precedence of the
     * expressions corresponding to these classes. Expression include operators
     * (like '+', '*', 'cos(...)' 'sum{...}' etc.) or general expressions like
     * literals, variables or vectors.
     */
    private static final List<Set<Class<? extends AbstractNode>>> levels;

    /**
     * The default registration of classes according to their default order of
     * precedence.
     */
    static {
        levels = new ArrayList<>();
        // Assignment
        Set<Class<? extends AbstractNode>> levelAssignment = new HashSet<>();
        levelAssignment.add(AssignmentNode.class);
        // Addition and subtraction: Default lowest level of precedence (last evaluated)
        Set<Class<? extends AbstractNode>> levelAddition = new HashSet<>();
        levelAddition.add(AdditionBinaryOpNode.class);
        levelAddition.add(SubtractionBinaryOpNode.class);
        // Multiplication, division and modulo
        Set<Class<? extends AbstractNode>> levelMultiplicatioin = new HashSet<>();
        levelMultiplicatioin.add(MultiplicationBinaryOpNode.class);
        levelMultiplicatioin.add(DivisionBinaryOpNode.class);
        levelMultiplicatioin.add(ModuloBinaryOpNode.class);
        // Power
        Set<Class<? extends AbstractNode>> levelPower = new HashSet<>();
        levelPower.add(PowerNode.class);
        // Summation and product
        Set<Class<? extends AbstractNode>> levelSummation = new HashSet<>();
        levelSummation.add(SummationNode.class);
        levelSummation.add(ProductNode.class);
        levelSummation.add(MinNode.class);
        levelSummation.add(MaxNode.class);
        // Trigonometric functions, floor, ceiling and round
        Set<Class<? extends AbstractNode>> levelTrigonometry = new HashSet<>();
        levelTrigonometry.add(TrigCosNode.class);
        levelTrigonometry.add(TrigSinNode.class);
        levelTrigonometry.add(TrigTanNode.class);
        levelTrigonometry.add(TrigAcosNode.class);
        levelTrigonometry.add(TrigAsinNode.class);
        levelTrigonometry.add(TrigAtanNode.class);
        levelTrigonometry.add(FloorNode.class);
        levelTrigonometry.add(CeilNode.class);
        levelTrigonometry.add(RoundNode.class);
        levelTrigonometry.add(SqrtNode.class);
        levelTrigonometry.add(AbsNode.class);
        // Vectors
        Set<Class<? extends AbstractNode>> levelVector = new HashSet<>();
        levelVector.add(VectorElementNode.class);
        // Variables and literals
        Set<Class<? extends AbstractNode>> levelVariable = new HashSet<>();
        levelVariable.add(VariableNode.class);
        levelVariable.add(LiteralNode.class);
        // Parentheses: Default highest level of precedence (first evaluated)
        Set<Class<? extends AbstractNode>> levelParentheses = new HashSet<>();
        levelParentheses.add(ParenthesesNode.class);
        // Add levels in order
        levels.add(levelAssignment);
        levels.add(levelAddition);
        levels.add(levelMultiplicatioin);
        levels.add(levelPower);
        levels.add(levelSummation);
        levels.add(levelTrigonometry);
        levels.add(levelVector);
        levels.add(levelVariable);
        levels.add(levelParentheses);
    }

    /**
     * Performs high-level parsing on an input string expression. High-level
     * parsing involves performing the following operations in-order: 1 - Divide
     * the input string expression into tokens 2 - Check the existence of
     * sufficient left parentheses, brackets and braces i.e. left balancing.
     * Right balancing is delegated to the ParenthesesNode class. 3 - Build the
     * parse tree representing your final expression. This is a done through a
     * recursive process over sections of the input string until the final parse
     * tree is built. 4 - Check for ignored tokens i.e. tokens which were not
     * included in the parse tree because they/others are misplaced and/or some
     * required tokens are missing.
     *
     * @param text is the string representing the whole input expression
     * @param vm is the manager at which variables and vectors are stored
     * @return The full parse tree representing the input expression, ready to
     * be evaluated.
     * @throws NoSuchMethodException If one of the type classes does not
     * implement findLastOccurence(...)
     * @throws IllegalAccessException If findLastOccurence(...) is not
     * accessible from this class.
     * @throws IllegalArgumentException If either findLastOccurence(...) is
     * called on a wrong object, or it it defined with wrong parameters.
     * @throws TooManyDecimalPointsException If more than one decimal point is
     * encountered in the same number.
     * @throws MisplacedTokensException If some of the tokens are ignored during
     * parsing. This is usually caused by missing operators/operands, extra
     * operators/operands, typos and/or encountering expressions whose
     * corresponding type classes were not added to the table of precedence.
     * @throws Throwable For any additional expression thrown by
     * findLastOccurence(...) itself.
     */
    public static AbstractNode parse(String text, VariablesManager vm) throws
            NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            TooManyDecimalPointsException,
            MisplacedTokensException, 
            InvalidFormatException,
            Throwable {
        // Split input into tokens
        List<Token> tokens = Utils.getTokens(text);
        // Introduce any necessary pre-fixations
        Utils.fix(tokens);
        // Check for closing (right) parentheses, brackets and braces, that have
        // no corresponding opening counterparts. Remember that the opposite
        // check is already included each in its releveant type class e.g.
        // SummationNode and ProductNode check if their opening (left) braces
        // have their corresponding closing counterparts, but they do not do the
        // reverse check. The same is true for ParenthesesNode with parentheses
        // and VectorElementNode with brackets.
        Utils.checkBalancing(tokens);
        // Parse tokens
        AbstractNode parseTree = buildParseTree(tokens, new ArrayList<>(tokens), null, vm);
        // See if any tokens were tokens
        List<Integer> ignoredTokensIndicesList = Utils.getIgnoredTokensIndices(tokens);
        if (ignoredTokensIndicesList.isEmpty()) {
            return parseTree;
        } else {
            throw new MisplacedTokensException(ignoredTokensIndicesList, tokens);
        }
    }

    /**
     * Looks for the expressions (operators and operand) of some specific level
     * in the precedence table. The method assumes left associativity for all
     * operators (remember they are already in the same level). That's why
     * parsing must start from right to left (the operator found first gets
     * executed last because it will be placed higher in the parse tree).
     *
     * @param levelIndex The index of the level whose type classes need to be
     * matched.
     * @param targetTokens The tokens against which the matching will be
     * performed.
     * @param allTokens All the tokens of the whole input expression. Required
     * for proper error reporting.
     * @param parentNode The parent node (in the parse tree) from which the new
     * nodes (to be recognized) will be emerging.
     * @param vm For storing variables and vectors.
     * @return An object encapsulating the new node and some meta data.
     * @throws NoSuchMethodException If one of the type classes does not
     * implement findLastOccurence(...).
     * @throws IllegalAccessException If findLastOccurence(...) is not
     * accessible from this class.
     * @throws IllegalArgumentException If either findLastOccurence(...) is
     * called on a wrong object, or it it defined with wrong parameters.
     * @throws Throwable For any additional expression thrown by
     * findLastOccurence(...) itself.
     */
    private static NodeRecognizedInfo lookFor(
            int levelIndex,
            List<Token> targetTokens,
            List<Token> allTokens,
            AbstractNode parentNode,
            VariablesManager vm)
            throws
            NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            Throwable {
        // Get the designated level
        Set<Class<? extends AbstractNode>> levelMap
                = levels.get(levelIndex);
        // Iterate over the designated level map entries
        Iterator<Class<? extends AbstractNode>> it
                = levelMap.iterator();
        List<NodeRecognizedInfo> infos = new ArrayList<>();
        while (it.hasNext()) {
            Class<? extends AbstractNode> opClass = it.next();
            // Check if the array of tokens contain any of the operators in this
            // designated level.
            Method method = opClass.getMethod("findLastOccurence", List.class, List.class, AbstractNode.class, VariablesManager.class);
            try {
                NodeRecognizedInfo info = (NodeRecognizedInfo) method.invoke(null, (Object) targetTokens, (Object) allTokens, (Object) parentNode, (Object) vm);
                if (info != null) {
                    infos.add(info);
                }
            } catch (InvocationTargetException ex) {
                throw ex.getCause();
            }
        }
        if (infos.isEmpty()) {
            // This means no operators from the current level were found
            return null;
        } else {
            // Return the last operator found, and remove all other discovered
            // same level operators as they will be re-discovered later on.
            // This is a very cricial part. It is responsible for maintaining
            // the left-to-right order of evaluation among operators of the
            // same level.
            int tokenPos = -1;
            NodeRecognizedInfo lastOpInfo = null;
            for (NodeRecognizedInfo info : infos) {
                if (info.getStartTokenIndex() > tokenPos) {
                    tokenPos = info.getStartTokenIndex();
                    lastOpInfo = info;
                }
            }
            for (NodeRecognizedInfo info : infos) {
                if (info != lastOpInfo) {
                    if (info.getNode().getParent() != null) {
                        info.getNode().getParent().removeChild(info.getNode());
                    }
                }
            }
            return lastOpInfo;
        }
    }

    /**
     * Recursively build the parse tree, by looking for specific expressions
     * (operators and operand) according to the levels of their corresponding
     * type classes in the precedence table. Type classes are matched level by
     * level. The method will try to match type classes of one level of
     * precedence only if none of the lower precedence type classes can be
     * matched.
     *
     * @param targetTokens The tokens against which the matching will be
     * performed.
     * @param allTokens All the tokens of the whole input expression. Required
     * for proper error reporting.
     * @param parentNode The parent node (in the parse tree) from which the new
     * nodes (to be recognized) will be emerging.
     * @param vm For storing variables and vectors.
     * @return The root node of the tree representing the part of the input upon
     * which the method is called.
     * @throws NoSuchMethodException If one of the type classes does not
     * implement findLastOccurence(...).
     * @throws IllegalAccessException If findLastOccurence(...) is not
     * accessible from this class.
     * @throws IllegalArgumentException If either findLastOccurence(...) is
     * called on a wrong object, or it it defined with wrong parameters.
     * @throws Throwable For any additional expression thrown by
     * findLastOccurence(...) itself.
     */
    private static AbstractNode buildParseTree(
            List<Token> targetTokens,
            List<Token> allTokens,
            AbstractNode parentNode,
            VariablesManager vm)
            throws
            NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException,
            //InstantiationException,
            Throwable {
        for (int l = 0; l < levels.size(); l++) {
            NodeRecognizedInfo info;
            info = lookFor(l, targetTokens, allTokens, parentNode, vm);
            if (info != null) {
                // Mark the recognized tokens
                String txt = "";
                for (int i = info.getStartTokenIndex();
                        i <= info.getEndTokenIndex();
                        i++) {
                    targetTokens.get(i).setRecognized(true);
                    txt += targetTokens.get(i).getString() + " ";
                }
                // Remove the mark from children tokens (Summation, Product,
                // trigonometirc operators etc., some tokens between start and 
                // end tokens are parsed later while parsing the children).
                // Asserting them now as "recognized" is not valid, because 
                // there might be some misplaced tokens among them. These 
                // misplaced tokens will only be discovered while parsing the 
                // children themselves).
                for (int i = 0; i < info.getChildrenCount(); i++) {
                    for (Token token : info.getChildTokensList(i)) {
                        token.setRecognized(false);
                    }
                }
                // Add only the recognized tokens to the node
                for (int i = info.getStartTokenIndex();
                        i <= info.getEndTokenIndex();
                        i++) {
                    if (targetTokens.get(i).isRecognized()) {
                        info.getNode().addToken(targetTokens.get(i));
                    }
                }
                // Recursively parse children
                for (int i = 0; i < info.getChildrenCount(); i++) {
                    if (info.getChildTokensList(i).isEmpty()) {
                        throw new InvalidFormatException(allTokens.get(info.getStartTokenIndex()), allTokens, "Missing operand.");
                    }
                    buildParseTree(info.getChildTokensList(i), allTokens, info.getNode(), vm);
                }
                // Return the final node representing the current operator
                return info.getNode();
            }
        }
        // Reaching this point means that there are still some unrecognized
        // tokens.
        StringBuilder sb = new StringBuilder();
        for (Token token : targetTokens) {
            sb.append(token.getString());
        }
        throw new UnsupportedOperationException("The parser is unable to recognize the following part: " + sb.toString());
    }

    /**
     * Displays all the levels in order from highest precedence to lowest
     * precedence.
     */
    public static String displayLevels() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < levels.size(); i++) {
            Set<Class<? extends AbstractNode>> level = levels.get(i);
            sb.append(String.format("Level %2d: ", i));
            Iterator<Class<? extends AbstractNode>> it = level.iterator();
            while (it.hasNext()) {
                sb.append(String.format("%10s", it.next().getSimpleName()));
                if (it.hasNext()) {
                    sb.append(" >> ");
                }
            }
            sb.append(String.format("%n"));
        }
        String result = sb.toString();
        System.out.println(result);
        return result;
    }
}
