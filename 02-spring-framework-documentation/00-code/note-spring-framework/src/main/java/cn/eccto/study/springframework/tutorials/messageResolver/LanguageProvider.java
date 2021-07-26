package cn.eccto.study.springframework.tutorials.messageResolver;

import java.util.List;

/**
 * 本实例展示了如何使用注解的方式校验 bean中的某个参数是否是有效参数
 * 1. 创建自定义注解 {@link MyLanguage} ,标注有这个注解的 field 会进行是否有这个语言的校验
 * 2. 创建测试实体类 {@link ClientBean} 和 {@link Book}, 并在 Book 实体类的属性上加上我们刚刚创建的注解
 * 3. 创建语言提供者 {@link LanguageProvider},主要获取所有语言
 * 4. {@link LanguageValidator } 对标有注解的属性进行校验
 *
 * @author JonathanChen 2019/11/22 22:30
 */
public interface LanguageProvider {

    List<String> getLanguages();

}
