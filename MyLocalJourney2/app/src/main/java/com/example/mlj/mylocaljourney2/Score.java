package com.example.mlj.mylocaljourney2;

/**
 * Created by alvin on 2016/7/11.
 */

import java.io.Serializable;
import java.text.NumberFormat;

public class Score implements Serializable {
    private int programming, dataStructure, algorithm;

    public Score(int programming, int dataStructure, int algorithm) {
        this.programming = programming;
        this.dataStructure = dataStructure;
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance();
        String text = "programming = " + programming +
                "\ndataStructure = " + dataStructure +
                "\nalgorithm = " + algorithm +
                "\nsum = " + nf.format(getSum()) +
                "\naverage = " + nf.format(getAverage());
        return text;
    }

    public int getSum() {
        return programming + dataStructure + algorithm;
    }

    public double getAverage() {
        return getSum() / 3.0;
    }

    public int getProgramming() {
        return programming;
    }

    public void setProgramming(int programming) {
        this.programming = programming;
    }

    public int getDataStructure() {
        return dataStructure;
    }

    public void setDataStructure(int dataStructure) {
        this.dataStructure = dataStructure;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
    }
}

