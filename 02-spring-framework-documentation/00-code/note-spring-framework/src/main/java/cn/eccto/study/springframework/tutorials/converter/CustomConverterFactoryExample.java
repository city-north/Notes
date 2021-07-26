package cn.eccto.study.springframework.tutorials.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.DefaultConversionService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 本例是 自定义 一个{@link ConverterFactory} 实例,实例用于产生一个自定义的 {@link IdToEntityConverter}
 *
 * @author JonathanChen 2019/11/24 12:05
 * @see CustomConverterFactoryExample.IdToEntityConverterFactory  the Custom Converter Factory
 * @see CustomConverterFactoryExample.IdToEntityConverter the Custom Converter
 */
@Configuration
public class CustomConverterFactoryExample {

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverterFactory(new IdToEntityConverterFactory());
        return service;
    }

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new
                AnnotationConfigApplicationContext(CustomConverterFactoryExample.class);

        MyBean myBean = context.getBean(MyBean.class);
        Entity entity = myBean.getEntity("1000", Order.class);
        System.out.println(entity);

        entity = myBean.getEntity("431", Customer.class);
        System.out.println(entity);
    }


    public static class MyBean {
        @Autowired
        private ConversionService service;

        public Entity getEntity(String entityId, Class<? extends Entity> type) {
            return service.convert(entityId, type);

        }
    }

    /**
     * ID 转换成 Entity 工厂
     */
    private static class IdToEntityConverterFactory implements ConverterFactory<String, Entity> {

        @Override
        public <T extends Entity> Converter<String, T> getConverter(Class<T> targetType) {
            return new IdToEntityConverter<>(targetType);
        }
    }

    /**
     * ID 转换成 Entity 转换器
     *
     * @param <T> 类型
     */
    private static class IdToEntityConverter<T extends Entity> implements Converter<String, T> {

        private Class<T> targetType;

        public IdToEntityConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String id) {
            return EntityService.getEntity(id, targetType);
        }
    }

    /**
     * 根据传入类型获取指定 ID 的 Entity 子类
     */
    private static class EntityService {

        public static <T extends Entity> T getEntity(String id, Class<T> targetType) {
            if (targetType == Item.class) {
                return (T) findItemById(id);
            } else if (targetType == Order.class) {
                return (T) findOrderById(id);
            } else if (targetType == Customer.class) {
                return (T) findCustomerById(id);
            }
            return null;
        }

        private static Customer findCustomerById(String id) {
            return new Customer(id, "Mike");
        }

        private static Order findOrderById(String id) {
            return new Order(id, 400, new Date());

        }

        private static Item findItemById(String id) {
            return new Item(id, "some tiem", new BigDecimal(100));
        }
    }

    private abstract static class Entity {
        private final String id;

        public Entity(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private static class Order extends Entity {
        private final int quantity;
        private final Date date;

        private Order(String id, int quantity, Date date) {
            super(id);
            this.quantity = quantity;
            this.date = date;
        }

        public int getQuantity() {
            return quantity;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "quantity=" + quantity +
                    ", date=" + date +
                    '}';
        }
    }

    private static class Item extends Entity {
        private final String itemName;
        private final BigDecimal cost;

        public Item(String id, String itemName, BigDecimal cost) {
            super(id);
            this.itemName = itemName;
            this.cost = cost;
        }

        public String getItemName() {
            return itemName;
        }

        public BigDecimal getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "itemName='" + itemName + '\'' +
                    ", cost=" + cost +
                    '}';
        }
    }

    private static class Customer extends Entity {
        private final String name;

        private Customer(String id, String name) {
            super(id);
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
