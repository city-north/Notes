[返回根目录](/README.md)

[返回目录](../README.md)

# 观察者模式（observer Pattern）

定义对象之间的一种一对多依赖关系，使得每当一个对象状态发生变化时，其相关依赖的对象都能得到通知并自动更新。

![img](assets/timg.jpg)



## 角色

- **Subject(目标)**: 目标又称为主题，它是被观察的对象。JDK中可以通过继承`Observable`类来实现被观察的需求。其中定义了一个观察者的集合`private Vector<Observer> obs;` 当目标发生变化时，调用`setChange`标记，并使用`notifyObservers`方法循环通知Observers。
- **Observer（观察者对象）**：观察者，可以通过实现JDK中的`Observer`类建立，当目标发生变化后，会调用`update`方法。

## 代码实例

`WeatherData`类是目标类，通过继承`Observable`类实现，`notifyObservers`方法会通知观察者

```java
/**
 * @author Chen 2018/9/3
 */
public class WeatherData extends Observable {
    private float temperature;
    private float humidity;
    private float pressure;

    enum DataType{
        TEMPERATURE(),
        HUMIDITY(),
        PRESSURE()
    }
    //通知观察者
    private void measurementsChanged(DataType dataType){
        notifyObservers(dataType);
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
        setChanged();//当发生温度改变时，循环调用通知观察者的方法
        measurementsChanged(DataType.TEMPERATURE);
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
        setChanged();
        measurementsChanged(DataType.HUMIDITY);
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
        setChanged();
        measurementsChanged(DataType.PRESSURE);
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                "} " + super.toString();
    }
}

```

```java
/**
 * @author Chen 2018/9/3
 */
public class CurrentCondictionDisplay implements DisplayElement, Observer {

    private float temperature;
    private float humidity;
    private float pressure;
    private Observable subject;

    public CurrentCondictionDisplay(Observable target) {
        this.subject = observer;
        //初始化观察者时，会注入观察者
        target.addObserver(this);
    }
    public void  unsubscribe(){
        subject.deleteObserver(this);
    }

    public void display() {
        System.out.println("-------↓实时天气↓-------");
        System.out.println(subject);
        System.out.println("-------↑实时天气↑-------");
    }

    //当目标发生变化时，会调用update方法
    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData){
            WeatherData o1 = (WeatherData) o;
            WeatherData.DataType dataType = (WeatherData.DataType) arg;
            //根据更改参数的类型试试更新展示
            switch (dataType){
                case TEMPERATURE:
                    this.temperature = o1.getTemperature();
                    display();
                    break;
                case HUMIDITY:
                    this.humidity = o1.getHumidity();
                    display();
                    break;
                case PRESSURE:
                    this.pressure = o1.getPressure();
                    display();
                    break;
            }
        }
    }

}

```

客户端代码

```java
䯮只需要维护
/**
 * @author Chen 2018/9/4
 */
public class Test {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentCondictionDisplay currentCondictionDisplay = new CurrentCondictionDisplay(weatherData);

        weatherData.setHumidity(1.0f);
        weatherData.setPressure(2.0f);
        weatherData.setTemperature(3.0f);
        ForecastCondictionDisplay forecastCondictionDisplay = new ForecastCondictionDisplay(weatherData);
        //在更新数据后，观察者会试试相应。
        weatherData.setTemperature(4.0f);
        weatherData.setHumidity(5.0f);
        weatherData.setPressure(6.0f);
    }
}

```

## 优势

- 观察者模式实现表示层和数据逻辑层的分离。
- 在目标和观察者之间建立一个抽象的耦合，观察目标只需要维持一个抽象观察者的集合，无需了解其具体的观察者。
- 观察者模式支持广播通讯，观察目标会向所有已注册的观察者发送通知，简化一对多系统的设计难度，
- 观察者模式符合开闭原则

## 缺点

- 如果存在循环依赖，系统将有可能崩溃



# 监听器模式

![img](assets/8129c489-e633-332b-934b-f1d02da88cd1.png)







[返回根目录](/README.md)

[返回目录](../README.md)
