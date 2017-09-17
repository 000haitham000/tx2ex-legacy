/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import types.AbstractNode;
import java.util.List;

/**
 * An object from this class is used to encapsulate a recently recognized
 * (matched) node, along with the meta data of the matching process.
 *
 * @author Haitham Seada
 */
public class NodeRecognizedInfo {

    /**
     * Index of the first token in the sequence of tokens representing this
     * node.
     */
    private final int startTokenIndex;
    /**
     * Index of the last token in the sequence of tokens representing this node.
     */
    private final int endTokenIndex;
    /**
     * The recognized (matched) node itself.
     */
    private final AbstractNode node;
    /**
     * Each list in this list of lists contains the group of tokens representing
     * one child. So if this node should have three children, this structure
     * should contain three lists of tokens.
     */
    private final List<List<Token>> childrenTokensLists;

    public NodeRecognizedInfo(
            int startTokenIndex,
            int endTokenIndex,
            AbstractNode node,
            List<List<Token>> childrenTokensLists) {
        this.startTokenIndex = startTokenIndex;
        this.endTokenIndex = endTokenIndex;
        this.node = node;
        this.childrenTokensLists = childrenTokensLists;
    }

    /**
     * Gets the list of tokens representing child(index)
     *
     * @param index The index of the child whose tokens are being retrieved
     * @return The list of tokens representing the designated child
     */
    public List<Token> getChildTokensList(int index) {
        return childrenTokensLists.get(index);
    }

    /**
     * Gets the number of children the recognized node is expected to have.
     *
     * @return number of expected children which is equal to the number of lists
     * stored in childrenTokensLists.
     */
    public int getChildrenCount() {
        if (childrenTokensLists == null) {
            return 0;
        } else {
            return childrenTokensLists.size();
        }
    }

    /**
     * Gets the index of the first token representing the recognized node within
     * the number of tokens being parsed (need not be the index within all the
     * tokens of the whole expression).
     *
     * @return The index of the first representing token.
     */
    public int getStartTokenIndex() {
        return startTokenIndex;
    }

    /**
     * Gets the index of the last token representing the recognized node within
     * the number of tokens being parsed (need not be the index within all the
     * tokens of the whole expression).
     *
     * @return The index of the last representing token.
     */
    public int getEndTokenIndex() {
        return endTokenIndex;
    }

    /**
     * @return the recognized (matched) node
     */
    public AbstractNode getNode() {
        return node;
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
        NodeRecognizedInfo other = (NodeRecognizedInfo) obj;
        if (this.startTokenIndex != other.startTokenIndex
                || this.endTokenIndex != other.endTokenIndex) {
            return false;
        }
        if (this.childrenTokensLists != null && this.childrenTokensLists != null) {
            if (this.getChildrenCount() != other.getChildrenCount()) {
                return false;
            }
            for (int i = 0; i < this.childrenTokensLists.size(); i++) {
                for (int j = 0; j < this.childrenTokensLists.get(i).size(); j++) {
                    if (!childrenTokensLists.get(i).get(j).equals(
                            other.childrenTokensLists.get(i).get(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
