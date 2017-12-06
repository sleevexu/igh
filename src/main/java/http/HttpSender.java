package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by holten on 2017/1/13.
 */
public class HttpSender {
    private final Logger logger = LoggerFactory.getLogger(HttpSender.class);

    public static String getHttpResponse(String requestUrl) {
        BufferedReader in = null;
        StringBuffer result;
        try {
            URI uri = new URI(requestUrl);
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "utf-8");
            // 设置 HttpURLConnection的字符编码
            connection.setRequestProperty("Accept-Charset", "utf-8");

            connection.connect();
            result = new StringBuffer();
            //读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}
