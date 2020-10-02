package vip.ericchen.framework.ecspring;

/**
 * <p>
 * BeanFactory
 * </p>
 *
 */
public interface BeanFactory {
    Object getBean(String name) throws Exception;
}
