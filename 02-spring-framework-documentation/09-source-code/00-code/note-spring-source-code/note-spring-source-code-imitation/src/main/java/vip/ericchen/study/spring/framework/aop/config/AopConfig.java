package vip.ericchen.study.spring.framework.aop.config;

import lombok.Data;

/**
 * description
 *
 * @author EricChen 2020/01/13 23:11
 */
@Data
public class AopConfig {

    private String pointCut;
    private String aspectClass;
    private String aspectBefore;
    private String aspectAfter;
    private String aspectAfterThrowingName;
}
