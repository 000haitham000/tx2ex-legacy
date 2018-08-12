/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author haitham
 */
public class VariablesManager implements Serializable {

    // Vectors
    private final Map<String, double[]> vectors;
    private final Map<String, double[]> vectorsMins;
    private final Map<String, double[]> vectorsMaxs;
    // Variables
    private final Map<String, Double> variables;
    private final Map<String, Double> variablesMins;
    private final Map<String, Double> variablesMaxs;
    // Constants
    private final Map<String, Double> constants;

    public VariablesManager() {
        // Vectors
        vectors = new LinkedHashMap<>();
        vectorsMins = new LinkedHashMap<>();
        vectorsMaxs = new LinkedHashMap<>();
        // Variables
        variables = new LinkedHashMap<>();
        variablesMins = new LinkedHashMap<>();
        variablesMaxs = new LinkedHashMap<>();
        // Constants
        constants = new LinkedHashMap<>();
    }

    public double[] setVector(String label, double[] vector, double[] mins, double[] maxs) {
        vectorsMins.put(label, mins);
        vectorsMaxs.put(label, maxs);
        return vectors.put(label, vector);
    }

    public double[] setVector(String label, double[] vector) {
        return setVector(label, vector, null, null);
    }

    public double[] getVector(String label) {
        return vectors.get(label);
    }

    public double[] getVectorMins(String label) {
        return vectorsMins.get(label);
    }

    public double[] getVectorMaxs(String label) {
        return vectorsMaxs.get(label);
    }

    public void set(String label, double value) {
        set(label, value, null, null);
    }

    public void set(String label, double value, Double min, Double max) {
        variables.put(label, value);
        variablesMins.put(label, min);
        variablesMaxs.put(label, max);
    }

    public Double get(String label) {
        if (variables.containsKey(label)) {
            return variables.get(label);
        } else if (constants.containsKey(label)) {
            return constants.get(label);
        } else {
            throw new IllegalArgumentException(String.format("(%s) is neither a variable nor a constant.", label));
        }
    }

    public Double getMin(String label) {
        return variablesMins.get(label);
    }

    public Double getMax(String label) {
        return variablesMaxs.get(label);
    }

    public Double remove(String label) {
        return variables.remove(label);
    }

    /**
     * Returns an iterator over the current variable-value pairs. Notice that as
     * variables are stored in a LinkedHashMap, this iterator returns the
     * variables in the same order they were inserted.
     *
     * @return an iterator over the current variable-value pairs.
     */
    public Iterator<Map.Entry<String, Double>> variablesIterator() {
        return variables.entrySet().iterator();
    }

    /**
     * Returns an iterator over the current vector-values pairs. Notice that as
     * vectors are stored in a LinkedHashMap, this iterator returns the vectors
     * in the same order they were inserted.
     *
     * @return an iterator over the current variable-value pairs.
     */
    public Iterator<Map.Entry<String, double[]>> vectorsIterator() {
        return vectors.entrySet().iterator();
    }

    /**
     * Returns the number of variables excluding the vectors.
     *
     * @return the number of variables currently in the manager.
     */
    public int getVariablesCount() {
        return variables.size();
    }

    /**
     * Returns the number of vectors excluding the variables.
     *
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
