package cn.eccto.study.springframework.tutorials.aop.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


/**
 * description
 *
 * @author JonathanChen 2020/01/10 14:22
 */
@Aspect
@EnableAspectJAutoProxy
@Component("annotaionAspect")
public class AnnotaionAspect {

    private final static Logger log = LoggerFactory.getLogger(AnnotaionAspect.class);


    //配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
    //切点的集合，这个表达式所描述的是一个虚拟面（规则）
    //就是为了Annotation扫描时能够拿到注解中的内容
    @Pointcut("execution(* cn.eccto.study.springframework.tutorials.aop.service..*(..))")
    public void aspect() {
    }


    /*
     * 配置前置通知,使用在方法aspect()上注册的切入点
     * 同时接受JoinPoint切入点对象,可以没有该参数
     */
    @Before("aspect()")
    public void before(JoinPoint joinPoint) {
        log.info("before: " + joinPoint);
    }


    //配置后置通知,使用在方法aspect()上注册的切入点
    @After("aspect()")
    public void after(JoinPoint joinPoint){
        log.info("after: " + joinPoint);
    }

    //配置环绕通知,使用在方法aspect()上注册的切入点
    @Around("aspect()")
    public void around(JoinPoint joinPoint){
        long start = System.currentTimeMillis();
        try {
            ((ProceedingJoinPoint) joinPoint).proceed();
            long end = System.currentTimeMillis();
            log.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            log.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
        }
    }

    //配置后置返回通知,使用在方法aspect()上注册的切入点
    @AfterReturning("aspect()")
    public void afterReturn(JoinPoint joinPoint){
        log.info("afterReturn " + joinPoint);
    }

    //配置抛出异常后通知,使用在方法aspect()上注册的切入点
    @AfterThrowing(pointcut="aspect()", throwing="ex")
    public void afterThrow(JoinPoint joinPoint, Exception ex){
        log.info("afterThrow " + joinPoint + "\t" + ex.getMessage());
    }
}
