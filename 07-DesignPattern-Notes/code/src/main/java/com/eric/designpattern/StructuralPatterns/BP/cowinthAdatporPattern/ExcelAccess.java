package com.eric.designpattern.StructuralPatterns.BP.cowinthAdatporPattern;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class ExcelAccess implements DataAccess {
    private ExcelDataAccess excelDataAccess;

    public ExcelAccess(ExcelDataAccess excelDataAccess) {
        this.excelDataAccess = excelDataAccess;
    }

    public void access() {
        excelDataAccess.dataAccess();
    }

}
