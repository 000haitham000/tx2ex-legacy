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
public abstract class AbstractBinaryOpNode extends AbstractNode {

    public AbstractBinaryOpNode(AbstractNode parent, List<Token> allTokens) {
        super(parent, allTokens);
    }

    public AbstractBinaryOpNode(
            AbstractNode parent,
            List<Token> allTokens,
            AbstractNode op1,
            AbstractNode op2) {
        super(parent, allTokens);
        addChild(op1);
        addChild(op2);
    }
}
