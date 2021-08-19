package com.jc.study;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.junit.Test;

/**
 * <p>
 * 使用默认方式创建Activiti
 * </p>
 *
 * @author Jonathan Chen 2021/08/17 21:58
 */
public class TestCreate {


    @Test
    public void testCreateDbTableUseDefault() {
//        需要使用avtiviti提供的工具类 ProcessEngines ,使用方法getDefaultProcessEngine
//        getDefaultProcessEngine会默认从resources下读取名字为actviti.cfg.xml的文件
//        创建processEngine时，就会创建mysql的表

//        默认方式
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment();

//        使用自定义方式
//        配置文件的名字可以自定义,bean的名字也可以自定义
/*        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("activiti.cfg.xml",
                        "processEngineConfiguration");*/

//        获取流程引擎对象
//        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
//
//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        System.out.println(processEngine);
    }


    @Test
    public void testCreateDbTableUseCustom() {
        //        使用自定义方式
//        配置文件的名字可以自定义,bean的名字也可以自定义
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.
                createProcessEngineConfigurationFromResource("activiti.cfg.xml",
                        "processEngineConfiguration");
    }
}
