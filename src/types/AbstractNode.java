/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.NodeRecognizedInfo;
import core.Token;
import core.VariablesManager;
import exceptions.EvaluationException;
import exceptions.InvalidFormatException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author haitham
 */
public abstract class AbstractNode implements Serializable {

    private AbstractNode parent;
    private final List<AbstractNode> children;
    private final List<Token> tokensList;
    private final List<Token> allTokens;

    public AbstractNode(AbstractNode parent, List<Token> allTokens) {
        this.parent = parent;
        if(parent != null) {
            parent.addChild(this);
        }
        children = new ArrayList<>();
        tokensList = new ArrayList<>();
        this.allTokens = allTokens;
    }
    
    public boolean addToken(Token token) {
        return tokensList.add(token);
    }
    
    public Token getToken(int index) {
        return tokensList.get(index);
    }
    
    public int getTokensCount() {
        return tokensList.size();
    }

    public boolean removeChild(AbstractNode child) {
        child.parent = null;
        return children.remove(child);
    }
    
    public AbstractNode getChild(int index) {
        return children.get(index);
    }
    
    public int getChildrenCount() {
        return children.size();
    }

    public boolean addChild(AbstractNode newChild) {
        return (children.add(newChild));
    }

    public AbstractNode getParent() {
        return parent;
    }

    public AbstractNode setParent(AbstractNode parent) {
        AbstractNode previousParent = parent;
        this.parent = parent;
        return previousParent;
    }

    @Override
    public abstract String toString();
    
    public abstract double evaluate() throws EvaluationException;
    
//    public static NodeRecognizedInfo findLastOccurence(
//            List<Token> targetTokens,
//            List<Token> allTtokens,
//            AbstractNode parent,
//            VariablesManager vm) throws InvalidFormatException {
//        throw new UnsupportedOperationException("The designated expression "
//                + "class must have its own implementation "
//                + "of the method findLastOccurence(...)");
//    }

    public List<Token> getAllTokens() {
        return allTokens;
    }
}
