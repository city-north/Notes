package com.eric.designpattern.StructuralPatterns.BP.cowinthAdatporPattern;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public abstract class ReportShow  {
    private DataAccess dataAccess;

    protected ReportShow() {
    }

    public DataAccess getDataAccess() {
        return dataAccess;
    }

    public synchronized void setDataAccess(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public abstract void showReport();
}
