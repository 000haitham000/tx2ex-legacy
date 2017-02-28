/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

/**
 * This interface defines the threshold of integral index acceptance (see
 * details below).
 * @author Haitham Seada
 */
public interface IndexPrecisionThresholdWatcher {

    /**
     * The value of this field determines the threshold of accepting the value
     * of an index to a vector. Any index must be integer, but since expressions
     * are allowed in place of indices, The floating point evaluated value of an
     * index must be checked. If the absolute difference between the evaluated 
     * value and its corresponding truncated integer is smaller than this 
     * threshold, it is accepted, otherwise an exception is thrown.
     */
    public final static double INDEX_ACCURACY_THRESHOLD = 1e-10;
}
