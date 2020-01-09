package vip.ericchen.study.spring.framework.beans.factory;

/**
 * Bean 工厂,核心接口
 *
 * @author EricChen 2020/01/07 22:48
 */
public interface BeanFactory {

    /**
     * 根据 Bean 的名称获取 Bean
     *
     * @param name bean 的名称
     * @return an instance of a bean
     * @throws Exception if the bean could not be obtained
     */
    Object getBean(String name) throws Exception;


    /**
     * 根据 Bean 的类型获取 Bean
     *
     * @param requiredType Bean 的类型 type the bean must match; could be an interface
     * @return an instance of bean
     * @throws Exception if the bean could not be obtained
     */
    <T> T getBean(Class<T> requiredType) throws Exception;


}
