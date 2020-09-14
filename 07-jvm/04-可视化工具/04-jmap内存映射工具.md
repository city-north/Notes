# 内存映射工具Jmap

## 目录

- 使用jmap导出dump

## 使用jmap导出dump

jmap (Memory map for java) 命令用于生成堆转储文件 

> 还可以查询 finalize 执行队列,Java 堆 和方法区的详细信息,如空间使用率.当前用的是哪种收集器等等

通常情况下

```
-XX: +HeapDumpOnOutOfMemoryError 参数可以在虚拟机溢出的出现之后自动生成dump
-XX: HeapDumpPath=dump.hprof //指定dump路径
```

或者通过

```
-XX: +HeapDumpOnCtrlBreak 参数可以使用 Ctrl + Break 快捷键让虚拟机生成堆转出文件
```

或者在 Linux 系统下使用 

```
kill -3 命令
```

吓唬一下虚拟机也能堆存储快照

<img src="../../assets/image-20200612234853160.png" alt="image-20200612234853160" style="zoom: 67%;" />

#### 手动导出一个dump

```
jmap -dump:format=b,file=dump.hprof 3500
-> Dumping heap to ....
Heap dumpfile created
```



#### ` jmap -histo[:live] <pid>`

通过histo选项，打印当前java堆中各个对象的数量、大小
如果添加了live，只会打印活跃的对象。

```
[root@wep-test01 ~]# ps -ef | grep java
root      5073     1  0 Jul01 ?        00:03:08 /usr/bin/java -Djava.util.logging.config.file=/root/apache-tomcat-7.0.72/conf/logging.properties -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djdk.tls.ephemeralDHKeySize=2048 -Djava.endorsed.dirs=/root/apache-tomcat-7.0.72/endorsed -classpath /root/apache-tomcat-7.0.72/bin/bootstrap.jar:/root/apache-tomcat-7.0.72/bin/tomcat-juli.jar -Dcatalina.base=/root/apache-tomcat-7.0.72 -Dcatalina.home=/root/apache-tomcat-7.0.72 -Djava.io.tmpdir=/root/apache-tomcat-7.0.72/temp org.apache.catalina.startup.Bootstrap start
root      8663 21593  0 11:08 pts/0    00:00:00 grep java
[root@wep-test01 ~]# jmap -histo 5073 >jmap.dump
[root@wep-test01 ~]# more jmap.dump 

 num     #instances         #bytes  class name
----------------------------------------------
   1:       2444981      225646208  [C
   2:       2081024       71506104  [Ljava.lang.Object;
   3:        270146       61733488  [I
   4:        322753       54584184  [B
   5:       1567260       37614240  java.lang.String
   6:       1117680       35765760  java.lang.StackTraceElement
   7:        971887       31100384  org.apache.ibatis.reflection.property.PropertyTokenizer
   8:        562589       22503560  java.util.TreeMap$Entry
   9:         99923       17262624  [S
  10:        862120       13793920  java.lang.Boolean
  11:        216028       10369344  java.util.HashMap
  12:         31784        9515032  [Ljava.lang.StackTraceElement;
  13:        170012        8117672  [Ljava.lang.String;
  14:        249881        5997144  java.util.ArrayList
  15:        373275        5972400  java.lang.Integer
  16:         60690        5717584  [Ljava.util.HashMap$Node;
  17:         64035        5635080  java.lang.reflect.Method
  18:        154234        4935488  java.util.HashMap$Node
  19:         19996        4799040  oracle.jdbc.driver.T4CVarcharAccessor
  20:         47811        3442392  org.apache.ibatis.ognl.OgnlContext
  21:        151730        3140456  [Ljava.lang.Class;
  22:        108599        2606376  java.lang.StringBuilder
  23:         30601        2448080  java.lang.reflect.Constructor
  24:          3584        2179072  com.qiku.model.bas.BasCustomer
  25:         61979        1983328  java.sql.Timestamp
  26:         27203        1958616  java.lang.reflect.Field
  27:          8433        1888992  com.qiku.model.doc.DocAsnSubSerialNo
  28:         19140        1837440  sun.util.calendar.Gregorian$Date

```

只打印活跃对象

```
[root@wep-test01 ~]# jmap -histo:live 5073 |more

 num     #instances         #bytes  class name
----------------------------------------------
   1:        249323       28354272  [C
   2:          7775        8678968  [B
   3:        246164        5907936  java.lang.String
   4:         63489        3610336  [Ljava.lang.Object;
   5:         36345        3198360  java.lang.reflect.Method
   6:         38337        2125152  [I
   7:          8786        2108640  oracle.jdbc.driver.T4CVarcharAccessor
   8:         57901        1852832  java.util.HashMap$Node
   9:         70561        1693464  java.util.ArrayList
  10:         11920        1371256  java.lang.Class
  11:         40859        1307488  java.util.concurrent.ConcurrentHashMap$Node
  12:         10695        1196464  [Ljava.util.HashMap$Node;
  13:         33461        1070752  java.lang.ref.WeakReference
  14:         12011         768704  java.net.URL
  15:         13138         630624  java.util.HashMap
  16:         14942         597680  java.lang.ref.SoftReference
  17:         14758         590320  java.util.LinkedHashMap$Entry
  18:          9978         558768  java.util.LinkedHashMap
```



```
 jmap -dump:[live,]format=b,file=<filename> <pid>
```

通过-dump选项，把java堆中的对象dump到本地文件，然后使用MAT进行分析。
如果添加了live，只会dump活跃的对象。通过-dump选项，把java堆中的对象dump到本地文件，然后使用MAT进行分析。
如果添加了live，只会dump活跃的对象。

```
[root@wep-test01 ~]# jmap -dump:format=b,file=jmap.dump1 5073
Dumping heap to /root/jmap.dump1 ...
Heap dump file created
[root@wep-test01 ~]# more jmap.dump1
ig;/io/Reader;)V is abstract0 Method oracle/jdbc/internal/OraclePreparedStatement.setTimestamp(ILjava/sql/Timestamp;)V is abstractetaDataalkerons/collection/AbstractCollectionDecorator/validation/constraints/NotNull$ListolvableTypeProvider;e; 
 'DATE' as type_name, rSize 
ƀZLEXCOUNT 
춽춾춿췀�췁췂췃췄췅췆췇췈췉췊췋���췌췍췎췏췐췑췒췓췔췕췖췗췘췙췚췛췜�췝췞췟췠췡췢췣췤췥췦췧���취췩췪췫췬췭췮췯췰췱췲췳췴췵췶췷췸�췹췺췻췼췽췾췿츀츁츂츃� 
@STATE_BLOCKEDl/bind/v2/runtime/reflect/Accessor;)Lcom/sun/xml/bind/v2/runtime/reflect/PrimitiveArrayListerBoolean$BooleanArrayPack;/alibaba/druid/sql/dialect/mysql/ast/clause/MySqlLeaveStatementva/lang/Object;>Ljava/lang/Object;Ljava/util/Iterator<TT;>;alizeConfig; 

```

生成Heap Dump文件的方法：

JMAP(Java Memory Map)

方法一：让运行中的JVM生成Dump文件
 /usr/java/jdk/bin/jmap -F -dump:format=b,file=/path/to/heap/dump/heap.bin PID

方法二：让JVM在遇到OOM(OutOfMemoryError)时生成Dump文件
 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/path/to/heap/dump
 用MAT分析Dump文件
 MAT(Memory Analyzer)

### 通过-heap选项，打印java堆的配置情况和使用情况，还有使用的GC算法。

```
jmap -heap <pid>
```

```
[root@node1 ~]# jmap -heap 66102
Attaching to process ID 66102, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.171-b11

using thread-local object allocation.
Parallel GC with 8 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 1073741824 (1024.0MB)
   NewSize                  = 178782208 (170.5MB)
   MaxNewSize               = 357564416 (341.0MB)
   OldSize                  = 358088704 (341.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 207618048 (198.0MB)
   used     = 185767680 (177.161865234375MB)
   free     = 21850368 (20.838134765625MB)
   89.47568951231061% used
From Space:
   capacity = 6815744 (6.5MB)
   used     = 3847024 (3.6688079833984375MB)
   free     = 2968720 (2.8311920166015625MB)
   56.44319974459135% used
To Space:
   capacity = 6815744 (6.5MB)
   used     = 0 (0.0MB)
   free     = 6815744 (6.5MB)
   0.0% used
PS Old Generation
   capacity = 587202560 (560.0MB)
   used     = 133345768 (127.1684341430664MB)
   free     = 453856792 (432.8315658569336MB)
   22.708648954119003% used

38087 interned Strings occupying 4891976 bytes.
```

```
 jmap -finalizerinfo <pid>
```



#### 通过-finalizerinfo选项，打印那些正在等待执行finalize方法的对象。

```
[root@test01 ~]# jmap -finalizerinfo 549
Attaching to process ID 549, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 24.79-b02
Number of objects pending for finalization: 0
```

```
5. jmap -permstat <pid>
```

通过-permstat选项，打印java堆永久代的信息，包括class loader相关的信息,和interned Strings的信息。