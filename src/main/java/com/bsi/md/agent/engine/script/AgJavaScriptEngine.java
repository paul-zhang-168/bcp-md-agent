package com.bsi.md.agent.engine.script;

import com.bsi.framework.core.utils.ExceptionUtils;
import com.bsi.framework.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;

/**
 * js脚本引擎接口
 * @author fish
 */

public class AgJavaScriptEngine implements AgScriptEngine{

    private static Logger info_log = LoggerFactory.getLogger("TASK_INFO_LOG");

    private static AgJavaScriptEngine instance = null;

    private ScriptEngine engine;

    /**
     * 返回单例
     *
     * @return
     */
    public static AgJavaScriptEngine getInstance() {
        if (instance == null)
            instance = new AgJavaScriptEngine();
        return instance;
    }

    /**
     * 无参构造器 初始化需要的js引擎
     *
     */
    private AgJavaScriptEngine() {
        try {
            //调用Java8 nashorn 运行JavaScript脚本
            this.engine = new ScriptEngineManager().getEngineByName("nashorn");
            ScriptContext sc = new SimpleScriptContext();
            Bindings bindings = new SimpleBindings();
            bindings.put("log", info_log); // 向nashorn引擎注入logger对象
            sc.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
            sc.getBindings(ScriptContext.ENGINE_SCOPE).putAll(bindings);
            engine.setBindings(sc.getBindings(ScriptContext.ENGINE_SCOPE), ScriptContext.ENGINE_SCOPE);
            //支持importClass
            engine.eval("load('nashorn:mozilla_compat.js')");
        }catch (Exception e){
            info_log.error("javaScript引擎初始化失败:{}", ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("js脚本初始化失败");
        }

    }
    /**
     * 执行方法
     * @param script
     * @return
     */
    public Object execute(String script,String method,Object[] args) throws Exception{
        Object result = eval(script);
        if( StringUtils.hasText(method) ){
            Invocable invocable = (Invocable) engine;
//            log.info("args:{}", JSON.toJSONString(args));
            result = invocable.invokeFunction(method,args);
        }
        return result;
    }

    public Object eval(String script) throws Exception{
        return engine.eval(script);
    }

//    public static void main(String[] arr) throws Exception{
//        String script ="importClass(com.bsi.utils.HttpUtils);\n" +
//                "importClass(com.bsi.utils.JSONUtils);\n" +
//                "//输入节点执行脚本\n" +
//                "function input(){\n" +
//                "   //输入代码\n" +
//                "   var tokenUrl = \"https://narwal2.test.ik3cloud.com/k3cloud/Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc\";\n" +
//                "   var tokenParam = \"{\\\"acctID\\\":\\\"60812474cbc380\\\",\\\"username\\\":\\\"srm01\\\",\\\"password\\\":\\\"Narwal@2021\\\",\\\"lcid\\\":\\\"2052\\\"}\";\n" +
//                "   var headers = {\"Content-Type\":\"application/json\"};\n" +
//                "   var supplierUrl = \"https://narwal2.test.ik3cloud.com/k3cloud/Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc\";\n" +
//                "   var supplierParam = \"{\\\"data\\\":{\\\"FormId\\\":\\\"BD_Supplier\\\",\\\"FieldKeys\\\":\\\"FSupplierId,FNumber,FName,FCreateDate,FModifyDate,FForbidStatus\\\",\\\"FilterString\\\":\\\"FNumber='YJ0178' and FUseOrgId=1 \\\",\\\"OrderString\\\":\\\"\\\",\\\"TopRowCount\\\":0,\\\"StartRow\\\":0,\\\"Limit\\\":0}}\";\n" +
//                "   var result = HttpUtils.request(\"POST\", tokenUrl, headers,tokenParam);\n" +
//                "   print(JSONUtils.toJson(result));\n" +
//                "   var cookies = result.getHeader(\"set-cookie\");\n" +
//                "   //Map<String,String> cookieMap = ApiEgStringUtils.splitToMap(cookies,\";\",\"=\");\n" +
//                "   //var token = cookieMap.get(\"kdservice-sessionid\");\n" +
//                "   var token = cookies.value.substring(cookies.value.indexOf(\"=\")+1,cookies.value.indexOf(\";\"))\n" +
//                "   print(token);\n" +
//                "   headers[\"kdservice-sessionid\"] = token;\n" +
//                "   print(JSON.stringify(headers))\n" +
//                "   var r1 =  HttpUtils.request(\"POST\", supplierUrl, headers, supplierParam);\n" +
//                "\n" +
//                "   var srmData = {\"header\":{\"applicationCode\":\"GOINGLINK_CLOUD_TEST\",\"applicationGroupCode\":\"PUBLIC_CLOUD\",\"batchNum\":\"9922\",\"externalSystemCode\":\"NARWAL_B90N2M31CL\",\"interfaceCode\":\"SSLM_SUPPLIER_IMP\",\"userName\":\"42102265\"},\"body\":[]};\n" +
//                "   //转换代码\n" +
//                "   JSONUtils.parseArray(r1.result).forEach(function(val,index) {\n" +
//                "      var obj = {\"esSupplierId\":val[0],\"esSupplierCode\":val[1],\"supplierName\":val[2],\"erpCreationDate\":val[3],\"erpLastUpdateDate\":val[4],\"enabledFlag\":'A'==val[5]?1:0};\n" +
//                "      srmData.body.push(obj);\n" +
//                "      print( JSON.stringify(obj) );\n" +
//                "   });\n" +
//                "   print(JSON.stringify(srmData));\n" +
//                "\n" +
//                "   //输出代码\n" +
//                "   var srmTokenUrl = \"https://roma.test.isrm.going-link.com/oauth/token?grant_type=client_credentials&client_id=srm-interface-client&client_secret=secret&scope=default\";\n" +
//                "   var srmSupplierUrl = \"https://roma.test.isrm.going-link.com/base/supplier/imp\";\n" +
//                "   headers = {\"Content-Type\":\"application/json\"};\n" +
//                "   var r2 = HttpUtils.request(\"POST\", srmTokenUrl, headers,\"\");\n" +
//                "   var tokenResult = JSONUtils.parseObject( r2.result );\n" +
//                "   headers[\"Authorization\"] = \"Bearer \"+ tokenResult[\"access_token\"];\n" +
//                "   print(JSON.stringify(headers))\n" +
//                "   var r3 =  HttpUtils.request(\"POST\", srmSupplierUrl, headers, JSON.stringify(srmData));\n" +
//                "   print(r3.result);\n" +
//                "   return r1.result;\n" +
//                "}";
////        Context c = new Context();
////        c.setEnv(new HashMap());
////        c.put("1","2");
////        String script = "importClass(com.bsi.utils.DBUtils);\n" +
////                "importClass(com.alibaba.fastjson.JSON);\n"+
////                "function input(a){\n" +
////                "   return log.info(a.getEnv().get('1'))" +
////                "}";
//        System.out.println(JSONUtils.toJson(AgJavaScriptEngine.getInstance().execute(script, "input", new Object[]{})) );
//    }
}
