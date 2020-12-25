package cn.eccto.study.springframework.convertion;

import cn.eccto.study.springframework.User;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * <p>
 * 自定义 {@link PropertyEditorRegistrar} 的实现
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/25 15:32
 * @see PropertyEditorRegistrar
 */
public class CustomizedPropertyEditorRegistrar implements PropertyEditorRegistrar {

    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        // 1. 通用类型转换
        // 2. Java Bean 属性类型转换
        registry.registerCustomEditor(User.class,"context",new StringToPropertiesPropertyEditor());
    }
}
