# 工具类 API 参考

MD-Agent 提供了一系列内置工具类，可在 JavaScript 脚本中直接调用，简化开发工作。

## 使用说明

所有工具类都需要先导入才能使用：

```javascript
importClass(com.bsi.utils.ClassName);
```

## DataSourceUtils - 数据源工具类

### 路径
```
com.bsi.utils.DataSourceUtils
```

### 说明
用于获取数据源的全局参数配置。

### 方法列表

#### getProp(String dsId)

**参数：**
- `dsId` - 数据源ID或数据源名称ID

**返回值：**
- `Map<String,String>` - 数据源的全局参数配置

**示例：**
```javascript
importClass(com.bsi.utils.DataSourceUtils);

function input(context) {
    var dsProp = DataSourceUtils.getProp("101");
    var userName = dsProp["用户名"];
    var password = dsProp["密码"];

    log.info("数据源用户名: {}", userName);
}
```

---

## DateUtils - 日期工具类

### 路径
```
com.bsi.utils.DateUtils
```

### 说明
提供日期处理相关功能。

### 方法列表

#### nowDate(String pattern)

**参数：**
- `pattern` - 日期格式，例如 "yyyy-MM-dd HH:mm:ss"

**返回值：**
- `String` - 格式化后的当前时间字符串

**说明：**
获取指定日期格式的当前时间。

**示例：**
```javascript
var now = DateUtils.nowDate("yyyy-MM-dd HH:mm:ss");
// 输出：2026-01-14 10:30:00
```

#### preMinuteForNow(Long minute, String pattern)

**参数：**
- `minute` - 分钟数
- `pattern` - 日期格式

**返回值：**
- `String` - 格式化后的时间字符串

**说明：**
获取当前时间前多少分钟的时间。

**示例：**
```javascript
var fiveMinutesAgo = DateUtils.preMinuteForNow(5, "yyyy-MM-dd HH:mm:ss");
// 输出：2026-01-14 10:25:00（如果当前时间是10:30:00）
```

#### preSecondsForNow(Long second, String pattern)

**参数：**
- `second` - 秒数
- `pattern` - 日期格式

**返回值：**
- `String` - 格式化后的时间字符串

**说明：**
获取当前时间前多少秒的时间。

**示例：**
```javascript
var thirtySecondsAgo = DateUtils.preSecondsForNow(30, "yyyy-MM-dd HH:mm:ss");
```

#### getDateStrFromTime(long time, String pattern)

**参数：**
- `time` - 时间戳/毫秒数
- `pattern` - 日期格式

**返回值：**
- `String` - 格式化后的日期字符串

**说明：**
将毫秒数转换为指定格式的日期字符串。

**示例：**
```javascript
var dateStr = DateUtils.getDateStrFromTime(1662371384200, "yyyy-MM-dd HH:mm:ss");
// 输出：2022-09-05 15:23:04
```

### 完整示例

```javascript
importClass(com.bsi.utils.DateUtils);

function transform(context, data) {
    var now = DateUtils.nowDate("yyyy-MM-dd HH:mm:ss");
    var fiveMinutesAgo = DateUtils.preMinuteForNow(5, "yyyy-MM-dd HH:mm:ss");
    var thirtySecondsAgo = DateUtils.preSecondsForNow(30, "yyyy-MM-dd HH:mm:ss");
    var timestamp = DateUtils.getDateStrFromTime(1662371384200, "yyyy-MM-dd HH:mm:ss");

    log.info("当前时间: {}", now);
    log.info("5分钟前: {}", fiveMinutesAgo);
    log.info("30秒前: {}", thirtySecondsAgo);
    log.info("时间戳转换: {}", timestamp);

    return data;
}
```

---

## DBUtils - 数据库工具类

### 路径
```
com.bsi.utils.DBUtils
```

### 说明
执行数据库SQL语句操作。

### 方法列表

#### queryForList(String sql, Object[] args, String dataSourceId)

**参数：**
- `sql` - SQL查询语句
- `args` - SQL参数数组
- `dataSourceId` - 数据源ID

**返回值：**
- `List` - 查询结果集合

**说明：**
根据SQL语句查询指定数据源的数据，返回集合。

**示例：**
```javascript
importClass(com.bsi.utils.DBUtils);

function input(context) {
    var sql = "SELECT id, name FROM t_item WHERE enable = ?";
    var params = [];
    params.push("y");

    var results = DBUtils.queryForList(sql, params, "101");

    log.info("查询到 {} 条记录", results.size());

    for (var i = 0; i < results.size(); i++) {
        var row = results.get(i);
        log.info("ID: {}, Name: {}", row.id, row.name);
    }

    return results;
}
```

#### queryForObject(String sql, Object[] args, String dataSourceId)

**参数：**
- `sql` - SQL查询语句
- `args` - SQL参数数组
- `dataSourceId` - 数据源ID

**返回值：**
- `Map` - 单条数据记录

**说明：**
根据SQL语句查询指定数据源的数据，返回单条记录。

**示例：**
```javascript
var sql = "SELECT COUNT(*) as total FROM users WHERE status = ?";
var params = ["active"];
var result = DBUtils.queryForObject(sql, params, "101");

log.info("活跃用户数: {}", result.total);
```

#### execute(String sql, Object[] args, String dataSourceId)

**参数：**
- `sql` - SQL执行语句（INSERT、UPDATE、DELETE）
- `args` - SQL参数数组
- `dataSourceId` - 数据源ID

**返回值：**
- `int` - 影响的行数

**说明：**
执行插入、更新、删除操作，返回成功行数。

**示例：**
```javascript
// 插入数据
var insertSql = "INSERT INTO users (name, email) VALUES (?, ?)";
var insertParams = ["张三", "zhangsan@example.com"];
var insertCount = DBUtils.execute(insertSql, insertParams, "101");
log.info("插入了 {} 条记录", insertCount);

// 更新数据
var updateSql = "UPDATE users SET status = ? WHERE id = ?";
var updateParams = ["inactive", 123];
var updateCount = DBUtils.execute(updateSql, updateParams, "101");
log.info("更新了 {} 条记录", updateCount);

// 删除数据
var deleteSql = "DELETE FROM users WHERE status = ?";
var deleteParams = ["deleted"];
var deleteCount = DBUtils.execute(deleteSql, deleteParams, "101");
log.info("删除了 {} 条记录", deleteCount);
```

---

## FileUtils - 文件工具类

### 路径
```
com.bsi.utils.FileUtils
```

### 说明
处理文件读写操作。

### 方法列表

#### readFile(String path)

**参数：**
- `path` - 文件路径

**返回值：**
- `List<String>` - 文件内容行列表

**说明：**
读取指定路径的文件内容。

**示例：**
```javascript
importClass(com.bsi.utils.FileUtils);

function input(context) {
    var content = FileUtils.readFile("/usr/local/test.txt");

    for (var i = 0; i < content.size(); i++) {
        log.info("第{}行: {}", i + 1, content.get(i));
    }

    return content;
}
```

#### writeFile(String path, String msg, boolean append)

**参数：**
- `path` - 文件路径
- `msg` - 写入内容
- `append` - 是否追加（true=追加，false=覆盖）

**返回值：**
- `boolean` - 是否成功

**说明：**
写入数据到指定路径的文件中，可选择覆盖或追加。

**示例：**
```javascript
// 覆盖写入
FileUtils.writeFile("/usr/local/output.txt", "Hello World", false);

// 追加写入
FileUtils.writeFile("/usr/local/log.txt", "New log entry\n", true);
```

---

## HttpRequestUtils - HTTP请求工具类

### 路径
```
com.bsi.utils.HttpRequestUtils
```

### 说明
用于获取传入的请求信息（在API上报模式下使用）。

### 方法列表

#### getQueryParam()

**返回值：**
- `String` - 请求路径中的查询参数

**说明：**
获取请求路径中的参数。

**示例：**
```javascript
importClass(com.bsi.utils.HttpRequestUtils);

function input(context) {
    // 请求URL: /api/data?userId=123&type=active
    var queryParam = HttpRequestUtils.getQueryParam();
    log.info("查询参数: {}", queryParam);
    // 输出：userId=123&type=active
}
```

#### getRequestHeaders()

**返回值：**
- `Map<String,String>` - 请求头集合

**说明：**
获取请求中的请求头。

**示例：**
```javascript
var headers = HttpRequestUtils.getRequestHeaders();
var contentType = headers["Content-Type"];
var authorization = headers["Authorization"];

log.info("Content-Type: {}", contentType);
log.info("Authorization: {}", authorization);
```

#### getRequestBody()

**返回值：**
- `String` - 请求体内容

**说明：**
获取请求体。

**示例：**
```javascript
var body = HttpRequestUtils.getRequestBody();
var data = JSON.parse(body);

log.info("接收到的数据: {}", JSON.stringify(data));
```

### 完整示例

```javascript
importClass(com.bsi.utils.HttpRequestUtils);

function input(context) {
    // 获取查询参数
    var queryParam = HttpRequestUtils.getQueryParam();

    // 获取请求头
    var headers = HttpRequestUtils.getRequestHeaders();
    var contentType = headers["Content-Type"];

    // 获取请求体
    var body = HttpRequestUtils.getRequestBody();
    var requestData = JSON.parse(body);

    log.info("查询参数: {}", queryParam);
    log.info("Content-Type: {}", contentType);
    log.info("请求数据: {}", JSON.stringify(requestData));

    return [requestData];
}
```

---

## HttpUtils - HTTP客户端工具类

### 路径
```
com.bsi.utils.HttpUtils
```

### 说明
用于HTTP接口调用。

### AgHttpResult 对象

**属性：**
- `code` - 调用接口返回状态码
- `result` - 调用接口返回结果字符串

### 方法列表

#### post(String url, Map<String,String> headers, String body)

**参数：**
- `url` - 请求地址
- `headers` - 请求头
- `body` - 请求体

**返回值：**
- `AgHttpResult` - 响应结果对象

**说明：**
通过POST方式调用HTTP接口。

**示例：**
```javascript
importClass(com.bsi.utils.HttpUtils);

function output(context, data) {
    var url = "http://192.168.1.1:8080/api/token";
    var param = {
        "key": "lyd2022kFksiwlskh",
        "secret": "QIWjjFNsuWSKH0"
    };

    var result = HttpUtils.post(url, null, JSON.stringify(param));

    log.info("调用接口返回code: {}, result: {}", result.code, result.result);

    if (result.code == 200) {
        var response = JSON.parse(result.result);
        log.info("获取token成功: {}", response.token);
    }
}
```

#### request(String method, String url, Map<String,String> headers, String body)

**参数：**
- `method` - 请求方法（POST、GET、PUT、DELETE等）
- `url` - 请求地址
- `headers` - 请求头
- `body` - 请求体

**返回值：**
- `AgHttpResult` - 响应结果对象

**说明：**
通过指定方法调用HTTP接口。

**示例：**
```javascript
var headers = {
    "Content-Type": "application/json",
    "Authorization": "Bearer token123"
};

var body = JSON.stringify({
    "userId": 123,
    "action": "update"
});

// GET 请求
var getResult = HttpUtils.request("GET", "http://api.example.com/users/123", headers, null);

// POST 请求
var postResult = HttpUtils.request("POST", "http://api.example.com/users", headers, body);

// PUT 请求
var putResult = HttpUtils.request("PUT", "http://api.example.com/users/123", headers, body);

// DELETE 请求
var deleteResult = HttpUtils.request("DELETE", "http://api.example.com/users/123", headers, null);
```

---

## JSONUtils - JSON工具类

### 路径
```
com.bsi.utils.JSONUtils
```

### 说明
处理JSON数据转换。

### 方法列表

#### toJson(Object obj)

**参数：**
- `obj` - 需要转为JSON的对象

**返回值：**
- `String` - JSON字符串

**说明：**
将对象转换为JSON字符串。

**示例：**
```javascript
importClass(com.bsi.utils.JSONUtils);

var user = {
    "name": "张三",
    "age": 25,
    "email": "zhangsan@example.com"
};

var jsonStr = JSONUtils.toJson(user);
log.info("JSON字符串: {}", jsonStr);
```

#### parseObject(String text)

**参数：**
- `text` - 需要转为JSON对象的字符串

**返回值：**
- `Map` - JSON对象

**说明：**
将字符串转换为JSON对象。

**示例：**
```javascript
var jsonStr = '{"name":"张三","age":25}';
var obj = JSONUtils.parseObject(jsonStr);

log.info("姓名: {}", obj.name);
log.info("年龄: {}", obj.age);
```

---

## SapRFCUtils - SAP RFC工具类

### 路径
```
com.bsi.utils.SapRFCUtils
```

### 说明
用于调用SAP RFC接口。

### 方法列表

#### execute(String functionName, Map<String,Object> params, String dataSourceId)

**参数：**
- `functionName` - 函数名称
- `params` - 参数
- `dataSourceId` - 数据源ID

**返回值：**
- `Object` - 执行结果

**说明：**
调用指定名称的SAP RFC接口。

**示例：**
```javascript
importClass(com.bsi.utils.SapRFCUtils);

function output(context, data) {
    var params = [{
        "MSGID": "00505690AAD81EDD83F5D5AC7D95E0ED",
        "ZMOGR": "MO20220804001",
        "AUFNR": "CX17202004"
    }];

    var result = SapRFCUtils.execute("ZF_PP_GOODSMVT_BORROWING", params, "129");

    log.info("SAP RFC调用结果: {}", JSON.stringify(result));
}
```

---

## XmlUtils - XML工具类

### 路径
```
com.bsi.utils.XmlUtils
```

### 说明
处理XML数据转换。

### 方法列表

#### json2Xml(String json, String interceptNode)

**参数：**
- `json` - JSON字符串
- `interceptNode` - XML的根节点名称

**返回值：**
- `String` - XML字符串

**说明：**
将JSON转换为XML格式。

**示例：**
```javascript
importClass(com.bsi.utils.XmlUtils);

function transform(context, data) {
    var jsonData = {
        "ufinterface": {
            "bill": {
                "bill-attr": {
                    "id": "1001ZZ1000000003RRFK"
                },
                "billhead": {
                    "pk_group": "00",
                    "pk_org": "00",
                    "code": "0911",
                    "name": "部门1021",
                    "shortname": "1021"
                }
            }
        }
    };

    var xml = XmlUtils.json2Xml(JSON.stringify(jsonData), "ufinterface");

    log.info("转换后的XML: {}", xml);

    return xml;
}
```

---

## 字符串处理工具

### 提取中文

```javascript
function GetChinese(strValue) {
    if (strValue != null && strValue != "") {
        var reg = /[\u4e00-\u9fa5]/g;
        return strValue.match(reg).join("");
    } else {
        return "";
    }
}

// 示例
var text = "Hello世界123";
var chinese = GetChinese(text);
log.info("提取的中文: {}", chinese);  // 输出：世界
```

### 去除中文

```javascript
function RemoveChinese(strValue) {
    if (strValue != null && strValue != "") {
        var reg = /[\u4e00-\u9fa5]/g;
        return strValue.replace(reg, "");
    } else {
        return "";
    }
}

// 示例
var text = "Hello世界123";
var nonChinese = RemoveChinese(text);
log.info("去除中文后: {}", nonChinese);  // 输出：Hello123
```

---

## 下一步

- 查看 [脚本开发指南](../guides/script-development.md)
- 了解 [数据库同步示例](../examples/database-sync.md)
- 参考 [REST API 文档](rest-api.md)
