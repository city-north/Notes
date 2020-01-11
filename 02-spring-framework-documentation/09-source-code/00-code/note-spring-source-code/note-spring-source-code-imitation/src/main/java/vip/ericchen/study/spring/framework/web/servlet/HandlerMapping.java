package vip.ericchen.study.spring.framework.web.servlet;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 处理器映射器
 * <p>
 * Interface to be implemented by objects that define a mapping between requests and handler objects
 *
 * @author EricChen 2020/01/11 19:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandlerMapping {
    private Object controller;
    private Method method;
    private Pattern pattern;  //url的封装
}
