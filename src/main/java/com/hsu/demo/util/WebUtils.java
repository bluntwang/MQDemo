package com.hsu.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.vipkid.invoker.exceptions.ServerRequestException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);
    private static final String DEFAULT_CHARSET = "UTF8";
    private static final int DEFAULT_TIMEOUT = 2 * 1000;
    private static final RequestConfig DEFAULT_REQUEST_CONFIG = RequestConfig.custom()
            .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
            .setConnectTimeout(DEFAULT_TIMEOUT)
            .setSocketTimeout(DEFAULT_TIMEOUT)
            .build();

    public static String multipartPost(String url, Map<String, String> params, String filename, File fileToUpload) {
        LOGGER.info("multipartPost,url = {},params = {},fileName = {}", url, params.toString(), filename);
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(DEFAULT_CHARSET));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("slidesFile", fileToUpload, ContentType.DEFAULT_BINARY, filename);
        params.forEach((k,v)->{
            builder.addTextBody(k, v, ContentType.DEFAULT_BINARY);
        });
        /*for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.DEFAULT_BINARY);
        }*/

        return doMutipartPost(url, post, builder);
    }

    public static String multipartPost(String url, File fileToUpload, String filename) {
        LOGGER.info("multipartPost,url = {} ,fileName = {}", url, filename);
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(DEFAULT_CHARSET));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upLoadFile", fileToUpload, ContentType.DEFAULT_BINARY, filename);
        return doMutipartPost(url, post, builder);
    }

    public static String multipartPost(String url, InputStream stream, String filename) {
        LOGGER.info("multipartPost,url = {},fileName = {} ", url, filename);
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(DEFAULT_CHARSET));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upLoadFile", stream, ContentType.DEFAULT_BINARY, filename);
        return doMutipartPost(url, post, builder);
    }

    public static String multipartPost(String url, byte[] bytes, String filename) {
        LOGGER.info("multipartPost,url = {} ,fileName = {} ", url, filename);
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(DEFAULT_CHARSET));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upLoadFile", bytes, ContentType.DEFAULT_BINARY, filename);
        return doMutipartPost(url, post, builder);
    }

    private static String doMutipartPost(String url, HttpPost post, MultipartEntityBuilder builder) {
        HttpEntity entity = builder.build();
        post.setEntity(entity);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(post);
            LOGGER.info("multipartPost,url = {},responseStatusLine = {}", url, response.getStatusLine());
            HttpEntity entity2 = response.getEntity();
            return EntityUtils.toString(entity2);
        } catch (Exception e) {
            LOGGER.error("multipartPost error", e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("multipartPost error", e);
                }
            }
        }
        return null;
    }

    public static String postJSONString(String url, String s, boolean withAuth, String authorization) {
        if (!withAuth) {
            LOGGER.warn("without auth only for test!");
        }
        LOGGER.info("Post data,url = {},params = {}", url, s);
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity jsonEntity = new StringEntity(s, DEFAULT_CHARSET);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(jsonEntity);
            if (withAuth) {
                httpPost.addHeader("Authorization", authorization);
            }
            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpPost);
            LOGGER.info("Post data,response status line = {}, requestStr = {}", response.getStatusLine(), s);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            LOGGER.error("Post data error,url = {},params = {}", url, s, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输出流时出错，url = {}", url, e);
                }
            }
        }
        return null;
    }

    public static String simpleGet(String url) {
        LOGGER.info("get data,url = {}", url);
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(DEFAULT_REQUEST_CONFIG);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpGet);
            LOGGER.info("get data,response status line = {}", response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String rt = EntityUtils.toString(entity);
            if (HttpStatus.OK.value() != response.getStatusLine().getStatusCode()) {
                LOGGER.error("http post error = {} ", rt);
                throw new Exception(rt);
            }
            return rt;
        } catch (Exception e) {
            LOGGER.error("get data error,url = {}", url, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输出流时出错，url = {}", url, e);
                }
            }
        }
        return null;
    }

    public static String post(String url, Map<String, String> params) {
        LOGGER.info("Post data,url = {},params = {}", url, params);
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> paramsList = Lists.newArrayList();
            params.forEach((k,v)->{
                paramsList.add(new BasicNameValuePair(k, v));
            });
           /* for (Map.Entry<String, String> entry : params.entrySet()) {
                paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }*/

            httpPost.setConfig(DEFAULT_REQUEST_CONFIG);
            httpPost.setEntity(new UrlEncodedFormEntity(paramsList, Charset.forName(DEFAULT_CHARSET)));
            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpPost);
            LOGGER.info("Post data,response status line = {}", response.getStatusLine());
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            LOGGER.error("Post data error,url = {},params = {}", url, params, e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输出流时出错，url = {}", url, e);
                }
            }
        }
        return null;
    }

    public static String postJSON(String url, Object object) {
        JSONObject json = JsonUtils.toJSONObject(object);
        LOGGER.info("Post data,url = {},params = {}", url, json);
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity jsonEntity = new StringEntity(json.toJSONString(),DEFAULT_CHARSET);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(jsonEntity);
//            httpPost.addHeader("Authorization", UserUtils.getAuthorization());
            CloseableHttpClient httpclient = HttpClients.createDefault();
            response = httpclient.execute(httpPost);
            LOGGER.info("Post data,response status line = {}",response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String rt = EntityUtils.toString(entity);
            return rt;
        } catch (Exception e) {
            LOGGER.error("Post data error,url = {},params = {}",url,json,e);
        } finally {
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("关闭输出流时出错，url = {}",url,e);
                }
            }
        }
        return null;
    }

    public static String query(Map<String, String> queryMap){
        if(queryMap!=null && queryMap.size()>0) {
            return Joiner.on("&").withKeyValueSeparator("=").join(queryMap);
        }
        return "";
    }

    public static String buildQueryString(Map<String, String> params) throws Exception {
        if (params == null || params.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> {
            try {
                sb.append("&")
                        .append(URLEncoder.encode(k, DEFAULT_CHARSET))
                        .append("=")
                        .append(URLEncoder.encode(v, DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                sb.append("");
            }
        });
        return sb.substring(1);

/*        String q = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (q.length() > 0) {
                q += "&";
            }
            q += URLEncoder.encode(entry.getKey() , DEFAULT_CHARSET) + "=" + URLEncoder.encode(entry.getValue(), DEFAULT_CHARSET);
        }
        return q;*/
    }

    public static String getHost(String url) {
        Pattern p = Pattern.compile("(http://|https://)?([^/]*)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(url);
        return m.find() ? m.group(0) : url;
    }

    public static String getHost(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        LOGGER.info("referer: {}", referer);
        String host = "";
        if (StringUtils.isEmpty(referer)) {
            LOGGER.warn("获取url refer为空!");
        } else {
            host = getHost(referer);
        }
        LOGGER.info("host:{}", host);
        return host;
    }

    public static String getAuthorization(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            authorization = "";
        }
        LOGGER.info("Authorization: {}", authorization);
        return authorization;
    }
}
