package cn.eccto.study.springframework.validation.spr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.DataBinder;

import java.util.Locale;

/**
 * Spring 自定义校验器实例
 * 1. 新建待校验的实体类{@link Order}
 * 2. 新建需要使用到校验的客户端 bean {@link ClientBean}
 * 3. 配置校验器 {@link OrderValidator}
 * 4. 配置配置类 {@link Config}
 *
 * @author JonathanChen 2019/11/04 19:29
 */
public class ClientBean {
    @Autowired
    private Order order;

    public void processOrder() {
        if (validateOrder()) {
            System.out.println("processing " + order);
        }
    }

    private boolean validateOrder() {
        DataBinder dataBinder = new DataBinder(order);
        dataBinder.addValidators(new OrderValidator());
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            ResourceBundleMessageSource messageSource =
                    new ResourceBundleMessageSource();
            //使用 messageSource
            messageSource.setBasename("messages/messages");
            messageSource.setDefaultEncoding("UTF-8");
            System.out.println(messageSource.getMessage("order.invalid", null, Locale.SIMPLIFIED_CHINESE));
            dataBinder.getBindingResult().getAllErrors().stream().
                    forEach(e -> System.out.println(messageSource
                            .getMessage(e, Locale.SIMPLIFIED_CHINESE)));
            return false;
        }
        return true;
    }
}
