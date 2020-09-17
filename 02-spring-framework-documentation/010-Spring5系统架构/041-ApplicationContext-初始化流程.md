# ApplicationContext初始化机制

- 入口
- 获取配置路径
- 开始启动
- 创建容器
- 载入配置路径
- 分配路径处理策略
- 解析配置文件路径
- 读取配置文件内容
- 准备文档对象
- 分配解析策略
- 将配置载入内存
- 向容器注册







获得配置路径还有AnnotationConfigApplicationContext、FileSystemXmlApplicationContext、XmlWebApplicationContext等，都继承自父容器AbstractApplicationContext，主要用到了装饰者模式和策略模式，最终都调用refresh（）方法。
8.2.2 获得配置路径