package com.eric.designpattern.StructuralPatterns.BP.cowinthAdatporPattern;

/**
 * 与适配器模式联合使用
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Test {
    public static void main(String[] args) {
        DataAccess dataAccess = new ExcelAccess(new ExcelDataAccess());
        ReportShow reportShow1 = new ReportShowType1(dataAccess);
        reportShow1.showReport();
    }
}
