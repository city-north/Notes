package vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.client;


/**
 * Created by Tom.
 */
public class Test {
    public static void main(String[] args) {
        ECMeipo gpMeipo = new ECMeipo();
        IPerson zhangsan = gpMeipo.getInstance(new Zhangsan());
        zhangsan.findLove();


//        IPerson zhaoliu = jdkMeipo.getInstance(new ZhaoLiu());
//        zhaoliu.findLove();
//        zhaoliu.buyInsure();


    }
}
