package vip.ericchen.study.spring.framework.web.servlet;

import lombok.Data;

import java.util.Map;

/**
 * description
 *
 * @author EricChen 2020/01/11 21:44
 */
@Data
public class ModelAndView {
    private String viewName;
    private Map<String,?> model;

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

}
