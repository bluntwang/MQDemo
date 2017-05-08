package com.hsu.demo;

import com.hsu.demo.util.RestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xushengxiang on 2017/4/24.
 */
public class ClassroomBuild {

    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();

        long timestamp = new Date().getTime();

        Map<String,String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String url = "http://127.0.0.1:9090/api/invoker/service/classrooms";

        Map createClassroomBody = new HashMap();
        createClassroomBody.put("title","欢迎新同学-VIPKID新生指导课");
        createClassroomBody.put("startDateTime",timestamp);
        createClassroomBody.put("supplierCode",2);
        createClassroomBody.put("documentId","8d77cc84fbff4d7186a23b05b3e0d9d0");
        createClassroomBody.put("roomType", 1);
        createClassroomBody.put("length",300);

        String jsonBody = "{\"title\":\"欢迎新同学-VIPKID新生指导课\"" +
                ",\"startDateTime\":"+ timestamp +"," +
                "\"supplierCode\":2,\"" +
                "documentId\":\"8d77cc84fbff4d7186a23b05b3e0d9d0\"" +
                ",\"roomType\":1,\"length\":300}";

        String post = RestUtils.post(restTemplate, url, header, createClassroomBody);
        System.out.println(post);
    }


}
