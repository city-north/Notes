package cn.eccto.study.springframework.tutorials.propertyeditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 使用 Spring xml 配置 的机制注册一个 {@link CustomNumberEditor}
 *
 * @author EricChen 2019/11/21 20:57
 */
public class XmlSpringCustomEditorExample {
    public static void main(String[] args) {
        ApplicationContext context = new
                ClassPathXmlApplicationContext("tutorials/propertyeditor/spring-config2.xml");
        MyBean bean = context.getBean(MyBean.class);
        System.out.println(bean);
    }

    public static class MyBean {
        private Double price;

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "MyBean{" + "price=" + price + '}';
        }
    }

    public static class MyCustomBeanRegistrar implements PropertyEditorRegistrar {
        @Override
        public void registerCustomEditors(PropertyEditorRegistry registry) {
            NumberFormat numberFormat = new DecimalFormat("##,###.00");
            registry.registerCustomEditor(java.lang.Double.class,
                    new CustomNumberEditor(java.lang.Double.class,
                            numberFormat, true));
        }
    }
}
