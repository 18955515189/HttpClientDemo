package ApacheHttpClientDemo;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Apache HttpCliect实现http请求
 */
public class HttpClientTest {

    /**
     * 测试 Post 请求
     */
    @Test
    public void testPost(){
        //1.创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.创建HttpPost
        HttpPost httpPost = new HttpPost("http://10.10.20.171:8006/shfs/pwplogin/toLoginPage.do");

        //3.创建参数队列
        List formparams = new ArrayList<>();

        UrlEncodedFormEntity urlEncodedFormEntity;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(formparams,"UTF-8");
            httpPost.setEntity(urlEncodedFormEntity);
            System.out.println(" executing request : "+httpPost.getURI());
            CloseableHttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            if(null!=entity){
                System.out.println(EntityUtils.toString(entity,"UTF-8"));
            }
            response.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接，释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getTest(){
        //1.创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2.创建httpGet
        HttpGet httpGet = new HttpGet("http://10.10.20.171:8006/shfs/pwplogin/toLoginPage.do");

        try {
            //3.执行Get请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            //4.获取响应实体
            HttpEntity entity = response.getEntity();
            if(null!=entity){
                System.out.println(EntityUtils.toString(entity,"UTF-8"));
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
