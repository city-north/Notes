package vip.ericchen.study.spring.framework.beans.factory.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.spring.framework.beans.factory.support.BeanDefinitionReader;
import vip.ericchen.study.spring.framework.beans.factory.support.BeanDefinitionRegistry;
import vip.ericchen.study.spring.framework.beans.factory.support.GenericBeanDefinition;
import vip.ericchen.study.spring.framework.stereotype.Controller;
import vip.ericchen.study.spring.framework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * 一个标准的 BeanDefinitionReader,由于Spring 中有各种 reader,这里只参考 classpath 下扫描注解的用法:覆盖的逻辑包括
 * org.springframework.context.annotation.ClassPathBeanDefinitionScanner
 * org.springframework.context.annotation.AnnotatedBeanDefinitionReader
 *
 * @author EricChen 2020/01/08 22:42
 * @see vip.ericchen.study.spring.framework.stereotype.Repository
 * @see vip.ericchen.study.spring.framework.stereotype.Service
 * @see vip.ericchen.study.spring.framework.stereotype.Controller
 */
public class GenericBeanDefinitionReader implements BeanDefinitionReader {

    private static Logger logger = LoggerFactory.getLogger(GenericBeanDefinitionReader.class);

    private static final String KEY_SCAN_BASE_PACKAGE = "scanBasePackage";

    private final BeanDefinitionRegistry registry;

    Properties config = new Properties();


    public GenericBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void loadBeanDefinitions(String[] locations) {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            config.load(resourceAsStream);
        } catch (IOException e) {
            if (null != resourceAsStream) {
                try {
                    resourceAsStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        doScan(config.getProperty(KEY_SCAN_BASE_PACKAGE));
    }

    @Override
    public String getProperty(String key) {
        return config.getProperty(key);
    }

    private void doScan(String basePackage) {
        if (basePackage == null) {
            return;
        }
        String pkg = basePackage.replaceAll("\\.", File.separator);
        URL resource = this.getClass().getClassLoader().getResource(pkg);
        if (resource == null) {
            return;
        }
        File fileDir = new File(resource.getFile());
        //迭代文件
        File[] files = fileDir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                doScan(basePackage + "." + f.getName());
            } else {
                doLoadBeanDefinitions(basePackage + "." + f.getName().replace(".class", ""));
            }
        }
    }

    private void doLoadBeanDefinitions(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            if (aClass.isInterface()) {
                return;
            }
            if (aClass.isAnnotationPresent(Controller.class)
                    || aClass.isAnnotationPresent(Service.class)) {
                String simpleBeanName = lowerFirstCase(aClass.getSimpleName());
                this.registry.registerBeanDefinition(simpleBeanName, new GenericBeanDefinition(simpleBeanName, className));
                logger.debug("found BeanDefinition [{}] ", simpleBeanName);

                Class<?>[] interfaces = aClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    String interfaceName = anInterface.getName();
                    registry.registerBeanDefinition(interfaceName, new GenericBeanDefinition(interfaceName, className));
                    logger.debug("found BeanDefinition [{}] ", interfaceName);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String lowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}