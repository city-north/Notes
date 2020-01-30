package cn.eccto.study.java.collections;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Example of Array in Java
 *
 * @author EricChen 2020/01/20 10:33
 */
public class ArrayExample {

    public static void main(String[] args) throws Exception {
        contains();
        streamContains();
        shallowCopy();
        deepCopy();
        removeDuplicates();
    }

    private static void contains() {
        String[] fruits = new String[]{"banana", "guava", "apple", "cheeku"};
        Arrays.asList(fruits).contains("apple"); // true
        Arrays.asList(fruits).indexOf("apple"); // 2

        Arrays.asList(fruits).contains("lion"); // false
        Arrays.asList(fruits).indexOf("lion"); // -1
    }

    /**
     * Since Java 8 , Stream also hold elements and you might want to test if stream contains elements or not
     */
    private static void streamContains() {
        String[] fruits = new String[]{"banana", "guava", "apple", "cheeku"};
        boolean apple = Arrays.asList(fruits)
                .stream()
                .anyMatch(x -> x.equalsIgnoreCase("apple")); //true
        System.out.println(apple);
        boolean result = Arrays.asList(fruits)
                .stream()
                .anyMatch(x -> x.equalsIgnoreCase("lion"));  //false
        System.out.println(result);
    }


    /**
     * Shallow Copy ,Cloning always creates shallow copy of  Array ,Any change in origin array will be
     * </p>
     * reflected in clone array as well
     */
    private static void shallowCopy() {
        System.out.println("----- shallow copy ------");
        Employee[] employees = new Employee[]{new Employee("eric", 123), new Employee("jack", 18)};
        Employee[] clone = employees.clone();
        // so if I change array "clone", array "fruits" is changed as well
        System.out.println(employees[0]);
        clone[0].setAge(11);
        System.out.println(employees[0]);
        System.out.println("----- shallow copy ------");

    }

    /**
     * If you want to create  deep copy of a array in Java, then use apache 's   SerializationUtils.clone(array);
     *
     * @see SerializationUtils
     */
    private static void deepCopy() {
        System.out.println("----- deep copy ------");
        Employee[] employees = new Employee[]{new Employee("eric", 123), new Employee("jack", 18)};
        Employee[] clone = SerializationUtils.clone(employees);
        // so if I change array "clone", array "fruits" is changed as well
        System.out.println(employees[0]);
        clone[0].setAge(11);
        System.out.println(employees[0]);
        System.out.println("----- deep copy ------");

    }


    /**
     * If there is no pre-condition to not to use collections API
     * then LinkedHashSet is the best approach for removing duplicate elements in an array
     * LinkedHashSet does two things internally:
     *
     * <ul>
     *     <li>1.remove duplicate elements</li>
     *     <li>2. maintain the order of elements added to it</li>
     * </ul>
     *
     * @see java.util.LinkedHashSet
     */
    private static void removeDuplicates() {
        //Array with duplicate elements
        Integer[] numbers = new Integer[]{1, 2, 3, 4, 5, 1, 3, 5};
        //This array has duplicate elements
        System.out.println(Arrays.toString(numbers));
        //Create set from array elements
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(Arrays.asList(numbers));
        //Get back the array without duplicates
        Integer[] numbersWithoutDuplicates = linkedHashSet.toArray(new Integer[]{});
        //Verify the array content
        System.out.println(Arrays.toString(numbersWithoutDuplicates));
    }

    static class Employee implements Serializable {
        public Employee(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        String name;
        Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}
