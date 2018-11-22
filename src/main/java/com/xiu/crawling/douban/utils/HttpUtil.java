package com.xiu.crawling.douban.utils;

import com.xiu.crawling.douban.proxypool.config.Constant;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * HTTP 请求工具类
 *
 * @author :
 * @version : 1.0.0
 * @date : 2015/7/21
 * @see : TODO
 */
public class HttpUtil {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;

    /**
     * 模拟的浏览器请求
     */
    private  static String userAgent;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        requestConfig = configBuilder.build();

        //
        //String[] userAgents = {""};
        //Random random = new Random();
        //userAgent = userAgents[random.nextInt(userAgents.length)+1];

    }

    /**
     * 发送 GET 请求（HTTP），不带输入数据
     * @param url
     * @return
     */
    public static String doGet(String url,HttpHost proxy) {
        return doGet(url, proxy,new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url,HttpHost proxy, Map<String, Object> params) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0) {
                param.append("?");
            }
            else {
                param.append("&");
            }
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        try {
            HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.setHeader("User-Agent", Constant.userAgentArray[new Random().nextInt(Constant.userAgentArray.length)]);


            RequestConfig requestConfig=RequestConfig.custom().setProxy(proxy).build();
            CloseableHttpClient httpclient = getCloseableHttpClient();

            HttpResponse response = httpclient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println("url: "+url+" 执行状态码 : " + statusCode);
            if(statusCode == 403 ||statusCode == 404){
                return statusCode+"";
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static CloseableHttpClient getCloseableHttpClient() {
        CookieStore cookieStore = new BasicCookieStore();
        /**
         * __utma    .movie.douban.com    /
         *  __utmc 223695111
         *  __utmz 223695111.1542685731.3.3.utmcsr=open.weixin.qq.com|utmccn=(referral)|utmcmd=referral|utmcct=/connect/qrconnect
         *  _pk_id.100001.4cf6  788cf61a5f78dd1e.1539257636.3.1542685731.1541070833.  movie.douban.com
         *  _pk_ref.100001.4cf6  %5B%22%22%2C%22%22%2C1542685731%2C%22https%3A%2F%2Fopen.weixin.qq.com%2Fconnect%2Fqrconnect%3Fappid%3Dwxd9c1c6bbd5d59980%26redirect_uri%3Dhttps%253A%252F%252Fwww.douban.com%252Faccounts%252Fconnect%252Fwechat%252Fcallback%26response_type%3Dcode%26scope%3Dsnsapi_login%26state%3DmABx-QaEx5U%252523douban-web%252523https%25253A%252F%252Fmovie.douban.com%252Ftyperank%25253Ftype_name%25253D%252525E5%25252589%252525A7%252525E6%25252583%25252585%252526type%25253D11%252526interval_id%25253D100%25253A90%252526action%25253D%22%5D
         */
        BasicClientCookie cookie = new BasicClientCookie("bid",RandCookie.getRandomCode(11));

        cookie.setVersion(0);
        cookie.setDomain("*");
        cookie.setPath("/");

        BasicClientCookie __utma = new BasicClientCookie("__utma","223695111.1100173818.1539257636.1541070696.1542685731.3");
        __utma.setVersion(0);
        __utma.setDomain(".movie.douban.com");
        __utma.setPath("/");

        BasicClientCookie __utmc = new BasicClientCookie("__utmc","223695111");
        __utmc.setVersion(0);
        __utmc.setDomain(".movie.douban.com");
        __utmc.setPath("/");

        BasicClientCookie __utmz = new BasicClientCookie("__utmc","223695111");
        __utmz.setVersion(0);
        __utmz.setDomain(".movie.douban.com");
        __utmz.setPath("/");

        BasicClientCookie _pk_id = new BasicClientCookie("_pk_id.100001.4cf6","788cf61a5f78dd1e.1539257636.3.1542685731.1541070833.");
        _pk_id.setVersion(0);
        _pk_id.setDomain("movie.douban.com");
        _pk_id.setPath("/");

        BasicClientCookie _pk_ref = new BasicClientCookie("_pk_ref.100001.4cf6","%5B%22%22%2C%22%22%2C1542685731%2C%22https%3A%2F%2Fopen.weixin.qq.com%2Fconnect%2Fqrconnect%3Fappid%3Dwxd9c1c6bbd5d59980%26redirect_uri%3Dhttps%253A%252F%252Fwww.douban.com%252Faccounts%252Fconnect%252Fwechat%252Fcallback%26response_type%3Dcode%26scope%3Dsnsapi_login%26state%3DmABx-QaEx5U%252523douban-web%252523https%25253A%252F%252Fmovie.douban.com%252Ftyperank%25253Ftype_name%25253D%252525E5%25252589%252525A7%252525E6%25252583%25252585%252526type%25253D11%252526interval_id%25253D100%25253A90%252526action%25253D%22%5D");
        _pk_ref.setVersion(0);
        _pk_ref.setDomain("movie.douban.com");
        _pk_ref.setPath("/");


        cookieStore.addCookie(__utma);
        cookieStore.addCookie(__utmc);
        cookieStore.addCookie(__utmz);
        cookieStore.addCookie(_pk_id);
        cookieStore.addCookie(_pk_ref);
        return HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            httpPost.setHeader("User-Agent", Constant.userAgentArray[new Random().nextInt(Constant.userAgentArray.length)]);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            CloseableHttpClient httpclient = getCloseableHttpClient();
            response = httpclient.execute(httpPost);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl
     * @param json json对象
     * @return
     */
    public static String doPost(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine().getStatusCode());
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).
                setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     * @param apiUrl API接口URL
     * @param json JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

}
