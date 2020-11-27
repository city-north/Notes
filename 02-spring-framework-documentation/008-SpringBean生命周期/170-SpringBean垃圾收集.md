# 170-SpringBean垃圾收集

- Bean垃圾回收(GC)
  - 关闭Spring容器(应用上下文)
  - 执行GC
  - SpringBean覆盖的finalize()方法被调用