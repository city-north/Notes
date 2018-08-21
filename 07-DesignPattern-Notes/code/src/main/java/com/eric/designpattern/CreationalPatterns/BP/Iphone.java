package com.eric.designpattern.CreationalPatterns.BP;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Iphone extends Product {
    private String series;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Override
    public String toString() {
        return "Iphone{" +
                super.toString()+
                "series='" + series + '\'' +
                "} " + super.toString();
    }
}
