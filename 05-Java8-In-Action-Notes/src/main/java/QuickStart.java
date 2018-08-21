import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class QuickStart {

    public static void main(String[] args) {
        List<String> names1 = new ArrayList<String>();
        names1.add("Google ");
        names1.add("Runoob ");
        names1.add("Taobao ");
        names1.add("Baidu ");
        names1.add("Sina ");

        List<String> names2 = new ArrayList<String>();
        names2.add("Google ");
        names2.add("Runoob ");
        names2.add("Taobao ");
        names2.add("Baidu ");
        names2.add("Sina ");

        List<String> names3 = new ArrayList<String>();
        names2.add("Google ");
        names2.add("Runoob ");
        names2.add("Taobao ");
        names2.add("Baidu ");
        names2.add("Sina ");

        QuickStart start = new QuickStart();
        System.out.println("使用 Java 7 语法: ");

        start.sortUsingJava7(names1);
        System.out.println(names1);
        System.out.println("使用 Java 8 语法: ");

        start.sortUsingJava8(names2);
        System.out.println(names2);

    }



    /**
     * 使用java7进行排序
     */
    private void sortUsingJava7(List<String> names) {

        Collections.sort(names, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

    }

    /**
     * 使用java8进行排序
     */
    private void sortUsingJava8(List<String> names) {
        Collections.sort(names,(s1,s2) -> s1.compareTo(s2));
    }

}
