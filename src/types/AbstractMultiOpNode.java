/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types;

import core.Token;
import java.util.List;

/**
 *
 * @author haitham
 */
public abstract class AbstractMultiOpNode extends AbstractNode {

    public AbstractMultiOpNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    public AbstractMultiOpNode(
            AbstractNode parent,
            List<Token> allTokens,
            List<AbstractNode> ops) {
        super(parent, allTokens);
        for (AbstractNode op : ops) {
            addChild(op);
        }
    }

    /**
     *
     * @param parent
     * @param ops
     */
    public AbstractMultiOpNode(
            AbstractNode parent,
            List<Token> allTokens,
            AbstractNode... ops) {
        super(parent, allTokens);
        for (AbstractNode op : ops) {
            addChild(op);
        }
    }
}
