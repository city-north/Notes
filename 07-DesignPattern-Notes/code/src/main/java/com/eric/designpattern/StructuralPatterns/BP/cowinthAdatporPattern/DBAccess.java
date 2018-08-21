package com.eric.designpattern.StructuralPatterns.BP.cowinthAdatporPattern;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class DBAccess implements DataAccess {
    public void access() {
        System.out.println("进行数据库数据的采集");
    }
}
