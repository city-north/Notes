# Usage of class sun.misc.Unsafe

This post is next update in sequence of discussions regarding **[little known features of java](https://howtodoinjava.com/tag/java-hidden-features/)**. Please **subscribe through email** to get updated when next discussion goes live. And do not forget to express your views in comments section.

Java is a safe programming language and prevents programmer from doing a lot of stupid mistakes, most of which were based on memory management. But, if you are determined to mess with it, you have Unsafe class. This class is a sun.* API which **isn’t really part of the J2SE**, so you may not find any official documentation. Sadly,It also does not have any good code documentation too.

## **Instantiation of sun.misc.Unsafe**

If you try to create an instance of Unsafe class, you will not be allowed because of two reasons.

1) Unsafe class has private constructor.
2) It also has static getUnsafe() method, but if you naively try to call Unsafe.getUnsafe() you, probably, get SecurityException. This class can be instantiated from only trusted code.

But there are always some workarounds. A similar easy way to create an instance is using reflextion:

```java
Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
f.setAccessible(true);
Unsafe unsafe = (Unsafe) f.get(null);
```

Note: Your IDE e.g. eclipse might show error related to Access restriction. Don’t worry. Go ahead and run the program. It will run.

Now coming to main part. With this object you can do following ‘interesting’ tasks.

## **Usage of sun.misc.Unsafe**

**1) Create an instance without any restriction**

Using allocateInstance() method, you can create an instance of a class without invoking it’s constructor code, initialization code, various JVM security checks and all other low level things. Even if class has private constructor, then also you can use this method to create new instance.

> A real nightmare for all Singleton lovers. Guys, You just can’t handle this threat so easily. 🙂

```
public class UnsafeDemo 
{
    public static void main(String[] args) throws NoSuchFieldException, SecurityException, 
                            IllegalArgumentException, IllegalAccessException, InstantiationException 
    {
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
 
Output:
 
0
45
50
```

 **2) Shallow clone using direct memory access**

How to normally do shallow cloning? Calling super.clone() in clone(){..} method, right? Here the problem is that you have to implement Cloneable interface and then override clone() method in all classes where you want to implement shallow cloning. Too much effort for a lazy developer.

I am not recommending this but using unsafe, we can create shallow clones in few lines and best part is that it can be used with any class just like some utility method.

The trick is to copy the bytes of an object to another location in memory, and then typecast this object to cloned object type.

**3) Password security from hackers**

This seems interesting? And yes, it is. Developers create passwords or store passwords in strings and then use them in application code. After using password, smarter developers make the string reference NULL, So that it is not referenced anymore and can be garbage collected easily.

But from the time, you made the reference null to the time garbage collector kicks in, that string instance lies in string pool. And a sophisticated attack on your system will be capable of reading your memory area and thus accessing the passwords also. Chances are low, but they are here.

That’s why it is suggested to use char[] to store passwords so that after use, you can iterate over array and make each character dirty/empty.
Another way is to use our magical class Unsafe. Here you create another temporary string with equal length as password, and store “?” or “*” (or any letter) for each character in temporary password. When you are done with password logic, you simply copy the bytes of temporary password ( e.g. ????????) on original password. It means overwrite the original password with temporary password.

Sample code would look like this.

```
String password = new String("l00k@myHor$e");
String fake = new String(password.replaceAll(".", "?"));
System.out.println(password); // l00k@myHor$e
System.out.println(fake); // ????????????
 
getUnsafe().copyMemory(fake, 0L, null, toAddress(password), sizeOf(password));
 
System.out.println(password); // ????????????
System.out.println(fake); // ????????????
```

Create classes dynamically on runtime

We can create classes in runtime, for example from compiled .class file. To perform that read class contents to byte array and pass it to defineClass method.

```java
//Sample code to craeet classes
byte[] classContents = getClassContent();
Class c = getUnsafe().defineClass(null, classContents, 0, classContents.length);
c.getMethod("a").invoke(c.newInstance(), null); 
 
//Method to read .class file
private static byte[] getClassContent() throws Exception {
    File f = new File("/home/mishadoff/tmp/A.class");
    FileInputStream input = new FileInputStream(f);
    byte[] content = new byte[(int)f.length()];
    input.read(content);
    input.close();
    return content;
}
```

**4) Super big arrays**

As you know Integer.MAX_VALUE constant is a max size of java array. If you want to build real big arrays (no practical need though in normal applications), you can use direct memory allocation for this.

Take an example of this class which creates an sequential memory (array) with double the size of what is allowed.

```java
class SuperArray {
    private final static int BYTE = 1;
    private long size;
    private long address;
     
    public SuperArray(long size) {
        this.size = size;
        address = getUnsafe().allocateMemory(size * BYTE);
    }
    public void set(long i, byte value) {
        getUnsafe().putByte(address + i * BYTE, value);
    }
    public int get(long idx) {
        return getUnsafe().getByte(address + idx * BYTE);
    }
    public long size() {
        return size;
    }
}
```

Sample usage:

long SUPER_SIZE = (long)Integer.MAX_VALUE * 2;
SuperArray array = new SuperArray(SUPER_SIZE);
System.out.println(“Array size:” + array.size()); // 4294967294
for (int i = 0; i < 100; i++) { array.set((long)Integer.MAX_VALUE + i, (byte)3); sum += array.get((long)Integer.MAX_VALUE + i); } System.out.println("Sum of 100 elements:" + sum); // 300 [/java]Please beware it an cause JVM crash.

## **Conclusion**

sun.misc.Unsafe provides almost unlimited capabilities for exploring and modification of VM’s runtime data structures. Despite the fact that these capabilities are almost inapplicable in Java development itself, Unsafe is a great tool for anyone who want to study HotSpot VM without C++ code debugging or need to create ad hoc profiling instruments.