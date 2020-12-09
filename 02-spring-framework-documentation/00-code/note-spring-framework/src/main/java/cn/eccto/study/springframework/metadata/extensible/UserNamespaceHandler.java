package cn.eccto.study.springframework.metadata.extensible;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.UtilNamespaceHandler;

/**
 * <p>
 * User.xsd 版本的解析处理器 {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/12/9 21:28
 */
public class UserNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        this.registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
    }
}
