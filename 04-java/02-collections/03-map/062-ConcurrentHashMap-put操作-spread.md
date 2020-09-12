# put方法准备阶段-spread

![image-20200912215807512](../../../assets/image-20200912215807512.png)

- (h >>> 16) :  hashcode 无符号右移 16 位, 即高 16 位
- 使用 hashcode 异或低 16 位(不同为 1 ) 自己的高 16 位 ,这样的目的是为了降低重复的可能性

```
   static final int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;
    }
```

