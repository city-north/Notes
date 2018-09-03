package com.eric.designpattern.BehavioralPatterns.ObserverPattern;

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
        weatherData.setTemperature(4.0f);
        weatherData.setHumidity(5.0f);
        weatherData.setPressure(6.0f);
    }
}
