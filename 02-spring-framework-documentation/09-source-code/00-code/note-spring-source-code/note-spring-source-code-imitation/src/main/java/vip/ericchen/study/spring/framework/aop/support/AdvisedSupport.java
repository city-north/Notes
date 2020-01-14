package vip.ericchen.study.spring.framework.aop.support;

import lombok.Data;
import vip.ericchen.study.spring.framework.aop.config.AopConfig;
import vip.ericchen.study.spring.framework.aop.method.MethodBeforeAdviceInterceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description
 *
 * @author EricChen 2020/01/13 21:49
 */
@Data
public class AdvisedSupport {

    private Class<?> targetClass;
    private Object target;
    private AopConfig aopConfig;
    private Pattern pointClassPattern;

    public AdvisedSupport(AopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        try {
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse() throws Exception {
        String pointCut = aopConfig.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\)")
                .replaceAll("\\)", "\\\\)");
        String substring = pointCut.substring(0, pointCut.lastIndexOf("\\(") - 4);
        pointClassPattern = Pattern.compile("class " + substring.substring(substring.lastIndexOf(" ") + 1));
        Pattern pattern = Pattern.compile(pointCut);
        Method[] methods = targetClass.getMethods();
        Class<?> aspectClass = Class.forName(this.aopConfig.getAspectClass());
        Map<String, Method> aspectMethods = new HashMap<>();
        for (Method method : aspectClass.getMethods()) {
            aspectMethods.put(method.getName(), method);
        }
        for (Method method : methods) {
            String methodString = method.toString();
            if (methodString.contains("throws")) {
                methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
            }
            Matcher matcher = pattern.matcher(methodString);
            if (matcher.matches()) {
                List<Object> advices = new LinkedList<>();
                if (!(null == aopConfig.getAspectBefore())) {
                    Method m = aspectMethods.get(aopConfig.getAspectBefore());
                    advices.add(new MethodBeforeAdviceInterceptor(m, aspectClass.newInstance()));
                }
                if (!(null == aopConfig.getAspectAfter())) {

                }
                if (!(null == aopConfig.getAspectAfterThrowingName()) ){

                }
                if (!(null == aopConfig.getPointCut())) {

                }

            }


        }
    }

    public boolean pointCutMatch() {
        return pointClassPattern.matcher(aopConfig.getAspectClass()).matches();
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Object proxy) {


        return null;
    }
}
