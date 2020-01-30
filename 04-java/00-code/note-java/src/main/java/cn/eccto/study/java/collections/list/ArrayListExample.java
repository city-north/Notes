package cn.eccto.study.java.collections.list;

import cn.eccto.study.java.utils.MyThreadPoolExecutor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Example of ArrayList
 *
 * @author EricChen 2020/01/20 15:45
 * @see java.util.ArrayList
 */
public class ArrayListExample {

    public static void main(String[] args) {
//        setMethod();
//        replaceElementWhileIterating();
//        addOnlySelectedItemToArrayList();
//        removeDuplicateElements();
//        convertArrayList2Array();
//        fastFailArrayList();
//        fastFailVector();
    }

    /**
     * Vector is thread safe because all its methods are synchronized
     */
    private static void fastFailVector() {
        Vector<String> vector = new Vector<>();
        MyThreadPoolExecutor.execute(() -> {
            vector.add("to");
            vector.forEach(System.out::println);
            vector.add("do");
            vector.add("in");
            vector.add("java");
        });
    }

    /**
     * ArrayList is not thread safe , it is fast-fail
     */
    private static void fastFailArrayList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("how");
        List<String> synchronizedList = Collections.synchronizedList(arrayList);
        MyThreadPoolExecutor.execute(() -> {
            synchronizedList.add("to");
            synchronizedList.forEach(System.out::println);
            synchronizedList.add("do");
            synchronizedList.add("in");
            synchronizedList.add("java");

        });

    }


    /**
     * Convert ArrayList to array, using both toArray() and java8 stream api
     *
     * @see #convertUsingToArray using toArray();
     * @see #convertUsingJava8StreamApi  using java8 api
     */
    private static void convertArrayList2Array() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("how");
        arrayList.add("to");
        arrayList.add("do");
        arrayList.add("in");
        arrayList.add("java");
        convertUsingJava8StreamApi(arrayList);
        convertUsingToArray(arrayList);

    }


    private static void convertUsingToArray(ArrayList<String> arrayList) {
        String[] strArray = arrayList.toArray(new String[arrayList.size()]);
        System.out.println(Arrays.toString(strArray));
    }

    private static void convertUsingJava8StreamApi(ArrayList<String> arrayList) {
        String[] strings = arrayList.stream().toArray(String[]::new);
        System.out.println(Arrays.toString(strings));

    }

    /**
     * remove the duplicate elements in ArrayList
     */
    private static void removeDuplicateElements() {

        // ArrayList with duplicate elements
        ArrayList<Integer> numbersList = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 3, 3, 4, 5, 6, 6, 6, 7, 8));
        useJava8StreamApi(numbersList);
        useLinkedListHashSet(numbersList);
    }

    /**
     * Using LinkedHashSet to remove the duplicate elements in ArrayList
     *
     * @see LinkedHashSet
     */
    private static void useLinkedListHashSet(ArrayList<Integer> numbersList) {
        System.out.println("-- useLinkedListHashSet ---");
        System.out.println(numbersList);
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet(numbersList);
        ArrayList<Integer> listWithoutDuplicates = new ArrayList<>(linkedHashSet);
        System.out.println(listWithoutDuplicates);
        System.out.println("-- useLinkedListHashSet ---");
    }

    /**
     * Using Java 8 Stream api to remove the duplicate elements in ArrayList
     */
    private static void useJava8StreamApi(ArrayList<Integer> numbersList) {
        System.out.println("-- useJava8StreamApi ---");
        System.out.println(numbersList);
        List<Integer> listWithoutDuplicates = numbersList.stream().distinct().collect(Collectors.toList());
        System.out.println(listWithoutDuplicates);
        System.out.println("-- useJava8StreamApi ---");
    }

    /**
     * 拷贝 List 1 指定的元素到List 2
     * This method use java8 stream API, We create a Stream list from first list . add filter to get the desired elements only, and then we
     * collect the filtered items to another list
     */
    private static void addOnlySelectedItemToArrayList() {
        //List 1
        List<String> namesList = Arrays.asList("alex", "brian", "charles");

        //List 2
        ArrayList<String> otherList = new ArrayList<>();

        namesList.stream()
                .filter(name -> name.contains("alex"))
                .forEachOrdered(otherList::add);

        System.out.println(otherList);
    }

    /**
     * Don't use iterator if you plan to modify the arrayList during iteration
     * use standard for loop , to keep track of index position to check the current element ,then use the index to set the new element.
     */
    private static void replaceElementWhileIterating() {
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList("alex", "brian", "charles"));
        System.out.println(namesList);
        Iterator<String> iterator = namesList.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            String next = iterator.next();
            if ("brian".equalsIgnoreCase(next)) {
                namesList.set(index, "EricChen");
            }
            index++;
        }
        System.out.println(namesList);


        //Replace item while iterating
        for (int i = 0; i < namesList.size(); i++) {
            if (namesList.get(i).equalsIgnoreCase("brian")) {
                namesList.set(i, "Lokesh");
            }
        }
        System.out.println(namesList);
    }

    /**
     * This method replaces a specified element E at the specified position in this List .
     * As the method replace the element,the list size does not change
     */
    private static void setMethod() {
        ArrayList<String> namesList = new ArrayList<>(Arrays.asList("alex", "brian", "charles"));
        System.out.println(namesList);  //list size is 3
        //Add element at 0 index
        namesList.set(0, "Lokesh");
        System.out.println(namesList);  //list size is 3

    }
}
