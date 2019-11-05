package cn.eccto.study.springframework.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

/**
 * description
 *
 * @author EricChen 2019/11/05 10:09
 */
public class SpELExample {


    public static void main(String[] args) {
        // Turn on:
        // - auto null reference initialization
        // - auto collection growing
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);

        ExpressionParser parser = new SpelExpressionParser(config);

        Expression expression = parser.parseExpression("list[3]");

        Demo demo = new Demo();

        Object o = expression.getValue(demo);
        //如果是 null 会自动自增
        System.out.println(o);
    }
}

class Demo {
    public List<String> list;
}