# 将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition

因为从XML配置文件中读取到的bean信息是存储在GernericBeanDefinition中的，但是所有的bean后续处理都是针对于RootBeanDefinition的，所以这里需要进行一个转换，转换的同时如果父类bean不为空的话，则会一并合并父类的属性。



![image-20200929212537127](../../assets/image-20200929212537127.png)

![image-20200922192538797](../../assets/image-20200922192538797.png)