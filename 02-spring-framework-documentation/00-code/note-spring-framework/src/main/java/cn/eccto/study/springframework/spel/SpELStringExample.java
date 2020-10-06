package cn.eccto.study.springframework.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/10/06 15:20
 */
public class SpELStringExample {

    public static void main(String[] args) {

        testParseConcat();
        testParseBytes();
        testParseLength();
        testParseToUpperCase();


    }

    private static void testParseToUpperCase() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        final Expression expression = expressionParser.parseExpression("new String ('hello World').toUpperCase()");
        final String value = expression.getValue(String.class);
        System.out.println(value); //HELLO WORLD
    }

    private static void testParseLength() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        final Expression expression = expressionParser.parseExpression("'Hello World'.bytes.length");
        final Integer value = expression.getValue(Integer.class);
        System.out.println(value); //11

    }

    private static void testParseBytes() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression exp2 = expressionParser.parseExpression("'Hello World'.bytes");
        final byte[] value2 = (byte[]) exp2.getValue();
        for (byte b : value2) {
            System.out.print(b + " "); //72 101 108 108 111 32 87 111 114 108 100
        }
        System.out.println();
    }

    private static void testParseConcat() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression exp = expressionParser.parseExpression("'Hello World'.concat('!')");
        final String value = (String) exp.getValue();
        System.out.println(value);
    }
}
