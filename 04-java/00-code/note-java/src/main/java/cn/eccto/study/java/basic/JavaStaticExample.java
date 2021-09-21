package cn.eccto.study.java.basic;

/**
 * Example of Static identifier in Java
 *
 * @author Jonathan 2020/01/18 15:35
 */
public class JavaStaticExample {


    public static void main(String[] args) {
        DataObject objOne = new DataObject();
        DataObject.staticVar = 10;
        objOne.nonStaticVar = 20;

        DataObject objTwo = new DataObject();

        System.out.println(objTwo.staticVar);       //10
        System.out.println(objTwo.nonStaticVar);    //null

        DataObject.staticVar = 30;  //Direct Access

        System.out.println(objOne.staticVar);       //30
        System.out.println(objTwo.staticVar);       //30
    }
}

class DataObject {
    public static Integer staticVar;
    public Integer nonStaticVar;


}