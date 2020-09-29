# bean的实例化

如果从缓存中得到了bean的原始状态，则需要对bean进行实例化。这里有必要强调一下，缓存中记录的只是最原始的bean状态，并不一定是我们最终想要的bean。

举个例子，假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是我们真正需要的是工厂bean中定义的 factory-method方法中返回的 bean，而 getObjectForBeanInstance 就是完成这个工作的，后续会详细讲解。

![image-20200922192538797](../../assets/image-20200922192538797.png)