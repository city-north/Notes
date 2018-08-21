package com.eric.designpattern.StructuralPatterns.BP.cowinthAdatporPattern;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class ReportShowType2 extends ReportShow {

    public ReportShowType2(DataAccess dataAccess) {
        super.setDataAccess(dataAccess);
    }
    @Override
    public void showReport() {
        System.out.println("这是调用报表展示方法2前准备工作");
        super.getDataAccess().access();
        System.out.println("这是调用报表展示方法2后收尾工作");
    }
}
