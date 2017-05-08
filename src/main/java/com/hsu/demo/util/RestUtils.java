package com.hsu.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * Created by KeTong on 2017/3/31.
 */
public class RestUtils {

    private static final String DEFAULT_CHARSET = "UTF8";
    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtils.class);

    public static HttpHeaders setHeaders(Map<String, String> paramMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        paramMap.forEach(httpHeaders::add);
        return httpHeaders;
    }

    public static String post(RestTemplate restTemplate, String url, Map httpHeaders, Map paramMap) {
        LOGGER.info("post request,url = {}, paramMap = {}, header={}",url,paramMap,httpHeaders);
        MultiValueMap postParameters = new LinkedMultiValueMap<>();
        paramMap.forEach(postParameters::add);
        HttpEntity<MultiValueMap<String,String>> requestEntity  = new HttpEntity<>(postParameters, setHeaders(httpHeaders));
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        LOGGER.info("post response,status = {}, body = {}", response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public static String get(RestTemplate restTemplate, String url, Map<String, String> map) {
        LOGGER.info("get request,url = {}, paramMap = {}",url,map);
        String forObject = restTemplate.getForObject(url, String.class, map);
        LOGGER.info("get response = {}", forObject);
        return forObject;
    }

    public static String simpleGet(RestTemplate restTemplate, String url) {
        LOGGER.info("get request,url = {}",url);
        String forObject = restTemplate.getForObject(url, String.class);
        LOGGER.info("get response = {}", forObject);
        return forObject;
    }

    public static String putJson(RestTemplate restTemplate, String url, Map<String, String> headerParams, String jsonBody, RestCallBack<String> callBack){
        LOGGER.info("put request,url = {}, content = {}, header={}", url, jsonBody, headerParams.toString());
        HttpEntity<String> requestEntity  = new HttpEntity<>(jsonBody, setHeaders(headerParams));
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        }catch (RestClientException e){
            LOGGER.error("RestClientException", e);
            return callBack.throwException(e);
        }
        LOGGER.info("put response,status = {}, body = {}", response.getStatusCode(), response.getBody());
        if(response.getStatusCode() != HttpStatus.OK){
            return callBack.failed(response);
        }
        return callBack.success(response);
    }

    public interface RestCallBack<T>{
        /**
         * RestTemplate response return success
         * @param responseEntity
         * @return T
         */
        T success(ResponseEntity<T> responseEntity);

        /**
         * RestTemplate response status code != 200
         * @param responseEntity
         * @return T
         */
        T failed(ResponseEntity<T> responseEntity);

        /**
         * RestTemplate exchange occur RestClientException or other Exception
         * @param e
         * @return T
         */
        T throwException(Exception e);
    }

}
