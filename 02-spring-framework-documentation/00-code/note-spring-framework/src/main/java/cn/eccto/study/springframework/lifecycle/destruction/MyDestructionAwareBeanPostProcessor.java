package cn.eccto.study.springframework.lifecycle.destruction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;

/**
 * <p>
 * 自定义的{@link DestructionAwareBeanPostProcessor}
 * </p>
 *
 * @author JonathanChen 2020/11/27 15:47
 */
public class MyDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        System.out.println("调用了 -> postProcessBeforeDestruction");
    }
}
