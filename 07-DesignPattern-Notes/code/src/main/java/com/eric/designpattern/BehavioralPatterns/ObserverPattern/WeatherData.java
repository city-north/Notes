package com.eric.designpattern.BehavioralPatterns.ObserverPattern;

import java.util.Observable;

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
        setChanged();
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
