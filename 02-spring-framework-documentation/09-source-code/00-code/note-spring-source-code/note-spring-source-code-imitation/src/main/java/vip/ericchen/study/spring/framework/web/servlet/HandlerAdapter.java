package vip.ericchen.study.spring.framework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 处理器适配器
 * 处理器: 将 请求具体进行处理
 * 适配器: 将处理器与请求进行适配
 *
 * @author EricChen 2020/01/11 21:39
 */
public class HandlerAdapter {
    private Map<String, Integer> paramMapping;

    public HandlerAdapter(Map<String, Integer> paramMapping) {
        this.paramMapping = paramMapping;
    }


    /**
     * 处理
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, HandlerMapping handler) throws Exception {
        Class<?>[] paramTypes = handler.getMethod().getParameterTypes();
        Map<String, String[]> reqParameterMap = request.getParameterMap();
        Object [] paramValues = new Object[paramTypes.length];
        for (Map.Entry<String,String[]> param : reqParameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]","").replaceAll("\\s","");
            if(!this.paramMapping.containsKey(param.getKey())){continue;}
            int index = this.paramMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value,paramTypes[index]);
        }
        Object result = handler.getMethod().invoke(handler.getController(),paramValues);
        if(result == null){ return  null; }

        boolean isModelAndView = handler.getMethod().getReturnType() == ModelAndView.class;
        if(isModelAndView){
            return (ModelAndView) result;
        }else{
            return null;
        }
    }

    private Object caseStringValue(String value,Class<?> clazz){
        if(clazz == String.class){
            return value;
        }else if(clazz == Integer.class){
            return  Integer.valueOf(value);
        }else if(clazz == int.class){
            return Integer.valueOf(value).intValue();
        }else {
            return null;
        }
    }

}
