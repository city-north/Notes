package vip.ericchen.study.designpatterns.structural.decorator;

/**
 * 装饰器模式实例
 *
 * @author EricChen 2020/01/04 13:46
 */
public class DecoratorExample {
    public static void main(String[] args) {
        Coffee coffee = new Coffee();

        //点一杯大杯加糖加双份奶的咖啡
        Goods myCoffee = new GrandeDecorator(new SugarDecorator(new MilkDecorator(new MilkDecorator(coffee))));
        System.out.println(myCoffee.getName());
        System.out.println("价格="+myCoffee.getPrice());

    }

}
