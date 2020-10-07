首先我们来了解一下ConversionService类所提供的作用。
1．ConversionService的设置　
之前我们提到过使用自定义类型转换器从String转换为Date的方式，那么，在Spring中还提供了另一种转换方式：使用Converter。同样，我们使用一个简单的示例来了解下Converter的使用方式。
1．定义转换器。
public class String2DateConverter implements Converter<String, Date> {  

    @Override  
    public Date convert(String arg0) {  
        try {  
            return DateUtils.parseDate(arg0,  
                    new String[] { "yyyy-MM-dd HH:mm:ss" });  
        } catch (ParseException e) {  
            return null;  
        }  
    }  

}
2．注册。
<bean id="conversionService"  
    class="org.Springframework.context.support.ConversionServiceFactoryBean">  
    <property name="converters">  
        <list>  
            <bean class="String2DateConverter" />  
        </list>  
    </property>  
</bean>
3．测试。
这样便可以使用Converter为我们提供的功能了，下面我们通过一个简便的方法来对此直接测试。
public void testStringToPhoneNumberConvert() {   
    DefaultConversionService conversionService = new DefaultConversionService();   
    conversionService.addConverter(new StringToPhoneNumberConverter());

());   

    String phoneNumberStr = "010-12345678";   
    PhoneNumberModel phoneNumber = conversionService.convert(phoneNumberStr,   
PhoneNumber Model.class);   

    Assert.assertEquals("010", phoneNumber.getAreaCode());   
}
通过以上的功能我们看到了Converter以及ConversionService提供的便利功能，其中的配置就是在当前函数中被初始化的。
2．冻结配置
冻结所有的bean定义，说明注册的bean定义将不被修改或进行任何进一步的处理。
public void freezeConfiguration() {
         this.configurationFrozen = true;
         synchronized (this.beanDefinitionMap) {
             this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.bean DefinitionNames);
         }
}
3．初始化非延迟加载
ApplicationContext实现的默认行为就是在启动时将所有单例bean提前进行实例化。提前实例化意味着作为初始化过程的一部分，ApplicationContext实例会创建并配置所有的单例bean。通常情况下这是一件好事，因为这样在配置中的任何错误就会即刻被发现（否则的话可能要花几个小时甚至几天）。而这个实例化的过程就是在finishBeanFactoryInitialization中完成的。
public void preInstantiateSingletons() throws BeansException {
         if (this.logger.isInfoEnabled()) {
             this.logger.info("Pre-instantiating singletons in " + this);
         }
         List<String> beanNames;
         synchronized (this.beanDefinitionMap) {
             // Iterate over a copy to allow for init methods which in turn register   
            //new bean definitions.
             // While this may not be part of the regular factory bootstrap, it does

//otherwise work fine.
             beanNames = new ArrayList<String>(this.beanDefinitionNames);
         }
         for (String beanName : beanNames) {
             RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
             if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
                 if (isFactoryBean(beanName)) {
                     final FactoryBean<?> factory = (FactoryBean<?>) getBean(FACTORY_ BEAN_PREFIX + beanName);
                     boolean isEagerInit;
                     if (System.getSecurityManager() != null && factory instanceof   
SmartFactoryBean) {
                         isEagerInit = AccessController.doPrivileged(new PrivilegedAction <Boolean>() {
                             public Boolean run() {
                                 return ((SmartFactoryBean<?>) factory).isEagerInit();
                             }
                         }, getAccessControlContext());
                     }
                     else {
                         isEagerInit = (factory instanceof SmartFactoryBean &&
                                 ((SmartFactoryBean<?>) factory).isEagerInit());
                     }
                     if (isEagerInit) {
                         getBean(beanName);
                     }
                 }
                 else {
                     getBean(beanName);
                 }
             }
         }
}