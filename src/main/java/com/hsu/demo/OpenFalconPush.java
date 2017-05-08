package com.hsu.demo;

import com.hsu.demo.util.RestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

/**
 * Created by xushengxiang on 2017/5/3.
 */
public class OpenFalconPush {
    /**
     * ts=`date +%s`;
     * curl -X POST -d
     * "[{\"metric\": \"metric.demo\", \"endpoint\": \"qd-open-falcon-judge01.hd\",
     * \"timestamp\": $ts,\"step\": 60,\"value\": 9,\"counterType\": \"GAUGE\",
     * \"tags\": \"project=falcon,module=judge\"}]" http://127.0.0.1:1988/v1/push
     */
  /*  public static void main(String[] args) {
        String json = "[{\"metric\": \"ctpr\", \"endpoint\": \"ip-10-1-3-50\"," +
                "\"timestamp\":"+ System.currentTimeMillis()+",\"step\": 60,\"value\": "+ new Random().nextInt(2000) +
                ",\"counterType\": \"GAUGE\",\"tags\": \"module=invoker\"}]";
        String url = "http://push.falcon.vipkid.com.cn/v1/push";
        RestUtils.post(new RestTemplate(),url,null, json)

    }*/
}
