package thread;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helper.DataBaseHelper;
import http.HttpSender;
import model.Data;
import model.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JsonUtil;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by holten.gao on 2016/7/13.
 */
public class GetWeatherThread implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(GetWeatherThread.class);

    @Override
    public void run() {
        System.out.println("GetWeatherThread run");
        logger.info("GetWeatherThread Run...");
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("deviceid", "10500003");

        String atmosphere = "-1";
        String temperature = "-1";
        String humidity = "-1";
        String windDirection = "-1";
        String windSpeed = "-1";
        String rainfall = "-1";

        String currAtmUrl = "http://api.weatherdt.com/common/?area=101021100&type=observe&key=44e0a77fca5c053e3d9a874d928ffcdd";
        String currWeatherUrl = "http://api.k780.com/?app=weather.today&weaid=44&appkey=29938&sign=686613710a2823f35dfce169f7b53a5a&format=json";

        String weatherResp = HttpSender.getHttpResponse(currWeatherUrl);
        System.out.println(weatherResp);
        String parasResp = HttpSender.getHttpResponse(currAtmUrl);
        System.out.println(parasResp);

        parasResp = parasResp.substring(35, parasResp.length() - 3);
        Map atoms = JsonUtil.toMap(parasResp);
        temperature = atoms.get("002").toString().substring(1, atoms.get("002").toString().length() - 1);
        System.out.println("temperature: " + temperature+new Timestamp(System.currentTimeMillis()));
        humidity = atoms.get("005").toString().substring(1, atoms.get("005").toString().length() - 1);
        System.out.println("humidity:" + humidity+new Timestamp(System.currentTimeMillis()));
        windDirection = atoms.get("004").toString().substring(1, atoms.get("004").toString().length() - 1);
        System.out.println("windDirection:" + windDirection);
        windSpeed = atoms.get("003").toString().substring(1, atoms.get("003").toString().length() - 1);
        System.out.println("Wind Speed:" + windSpeed+new Timestamp(System.currentTimeMillis()));
        atmosphere = atoms.get("007").toString().substring(1, atoms.get("007").toString().length() - 1);
        System.out.println("Atmosphere:" + atmosphere+new Timestamp(System.currentTimeMillis()));
        rainfall = atoms.get("006").toString().substring(1, atoms.get("006").toString().length() - 1);
        System.out.println("Rainfall:" + rainfall+new Timestamp(System.currentTimeMillis()));

        float atmosphereF = Float.parseFloat(atmosphere) * 100;
        dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
        dataMap.put("value", atmosphereF);
        DataBaseHelper.insertEntity("t_atmosphere", dataMap);
        System.out.println("Save atmosphere."+new Timestamp(System.currentTimeMillis()));

        dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
        dataMap.put("value", temperature);
        DataBaseHelper.insertEntity("t_airtemp", dataMap);
        System.out.println("Save Temperature."+new Timestamp(System.currentTimeMillis()));

        dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
        dataMap.put("value", humidity);
        DataBaseHelper.insertEntity("t_airhumidity", dataMap);
        System.out.println("Save airhumidity."+new Timestamp(System.currentTimeMillis()));

        dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
        dataMap.put("value", convertWindDirect(windDirection));
        DataBaseHelper.insertEntity("t_winddirect", dataMap);
        System.out.println("Save winddirect."+new Timestamp(System.currentTimeMillis()));

        dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
        dataMap.put("value", convertWindSpeed(windSpeed));
        DataBaseHelper.insertEntity("t_windspeed", dataMap);
        System.out.println("Save wind speed." +new Timestamp(System.currentTimeMillis()));

        dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
        dataMap.put("value", rainfall);
        DataBaseHelper.insertEntity("t_rainfall", dataMap);
        System.out.println("Save rainfall."+ new Timestamp(System.currentTimeMillis()));

        if ("\"1\"".equals(JsonUtil.toMap(weatherResp).get("success").toString())) {
            weatherResp = weatherResp.substring(24, weatherResp.length() - 1);
            Gson gson = new GsonBuilder().create();
            Weather weathers = gson.fromJson(weatherResp, Weather.class);
            String weather = weathers.getWeather_curr();
            dataMap.put("sampletime", new Timestamp(System.currentTimeMillis()).toString());
            dataMap.put("value", weather);
            DataBaseHelper.insertEntity("t_weather", dataMap);
            System.out.println("Save weather."+ new Timestamp(System.currentTimeMillis()));
        }

//        String sql = "SELECT * FROM t_light WHERE deviceid=10200013 ORDER BY id DESC LIMIT 1";
//        Data solar = DataBaseHelper.queryEntity(Data.class, sql);
//        dataMap.put("sampletime", solar.getSampletime());
//        dataMap.put("value", solar.getValue());
//        DataBaseHelper.insertEntity("t_solar", dataMap);
        logger.info("Get Weather Resp...");
    }

    private static String convertWindDirect(String windDirect) {
        if ("8".equals(windDirect)) {
            return "0";
        } else if ("1".equals(windDirect)) {
            return "45";
        } else if ("2".equals(windDirect)) {
            return "90";
        } else if ("3".equals(windDirect)) {
            return "135";
        } else if ("4".equals(windDirect)) {
            return "180";
        } else if ("5".equals(windDirect)) {
            return "215";
        } else if ("6".equals(windDirect)) {
            return "270";
        } else if ("7".equals(windDirect)) {
            return "315";
        } else {
            return "0";
        }
    }

    private static String convertWindSpeed(String windLevel) {
        int windIndex = Integer.valueOf(windLevel);
        String[] windSpeeds = {"0.1", "0.8", "3.5", "6", "9", "11", "15", "19", "22", "25", "30", "35"};
        return windSpeeds[windIndex];
    }
}
