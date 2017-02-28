/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author haitham
 */
public class VariablesManager {

    private final Map<String, double[]> vectors;
    private final Map<String, Double> variables;
    private final Map<String, Double> constants;

    public VariablesManager() {
        vectors = new LinkedHashMap<>();
        variables = new LinkedHashMap<>();
        constants = new LinkedHashMap<>();
    }

    public double[] setVector(String label, double[] vector) {
        return vectors.put(label, vector);
    }

    public double[] getVector(String label) {
        return vectors.get(label);
    }

    public void set(String label, double constant) {
        variables.put(label, constant);
    }

    public Double remove(String label) {
        return variables.remove(label);
    }

    public Double get(String label) {
        if(variables.containsKey(label)) {
            return variables.get(label);
        } else if(constants.containsKey(label)) {
            return constants.get(label);
        } else {
            throw new IllegalArgumentException(String.format("(%s) is neither a variable nor a constant.", label));
        }
    }
    
    /**
     * Returns an iterator over the current variable-value pairs. Notice that 
     * as variables are stored in a LinkedHashMap, this iterator returns the 
     * variables in the same order they were inserted.
     * @return an iterator over the current variable-value pairs.
     */
    public Iterator<Map.Entry<String, Double>> variablesIterator() {
        return variables.entrySet().iterator();
    }
    
    /**
     * Returns an iterator over the current vector-values pairs. Notice that 
     * as vectors are stored in a LinkedHashMap, this iterator returns the 
     * vectors in the same order they were inserted.
     * @return an iterator over the current variable-value pairs.
     */
    public Iterator<Map.Entry<String, double[]>> vectorsIterator() {
        return vectors.entrySet().iterator();
    }
    
    /**
     * Returns the number of variables excluding the vectors.
     * @return the number of variables currently in the manager.
     */
    public int getVariablesCount() {
        return variables.size();
    }
    
    /**
     * Returns the number of vectors excluding the variables.
     * @return the number of vectors currently in the manager.
     */
    public int getVectorsCount() {
        return vectors.size();
    }
    
    public void setConstant(String label, double value) {
        constants.put(label, value);
    }

    public double removeConstant(String label) {
        return constants.remove(label);
    }
}
