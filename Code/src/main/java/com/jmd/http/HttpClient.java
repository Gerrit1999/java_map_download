package com.jmd.http;

import com.jmd.util.RequestUtils;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serial;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HttpClient {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    public static HashMap<String, String> HEADERS = new HashMap<>() {

        @Serial
        private static final long serialVersionUID = 9078863629526057150L;

        {
            put(HttpHeaders.ORIGIN, "https://map.tianditu.gov.cn");
            put(HttpHeaders.REFERER, "https://map.tianditu.gov.cn/");
            put("Sec-Fetch-Site", "same-site");
        }
    };

    @Value("${okhttp.connect-timeout}")
    private int connectTimeout;
    @Value("${okhttp.read-timeout}")
    private int readTimeout;
    @Value("${okhttp.write-timeout}")
    private int writeTimeout;
    @Value("${okhttp.max-idle-connections}")
    private int maxIdleConnections;
    @Value("${okhttp.keep-alive-duration}")
    private int keepAliveDuration;

    @Setter
    private OkHttpClient okHttpClient;

    private final Random random = new Random();

    @PostConstruct
    private void init() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // 连接超时
        clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        // 读取超时
        clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        // 写入超时
        clientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        // 连接池
        clientBuilder.connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS));
        // 创建
        okHttpClient = clientBuilder.build();
    }

    public String rebuild() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        // 连接超时
        clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        // 读取超时
        clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        // 写入超时
        clientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        // 连接池
        clientBuilder.connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS));
        // 代理
        if (ProxySetting.enable) {
            try {
                clientBuilder.proxy(
                        new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxySetting.hostname, ProxySetting.port)));
            } catch (Exception e) {
                return "请输入正确的代理参数";
            }
        }
        // 创建
        okHttpClient = clientBuilder.build();
        return "success";
    }

    /**
     * get 请求
     */
    public String doGet(String url) {
        return doGet(url, null, null);
    }

    /**
     * get 请求
     */
    public String doGet(String url, Map<String, String> params) {
        return doGet(url, params, null);
    }

    /**
     * get 请求
     */
    public String doGet(String url, String[] headers) {
        return doGet(url, null, headers);
    }

    /**
     * get 请求
     */
    public String doGet(String url, Map<String, String> params, String[] headers) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && params.keySet().size() > 0) {
            boolean firstFlag = true;
            for (String key : params.keySet()) {
                if (firstFlag) {
                    sb.append("?").append(key).append("=").append(params.get(key));
                    firstFlag = false;
                } else {
                    sb.append("&").append(key).append("=").append(params.get(key));
                }
            }
        }
        Request.Builder builder = new Request.Builder();
        if (headers != null && headers.length > 0) {
            if (headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i = i + 2) {
                    builder.addHeader(headers[i], headers[i + 1]);
                }
            } else {
                log.warn("headers's length[{}] is error.", headers.length);
            }

        }
        Request request = builder.url(sb.toString()).build();
        log.info("do get request and url[{}]", sb.toString());
        return execute(request);
    }

    /**
     * post 请求
     */
    public String doPost(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        log.info("do post request and url[{}]", url);

        return execute(request);
    }

    /**
     * post 请求
     */
    public String doPostJson(String url, String json) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, json, JSON);
    }

    /**
     * post 请求
     */
    public String doPostXml(String url, String xml) {
        log.info("do post request and url[{}]", url);
        return exectePost(url, xml, XML);
    }

    private String exectePost(String url, String data, MediaType contentType) {
        RequestBody requestBody = RequestBody.create(contentType, data);
        Request request = new Request.Builder().url(url).post(requestBody).build();

        return execute(request);
    }

    private String execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return "";
    }

    /**
     * 获取文件流
     */
    public byte[] getFileBytes(String url, HashMap<String, String> headers) {
        url = URLDecoder.decode(url, StandardCharsets.UTF_8);
        headers.put(HttpHeaders.USER_AGENT, RequestUtils.getRandomUserAgent());

        if (url.contains("tianditu")) {
            int randomT = random.nextInt(8);
            url = url.replaceFirst("t\\d", "t" + randomT);
        }
        log.info("url = {}", url);

        Builder builder = new Request.Builder().url(url);
        for (Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
        Request request = builder.build();
        Response response = null;
        byte[] buf = new byte[0];
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                buf = response.body().bytes();
            } else {
                log.error("code = {}", response.code());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return buf;
    }

}
