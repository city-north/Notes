# 线程间交换数据的 Exchanger

Exchanger 交换者用于线程间协作的工具类

Exchanger 用于进行线程间的数据交换.它提供一个同步点,在这个同步点,两个线程可以交换彼此的数据

- 如果一个线程调用了` exchange `方法,它会一直等待第二个线程也执行` exchange `方法
- 当两个线程达到同步点时,这两个线程就可以交换数据,将本线程生产的数据传递给对方

## 应用场景

- Exchanger 可以用于遗传算法

> 遗传算法需要选出两个人作为交换对象,这个时候会交换两个人的数据并使用交叉规则得出 2 个交配结果

- Exchager 可以用于校对工作

> 比如,我们需要将纸质银行流水通过人工的方式录入成电子银行流水,为了避免错误,采用 AB 岗两人进行录入,
>
> 录入到 excel 后,系统需要加载两个 excel, 并对两个Excel 数据进行校对,看看是否录入一直

```java
public class ExchangerTest {
    private static final Exchanger<String> exgr = new Exchanger<String>();

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String A = "银行流水A";// A录入银行流水数据
                    final String exchange = exgr.exchange(A);
                    System.out.println("A和B数据是否一致：" + exchange);
                } catch (InterruptedException e) {
                }
            }
        });

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String B = "银行流水B";// B录入银行流水数据
                    String A = exgr.exchange("B");
                    System.out.println("A和B数据是否一致：" + A.equals(B) + "，A录入的是：" + A + "，B录入是：" + B);
                } catch (InterruptedException e) {
                }
            }
        });

        threadPool.shutdown();

    }
}
```

## 输出

```
A和B数据是否一致：B
A和B数据是否一致：false，A录入的是：银行流水A，B录入是：银行流水B
```

