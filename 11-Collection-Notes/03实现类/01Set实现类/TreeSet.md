# TreeSet

特性：实现`SortedSet`里面的操作、有序，元素唯一

- 使用场景：用来对象元素进行排序,同样保证元素的唯一
- 基于TreeMap实现，TreeSet为基本操作（add、remove 和 contains）提供受保证的 log(n) 时间开销。
  另外，TreeSet是非同步的。 它的iterator 方法返回的迭代器是fail-fast的。
- 排序的方式
  - a.自然顺序(Comparable)
    - TreeSet类的add()方法中会把存入的对象提升为Comparable类型
    - 调用对象的compareTo()方法和集合中的对象比较
    - 根据compareTo()方法返回的结果进行存储
  - b.比较器顺序(Comparator)
    - 创建TreeSet的时候可以指定 一个Comparator
    - 如果传入了Comparator的子类对象, 那么TreeSet就会按照比较器中的顺序排序
    - add()方法内部会自动调用Comparator接口中compare()方法排序
    - 调用的对象是compare方法的第一个参数,集合中的对象是compare方法的第二个参数
  - c.两种方式的区别
    - TreeSet构造函数什么都不传, 默认按照类中Comparable的顺序(没有就报错ClassCastException)
    - TreeSet如果传入Comparator, 就优先按照Comparator