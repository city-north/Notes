package cn.eccto.study.springframework.tutorials.converter;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

/**
 * 条件通用转换器代码实例
 *
 * @author JonathanChen 2019/11/22 23:19
 * @see GenericConverterExample 示例的另一种方式
 */
public class ConditionalGenericConverterExample {

    public static void main(String[] args) {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new NumberToBigDecimalConverter());

        BigDecimal bd = service.convert(Double.valueOf("2222.336"), BigDecimal.class);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(bd);

        //this will return the same BigDecimal instance without conversion
        bd = service.convert(new BigDecimal("898.33"), BigDecimal.class);
        System.out.println(bd);
    }

    public static class NumberToBigDecimalConverter implements ConditionalGenericConverter {

        /**
         * 条件匹配
         */
        @Override
        public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
            return sourceType.getType() != BigDecimal.class;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new ConvertiblePair(Number.class, BigDecimal.class));
        }

        @Override
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            Number number = (Number) source;
            return new BigDecimal(number.doubleValue());
        }
    }
}
