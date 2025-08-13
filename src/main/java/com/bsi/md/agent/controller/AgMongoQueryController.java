package com.bsi.md.agent.controller;

import com.alibaba.fastjson.JSONObject;
import com.bsi.md.agent.config.MongoConfigProperties;
import com.bsi.md.agent.utils.AgSystemInfoUtil;
import com.bsi.utils.MongoDBUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mongodb服务
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/mongo")
public class AgMongoQueryController {

    @PostMapping("/query")
    public List<Document> query(@RequestBody JSONObject params) {
        if( !MongoConfigProperties.isEnabled() ){
            return new ArrayList<>();
        }
        return MongoDBUtils.queryDocuments(params);
    }

    @PostMapping("/aggregate")
    public List<Document> aggregate(@RequestBody JSONObject params) {
        if( !MongoConfigProperties.isEnabled() ){
            return new ArrayList<>();
        }
        return MongoDBUtils.queryAndAggregate(params);
    }

    @GetMapping("/system/info")
    public Map<String, Object> getSystemInfo() {
        return AgSystemInfoUtil.getSystemInfo();
    }

    @PostMapping("/task/stats")
    public Map<String, Object> getStats(@RequestBody JSONObject params) {
        if( !MongoConfigProperties.isEnabled() ){
            return new HashMap<>();
        }
        return MongoDBUtils.getStats(params);
    }
}
