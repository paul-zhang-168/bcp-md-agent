package com.bsi.md.agent.utils;

import com.bsi.framework.core.httpclient.common.HttpConfig;
import com.bsi.framework.core.httpclient.common.HttpHeader;
import com.bsi.framework.core.httpclient.common.HttpMethods;
import com.bsi.framework.core.httpclient.common.HttpResult;
import com.bsi.framework.core.httpclient.utils.HttpClientUtil;
import com.bsi.framework.core.utils.ExceptionUtils;
import com.bsi.framework.core.utils.StringUtils;
import com.bsi.md.agent.datasource.AgApiTemplate;
import com.bsi.md.agent.entity.dto.AgHttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;

/**
 * token工具类
 * @author fish
 */
@Slf4j
public class AgTokenUtils {

    /**
     * 获取token
     * @param tokenUrl
     * @param params
     * @return
     */
    public static AgHttpResult getToken(String tokenUrl,String params,String method){
        HttpConfig config = HttpConfig.simpleCustom(50000).method(HttpMethods.valueOf(method)).url( tokenUrl );
        if( StringUtils.hasText( params ) ){
            config.json( params );
        }
        //设置参数和请求头
        config.headers( HttpHeader.custom().contentType(ContentType.APPLICATION_JSON.toString()).build() );
        AgHttpResult rs = new AgHttpResult();
        try{
            HttpResult result = HttpClientUtil.sendAndGetResp( config,false );
            rs.setCode( result.getStatusCode() );
            rs.setResp( result.getResp() );
            rs.setHeader( result.getRespHeaders() );
            rs.setResult( result.getResult() );
        }catch (Exception e){
            log.error("获取token报错,错误信息：{}", ExceptionUtils.getFullStackTrace(e));
            rs.setCode(500);
            rs.setResult("获取token失败");
        }
        return rs;
    }

    /**
     * 根据数据源id获取token
     * @param apiTemplate
     * @return AgHttpResult
     */
    public static AgHttpResult getToken(AgApiTemplate apiTemplate){
        return AgTokenUtils.getToken(apiTemplate.getAuthUrl(),apiTemplate.getAuthParam(),apiTemplate.getAuthMethod());
    }
}
