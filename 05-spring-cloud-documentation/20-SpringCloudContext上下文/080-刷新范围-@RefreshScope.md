#  080-刷新范围-@RefreshScope

[TOC]

## 什么是@RefreshScope

一个被标记为@RefreshScope的Spring Bean实例在配置发生变更时可以重新进行初始化，即动态刷新配置，这是为了解决状态Bean实例只能在初始化的时候才能进行属性注入的问题。

被@RefreshScope修饰的Bean实例是懒加载的，即当它们被使用的时候才会进行初始化(方法被调用的时候)，想要在下次方法调用前强制重新初始化一个Bean实例，只需要将它的缓存失效即可。

RefreshScope是上下文中的一个Bean实例，它有一个公共方法refreshAll，该方法可以通过清除目标缓存来刷新作用域中的所有Bean实例。

RefreshScope也有一个refresh方法来按照名字刷新单个Bean。

