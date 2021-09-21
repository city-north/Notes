package cn.eccto.study.java.basic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * UnSafe 机制, JVM 通常会管理内存的使用,你也可以用这个机制去自己创建一个类的实例
 * 但是这个实例不会调用构造方法,更不会调用初始化的任何方法
 *
 * @author Jonathan 2020/01/18 21:39
 */
public class UnsafeExample {
    public static void main(String[] args) throws Exception {
        Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        //This creates an instance of player class without any initialization
        Player p = (Player) unsafe.allocateInstance(Player.class);
        System.out.println(p.getAge());     //Print 0

        p.setAge(45);                       //Let's now set age 45 to un-initialized object
        System.out.println(p.getAge());     //Print 45

        System.out.println(new Player().getAge());  //This the normal way to get fully initialized object; Prints 50
    }

}
class Player{
    private int age = 12;

    public Player(){        //Even if you create this constructor private;
        //You can initialize using Unsafe.allocateInstance()
        this.age = 50;
    }
    public int getAge(){
        return this.age;
    }
    public void setAge(int age){
        this.age = age;
    }
}