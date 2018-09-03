package com.eric.designpattern.BehavioralPatterns.ObserverPattern;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Chen 2018/9/3
 */
public class CurrentCondictionDisplay implements DisplayElement, Observer {

    private float temperature;
    private float humidity;
    private float pressure;
    private Observable subject;

    public CurrentCondictionDisplay(Observable observer) {
        this.subject = observer;
        observer.addObserver(this);
    }
    public void  unsubscribe(){
        subject.deleteObserver(this);
    }

    public void display() {
        System.out.println("-------↓实时天气↓-------");
        System.out.println(subject);
        System.out.println("-------↑实时天气↑-------");
    }

    public void update(Observable o, Object arg) {
        if (o instanceof WeatherData){
            WeatherData o1 = (WeatherData) o;
            WeatherData.DataType dataType = (WeatherData.DataType) arg;
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
