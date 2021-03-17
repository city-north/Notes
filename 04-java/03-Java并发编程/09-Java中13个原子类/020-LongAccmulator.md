# 020-LongAccmulator

[TOC]

## 总结

LongAdder 类是 LongAccumulator 的一个特例, LongAccumulator 比 LongAdder 更强大

## 构造函数

```java
public LongAccumulator(LongBinaryOperator accumulatorFunction,long identity) {
  this.function = accumulatorFunction;
  base = this.identity = identity;
}
```

```java
@FunctionalInterface
public interface LongBinaryOperator {

    /**
     * 根据两个参数计算并返回一个值
     */
    long applyAsLong(long left, long right);
}
```

## LongAdder 是 LongAccumulator 的一个特例

```java
LongAdder adder = new LongAdder();
LongAccumulator acc = new LongAccumulator(new LongBinaryOperator(){
  	@Override
  	public long applyAsLong(long left, long right){
    	return left + right;
    }
	},0);
```

**LongAccumulator** 相比于 **LongAdder** , 可以为累加器提供 非 0 的初始值, 后者只能提供默认的 0 值, 

另外 ,  前者还可以指定累加规则, 比如不进行累加而进行相乘, 只需要在构造 LongAccumlator 时 传入自定义的双目运算器即可, 后者则内置累加的规则

LongAdder 的 add 方法

```java
    public void add(long x) {
        Cell[] as; long b, v; int m; Cell a;
        if ((as = cells) != null || !casBase(b = base, b + x)) {
            boolean uncontended = true;
            if (as == null || (m = as.length - 1) < 0 ||
                (a = as[getProbe() & m]) == null ||
                !(uncontended = a.cas(v = a.value, v + x)))
                longAccumulate(x, null, uncontended);
        }
    }
```

LongAccumlator 的 accumulate

```java
    public void accumulate(long x) {
        Cell[] as; long b, v, r; int m; Cell a;
        if ((as = cells) != null ||
            (r = function.applyAsLong(b = base, x)) != b && !casBase(b, r)) {
            boolean uncontended = true;
            if (as == null || (m = as.length - 1) < 0 ||
                (a = as[getProbe() & m]) == null ||
                !(uncontended =
                  (r = function.applyAsLong(v = a.value, x)) == v ||
                  a.cas(v, r)))
                longAccumulate(x, function, uncontended);
        }
    }
```

前者 longAccumulate 时 传递的是 function , 而后者是 null

从下面的代码可知 ,  当 fn 为 null 时, 就  v + x 加法, 这时候就等价 LongAdder , 当 fn 不为 null 时使用传递的 fn 函数计算

```java
else if (casBase(v = base, ((fn == null)) ? v + x:
                 fn.applyAsLong(v,x))))
  			break;
	))
```

