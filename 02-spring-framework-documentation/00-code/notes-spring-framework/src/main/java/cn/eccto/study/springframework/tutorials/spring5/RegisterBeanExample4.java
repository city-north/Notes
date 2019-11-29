package cn.eccto.study.springframework.tutorials.spring5;

import org.springframework.context.support.GenericApplicationContext;

/**
 * description
 *
 * @author EricChen 2019/11/27 18:10
 */
public class RegisterBeanExample4 {
    //using registerBean(beanClass, beanSupplier,  customizers)
    public static void main(String[] args) {
        GenericApplicationContext gac = new GenericApplicationContext();
        gac.registerBean(LogService.class, LogService.LogServiceImpl::new, Customizers::lazy, Customizers::defaultInitMethod);
        gac.refresh();
        System.out.println("context refreshed");
        LogService ls = gac.getBean(LogService.class);
        ls.log("msg from main method");
        gac.close();
    }
}