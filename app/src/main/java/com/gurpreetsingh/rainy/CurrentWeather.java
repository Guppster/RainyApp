package com.gurpreetsingh.rainy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Gurpreet on 2015-01-19.
 */
public class CurrentWeather
{
    private String icon;
    private double humidity;
    private long time;
    private double temperature;
    private double precipChance;
    private String summary;
    private String timeZone;

    public CurrentWeather(String icon, double humidity, long time, double temperature, double precipChance, String summary, String timeZone)
    {
        this.icon = icon;
        this.humidity = humidity;
        this.time = time;
        this.temperature = temperature;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }//End of constructor

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getIconID()
    {
        int iconID = R.drawable.clear_day;   //Documentation states to include default

        switch(icon)
        {
            case "clear-day":
                iconID = R.drawable.clear_day;
                break;
            case "clear-night":
                iconID = R.drawable.clear_night;
                break;
            case "rain":
                iconID = R.drawable.rain;
                break;
            case "snow":
                iconID = R.drawable.snow;
                break;
            case "sleet":
                iconID = R.drawable.sleet;
                break;
            case "wind":
                iconID = R.drawable.wind;
                break;
            case "fog":
                iconID = R.drawable.fog;
                break;
            case "cloudy":
                iconID = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconID = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconID = R.drawable.cloudy_night;
                break;
        }
        return iconID;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public String getFormattedTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.format(new Date(time*1000));
    }

    public int getTemperature()
    {
        return (int)Math.round(temperature);
    }

    public void setTemperature(double temperature)
    {
        this.temperature = temperature;
    }

    public int getPrecipChance()
    {
        return (int)Math.round(precipChance * 100);
    }

    public void setPrecipChance(double precipChance)
    {
        this.precipChance = precipChance;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public double getHumidity()
    {
        return humidity;
    }

    public void setHumidity(double humidity)
    {
        this.humidity = humidity;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }
}
