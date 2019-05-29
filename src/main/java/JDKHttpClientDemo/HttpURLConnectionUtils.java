package JDKHttpClientDemo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpURLConnectionUtils {

    /**
     * HTTP 请求方式
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
        String responseMessage = "BUG";
        try {
            //1.创建HttpURLConnection对象
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //2.添加Http请求头（对于爬虫来说这个设置是必要的，很多网站回对请求头中的Referer检查，防爬）
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "utf-8");
            //3.设置请求包含输入以及输出内容
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //4.设置请求类型（POST | GET）
            connection.setRequestMethod(requestMethod);
            //5.写入请求内容
            if(null != outputStr){
                OutputStream os=connection.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.flush();
                os.close();
            }
            //6.建立连接
            connection.connect();

            int responseCode = connection.getResponseCode();
            if(200 == responseCode){
                //请求成功 处理业务逻辑
                responseMessage = readResponseBody(connection.getInputStream());
            }else{
                // TODO 请求错误处理
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseMessage;
    }

    private static String readResponseBody(InputStream inputStream) throws Exception{
        Reader reader = new InputStreamReader(inputStream,"utf-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }
    
    public static void main(String[] args) {
        String s=httpRequest("http://10.10.20.171:8006/shfs/pwplogin/toLoginPage.do","POST",null);
        System.out.println(s);
    }

}
