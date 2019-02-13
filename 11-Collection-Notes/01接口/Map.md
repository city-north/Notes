# Map	

`Map`用来描述键值对，一个Map不能包含重复的key，一个key最多对应一个value.它是一个数学函数的抽象模型，`Map接口`包含了一些基础的操作：`put`, `get`, `remove`, `containsKey`, `containsValue`, `size`, and `empty`、容量操作`putAll` and `clear`，Collection视图 ：`keySet`, `entrySet`, and `values`

Java平台包含`Map`接口的三个实现类： [`HashMap`](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html), [`TreeMap`](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html), and [`LinkedHashMap`](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html).它们的行为和实现类似于实现了`Set`接口的`HashSet`, `TreeSet`, and `LinkedHashSet`



## 例子

在JDK8中，我们可以根据`Employee	`中的`Department`进行`group by` ,`key`为`Department`，`value`是`Employee`

```
Map<Department, List<Employee>> byDept = employees.stream()
.collect(Collectors.groupingBy(Employee::getDepartment));
```

根据`Department`的分组进行相关的`Salary`的汇总。

```java
// Compute sum of salaries by department
Map<Department, Integer> totalByDept = employees.stream()
.collect(Collectors.groupingBy(Employee::getDepartment,
Collectors.summingInt(Employee::getSalary)));
```

根据得分情况对学生进行分组：

```
// Partition students into passing and failing
Map<Boolean, List<Student>> passingFailing = students.stream()
.collect(Collectors.partitioningBy(s -> s.getGrade()>= PASS_THRESHOLD)); 
```

根据人们的城市进行分组：

```
// Classify Person objects by city
Map<String, List<Person>> peopleByCity
         = personStream.collect(Collectors.groupingBy(Person::getCity));
```

我们可以使用城市和State两个属性进行分类，放入一个嵌套的Map中去

```
// Cascade Collectors 
Map<String, Map<String, List<Person>>> peopleByStateAndCity
  = personStream.collect(Collectors.groupingBy(Person::getState,
  Collectors.groupingBy(Person::getCity)))
```

## Map接口的基本操作

统计输入字符数组中，的那个元素的出现频率

```java
import java.util.*;

public class Freq {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<String, Integer>();

        // Initialize frequency table from command line
        for (String a : args) {
            Integer freq = m.get(a);
            m.put(a, (freq == null) ? 1 : freq + 1);
        }

        System.out.println(m.size() + " distinct words:");
        System.out.println(m);
    }
}
```

当输入：

```
java Freq if it is to be it is up to me to delegate
```

输出为：

```
8 distinct words:
{to=3, delegate=1, be=1, it=2, up=1, if=1, me=1, is=2}
```

如果你想按照字母表的顺序统计上述数组，可以将实现类从`HashMap`改为`TreeMap`：

```
8 distinct words:
{be=1, delegate=1, if=1, is=2, it=2, me=1, to=3, up=1}
```

同样，你可以根据字母第一次出现的的顺序来统计：可以将实现类从`HashMap`改为`LinkedHashMap`

```
8 distinct words:
{if=1, it=2, is=2, to=3, be=1, up=1, me=1, delegate=1}
```

我们可以通过`Map`实现类的有参构造方法，实现`HashMap`的复制。

```
Map<K, V> copy = new HashMap<K, V>(m);
```

## Map接口的批量操作

`clear`方法移除了Map中的所有所有键值对，`putAll`操作和`Collection`接口中的`addAll`方法类似，将一个`Map`中的值添加到另一个`Map`中去，这个方法有一个微妙的使用，加入你有一个`Map`，用来表示一个键值对类型的集合，`putAll`方法结合`Map`的构造方法，提供一个更加简洁的方式实现具有默认初始值的相关`Map`，下面有一个静态方法，实现了使用默认值初始化一个`Map。`

```
static <K, V> Map<K, V> newAttributeMap(Map<K, V>defaults, Map<K, V> overrides) {
    Map<K, V> result = new HashMap<K, V>(defaults);
    result.putAll(overrides);
    return result;
}
```

## 集合视图

集合视图方法，从是那种方式上实现了，允许`Map`可以被看做为一个`Collection`

- `keySet` — the `Set` of keys contained in the `Map`.
- `values` — The `Collection` of values contained in the `Map`. This `Collection` is not a `Set`, because multiple keys can map to the same value.
- `entrySet` — the `Set` of key-value pairs contained in the `Map`. The `Map` interface provides a small nested interface called `Map.Entry`, the type of the elements in this `Set`.

`Collection`视图提供了迭代Map的唯一手段，下面的例子阐述了标准的迭代一个Map的形式：

```
for (KeyType key : m.keySet())
    System.out.println(key);
```

沟通过一个迭代器：

```
// Filter a map based on some 
// property of its keys.
for (Iterator<Type> it = m.keySet().iterator(); it.hasNext(); )
    if (it.next().isBogus())
        it.remove();
```

The idiom for iterating over values is analogous. Following is the idiom for iterating over key-value pairs.

```
for (Map.Entry<KeyType, ValType> e : m.entrySet())
    System.out.println(e.getKey() + ": " + e.getValue());
```

遍历值也是类似的：

```
for (Map.Entry<KeyType, ValType> e : m.entrySet())
    System.out.println(e.getKey() + ": " + e.getValue());
```

起初，很多人担心这些习惯用法效率低下，因为每一次调用`Collection`视图中的操作时，都要穿件一个新的`Collection`实例，其实大可放心：每一次调用的`Collection`视图里面的方法，会返回同一个Object,这恰恰是为什么`Map`实现类都在`java.util`方法。

在`entrySet`视图上，调用`Map.Entry`'s `setValue`方法可以在迭代过程中修改指定key的value.(假设该Map可以被修改)。注意，这是唯一可以安全的在迭代过程中修改Map值的方法，但是，如果在迭代过程中，被其他线程或者别处修改了Map,那么调用这个方法的行为是无法预测的。

`Collection`view 支持元素的移除，可以通过许多方式：`remove`, `removeAll`, `retainAll`, and `clear`方法都可以。当然，`Iterator.remove` 操作也可以

`Collection`视图不支持元素的添加。因为这些操作在`keySet`和`vlaue`视图中是没有意义的，因为`Map`的`put`和`putAll`方法提供相同的功能。

## Collection View 的有趣使用

你想知道一个`Map`是否包含另一个`Map`

```
if (m1.entrySet().containsAll(m2.entrySet())) {
    ...
}
```

你想知道两个`Map`是否包含相同的`Key`

```
if (m1.keySet().equals(m2.keySet())) {
    ...
}
```

下面有一个例子：

假设你有有一个`Map`叫`attrMap`里面代表了一个属性-值的键值对，两个`Set` 代表了必须的属性和权限允许的属性（权限允许的属性包含必须属性）。下面的例子判断`attrMap`中是否包含这些约束，如果不包含，打印出错信息

```
static <K, V> boolean validate(Map<K, V> attrMap, Set<K> requiredAttrs, Set<K>permittedAttrs) {
    boolean valid = true;
    Set<K> attrs = attrMap.keySet();

    if (! attrs.containsAll(requiredAttrs)) {
        Set<K> missing = new HashSet<K>(requiredAttrs);
        missing.removeAll(attrs);
        System.out.println("Missing attributes: " + missing);
        valid = false;
    }
    if (! permittedAttrs.containsAll(attrs)) {
        Set<K> illegal = new HashSet<K>(attrs);
        illegal.removeAll(permittedAttrs);
        System.out.println("Illegal attributes: " + illegal);
        valid = false;
    }
    return valid;
}
```

如果你想知道两个Map中相同的key

```
Set<KeyType>commonKeys = new HashSet<KeyType>(m1.keySet());
commonKeys.retainAll(m2.keySet());
```

如果你想删除`m1`中与`m2`相应的键值对

```
m1.entrySet().removeAll(m2.entrySet());
```

如果你想删除`m1`中的与`m2`相同的key

```
m1.keySet().removeAll(m2.keySet());
```

## Multimaps

我们都知道，Map是键值对，是一对一的关系，那如果实现一对多的关系呢，只要将value设置为List即可：

```
import java.util.*;
import java.io.*;

public class Anagrams {
    public static void main(String[] args) {
        int minGroupSize = Integer.parseInt(args[1]);

        // Read words from file and put into a simulated multimap
        Map<String, List<String>> m = new HashMap<String, List<String>>();

        try {
            Scanner s = new Scanner(new File(args[0]));
            while (s.hasNext()) {
                String word = s.next();
                String alpha = alphabetize(word);
                List<String> l = m.get(alpha);
                if (l == null)
                    m.put(alpha, l=new ArrayList<String>());
                l.add(word);
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        // Print all permutation groups above size threshold
        for (List<String> l : m.values())
            if (l.size() >= minGroupSize)
                System.out.println(l.size() + ": " + l);
    }

    private static String alphabetize(String s) {
        char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
```

```
9: [estrin, inerts, insert, inters, niters, nitres, sinter,
     triens, trines]
8: [lapse, leaps, pales, peals, pleas, salep, sepal, spale]
8: [aspers, parses, passer, prases, repass, spares, sparse,
     spears]
10: [least, setal, slate, stale, steal, stela, taels, tales,
      teals, tesla]
8: [enters, nester, renest, rentes, resent, tenser, ternes,
     treens]
8: [arles, earls, lares, laser, lears, rales, reals, seral]
8: [earings, erasing, gainers, reagins, regains, reginas,
     searing, seringa]
8: [peris, piers, pries, prise, ripes, speir, spier, spire]
12: [apers, apres, asper, pares, parse, pears, prase, presa,
      rapes, reaps, spare, spear]
11: [alerts, alters, artels, estral, laster, ratels, salter,
      slater, staler, stelar, talers]
9: [capers, crapes, escarp, pacers, parsec, recaps, scrape,
     secpar, spacer]
9: [palest, palets, pastel, petals, plates, pleats, septal,
     staple, tepals]
9: [anestri, antsier, nastier, ratines, retains, retinas,
     retsina, stainer, stearin]
8: [ates, east, eats, etas, sate, seat, seta, teas]
8: [carets, cartes, caster, caters, crates, reacts, recast,
     traces]
```