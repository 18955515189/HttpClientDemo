package JDKHttpsClientDemo;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class HttpsURLConnectionDemo {

    /**
     * HTTP 请求方式
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) throws Exception {
        String responseMessage = "BUG";
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            ;
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            //1.创建HttpURLConnection对象
            URL url = new URL(requestUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            //2.添加Http请求头（对于爬虫来说这个设置是必要的，很多网站回对请求头中的Referer检查，防爬）
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "utf-8");
            //3.设置是包含输入输出参数
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //4.设置请求方式（POST | GET）
            connection.setRequestMethod(requestMethod);

            //5.设置当前实例使用的SSLSoctetFactory
            connection.setSSLSocketFactory(ssf);
            if (null != outputStr) {
                OutputStream os = connection.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            //6.请求连接
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (200 == responseCode) {
                //获取请求内容
                responseMessage = readResponseBody(connection.getInputStream());
                JSONObject jsStr = JSONObject.parseObject(responseMessage);
                Object wp_bill_sync_response = jsStr.get("wp_bill_sync_response");
                //PublicPaymentDTOs.PublicPaymentSyncServerDTOs.ResponseDTO responseDTO = JSON.parseObject(wp_bill_sync_response.toString(), PublicPaymentDTOs.PublicPaymentSyncServerDTOs.ResponseDTO.class);
            } else {
                // TODO 请求错误处理
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }

    private static String readResponseBody(InputStream inputStream) throws Exception {
        Reader reader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }


    public static void main(String[] args) throws Exception {
        String s=httpRequest("https://kyfw.12306.cn/","GET",null);
        System.out.println(s);
    }


}
