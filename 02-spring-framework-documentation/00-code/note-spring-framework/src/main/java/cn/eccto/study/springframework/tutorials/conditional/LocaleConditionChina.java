package cn.eccto.study.springframework.tutorials.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Locale;

/**
 * 自定义 {@link Condition} 实现, 在中国环境下使用中国 Locale
 *
 * @author JonathanChen 2019/11/15 20:49
 */
public class LocaleConditionChina implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return Locale.getDefault()
                .equals(Locale.CHINA);
    }
}
