package model;

/**
 * Created by holten on 2017/1/13.
 */
public class Weather {
    private String weather_curr;
    private String temp_curr;
    private String humidity;
    private String wind;
    private String winp;

    public String getWeather_curr() {
        return weather_curr;
    }

    public void setWeather_curr(String weather_curr) {
        this.weather_curr = weather_curr;
    }

    public String getTemp_curr() {
        return temp_curr;
    }

    public void setTemp_curr(String temp_curr) {
        this.temp_curr = temp_curr;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWinp() {
        return winp;
    }

    public void setWinp(String winp) {
        this.winp = winp;
    }
}
