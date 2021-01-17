# 010-Java事件模型-面向接口

[TOC]

## 设计模式-观察者模式

 [07-observer-pattern.md](../../01-design-patterns/04-behavioral-patterns/07-observer-pattern.md) 

## 观察者模式核心类

- 可观察对象(消息发送者) - java.util.Observable
- 观察者 -java.util.Obsever

## 标准化接口

- 事件对象-java.uti.EventObject
- 事件监听器-java.util.EventListener

## 事件/监听器场景举例

| Java技术规范                                            | 事件接口                              | 监听器接口                               |
| ------------------------------------------------------- | ------------------------------------- | ---------------------------------------- |
| [JavaBeans](../12-Java自省机制/004-JavaBean事件监听.md) | java.beans.PropertyChangeEvent        | java.beans.PropertyChangeListener        |
| Java AWT                                                | java.awt.event.MouseEvent             | java.awt.event.MouseListener             |
| Java Swing                                              | javax.swing.envent.MenuEvent          | javax.swing.event.MenuListener           |
| Java Preference                                         | java.util.prefs.ProperenceChangeEvent | java.util.prefs.PreferenceChangeListener |

## 源码分析

#### java.util.EventObject

事件要有一个事件源

```java
public class EventObject implements java.io.Serializable {

    private static final long serialVersionUID = 5516075349620653480L;

    protected transient Object  source;

    public EventObject(Object source) {
        if (source == null)
            throw new IllegalArgumentException("null source");

        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}

```

####  java.util.Observable

```java
public class Observable {
    private boolean changed = false;
    private Vector<Observer> obs;

    public synchronized void addObserver(Observer o) {
    public synchronized void deleteObserver(Observer o) {
    public void notifyObservers() {
    public synchronized void deleteObservers() {
    protected synchronized void setChanged() {
    protected synchronized void clearChanged() {
    public synchronized boolean hasChanged() {
    public void notifyObservers(Object arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((Observer)arrLocal[i]).update(this, arg);
    }
}

```

#### java.util.Observer

```java
public interface Observer {
    void update(Observable o, Object arg);
}
```

## 代码实例

```java
public class ObserverDemo {

    public static void main(String[] args) {
        EventObservable observable = new EventObservable();
        // 添加观察者（监听者）
        observable.addObserver(new EventObserver());
        // 发布消息（事件）
        observable.notifyObservers("Hello,World");
    }
  
  
    static class EventObservable extends Observable {

        public void setChanged() {
            super.setChanged();
        }

        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(new EventObject(arg));
            clearChanged();
        }
    }

    static class EventObserver implements Observer, EventListener {

        @Override
        public void update(Observable o, Object event) {
            EventObject eventObject = (EventObject) event;
            System.out.println("收到事件 ：" + eventObject);
        }
    }
}

```

#### 输出

```
收到事件 ：java.util.EventObject[source=Hello,World]
```

