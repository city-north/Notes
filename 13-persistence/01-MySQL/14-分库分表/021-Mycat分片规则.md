# Mycat分片规则

- [为什么要分片](#为什么要分片)
- [分片算法有哪些](#分片算法有哪些)

## 为什么要分片



## 分片算法有哪些

- [枚举法](#枚举法)
- [固定分片hash算法](#固定分片hash算法)
- 范围约定法

- 求模法
- 日期列分区法
- 通配符取模法
- ASCII码求模通配
- 编程指定
- 截取数字哈希解析
- [一致性hash](#一致性hash)

## 枚举法

根据列的值匹配枚举,匹配到的枚举到固定的表

```xml
<tableRule name="sharding-by-intfile"> 
	<rule>
    <!-- 分片的字段 -->
		<columns>user_id</columns>
    <!-- 指定分片的算法 -->
		<algorithm>enum-map</algorithm> </rule>
</tableRule>

<function name="enum-map" class="io.mycat.route.function.PartitionByFileMap"> 
   <!-- 配置文件 -->
  <property name="mapFile">partition-hash-int.txt</property>
    <!-- type默认的值为0 ,0 表示Integer  非0表示String -->
  <property name="type">0</property>
  <property name="defaultNode">0</property>
</function>
```

partition-hash-int.txt 的配置如下

```
10000=0
10010=1
DEFAULT_NODE=1
//defaultNode 小于0 表示 不设置默认节点,大于等于0表示设置默认节点
```

默认节点的作用为: 在枚举分片时,如果碰上了不识别的枚举值, 就让他路由到默认的节点

如果不配置默认的节点(defaultNode值小于0, 表示不配置默认节点)则遇到不识别的枚举值时就会报错

## 固定分片hash算法

可在配置文件中配置固定分片的Hash算法,使用的规则如下:

```xml
<tableRule name="sharding-by-fixed-binary"> 
	<rule>
    <!-- 标识将要分片的表字段 -->
		<columns>user_id</columns>
    <!-- 指定分片的算法 -->
		<algorithm>fixed-binary</algorithm> </rule>
</tableRule>
<function name="fixed-binary" class="io.mycat.route.function.PartitionByLong"> 
   <!-- 配置文件 -->
  <property name="partitionCount">2,1</property>
    <!-- 分片范围列表,分区长度最大支持1024个槽 -->
  <property name="partitionLength">256,512</property>
</function>
```

本类规则类似于十进制的求模运算,区别在于它是二进制操作,是取分区表字段(ID) 的二进制低10位,做法是将ID二进制与上11111 11111. 这样就只去十位了

```
010101010101001010
&
1111111111
= 低10位
```



## 一致性hash

 [041-一致性哈希.md](../../02-Redis/04-Redis集群/041-一致性哈希.md) 

一致性hash算法有效地解决了分布式数据扩容问题,前面的规则都存在数据扩容难题,需要使用双写方案才能平滑迁移