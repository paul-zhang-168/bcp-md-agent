# 快速入门

本指南将帮助你在 5 分钟内完成第一个数据集成任务。

## 前置条件

- 已安装 MD-Agent（参考 [安装指南](installation.md)）
- 已登录 MD-Agent 管理平台
- 准备好源数据库和目标数据库的连接信息

## 数据集成快速入门

### 步骤 1：创建数据源

#### 1.1 登录管理平台

访问：`http://localhost:8080`（默认地址）

#### 1.2 创建源数据源

1. 点击左侧菜单「集成建模」→「数据源管理」
2. 点击页面右上角「新增」按钮
3. 填写数据源信息：

```
名称：MySQL-Source
类型：MySQL
URL：jdbc:mysql://localhost:3306/source_db
用户名：reader
密码：******
```

4. 点击「测试连接」验证
5. 点击「确定」保存

#### 1.3 创建目标数据源

重复上述步骤，创建目标数据源：

```
名称：PostgreSQL-Target
类型：PostgreSQL
URL：jdbc:postgresql://localhost:5432/target_db
用户名：writer
密码：******
```

### 步骤 2：创建集成任务

#### 2.1 新建任务

1. 点击左侧菜单「集成建模」→「集成任务管理」
2. 点击右上角「新增」按钮
3. 填写任务基本信息：

```
任务名称：用户数据同步
任务描述：从 MySQL 同步用户数据到 PostgreSQL
```

### 步骤 3：配置节点

#### 3.1 输入节点配置

1. 在任务编辑页面，点击「输入节点」→「配置」
2. 填写配置：

**数据源：** 选择 `MySQL-Source`

**脚本内容：**
```javascript
function input(context) {
    importClass(com.bsi.utils.DBUtils);

    var sql = "SELECT id, username, email, created_at FROM users WHERE updated_at > ?";
    var params = [context.getTaskTs()];  // 获取上次执行时间

    var dataList = DBUtils.queryForList(sql, params, context.inputConf().dataSource);

    log.info("查询到 {} 条数据", dataList.size());

    return dataList;
}

input(context);
```

3. 点击「保存」

#### 3.2 转换节点配置

1. 点击「转换节点」→「配置」
2. 填写转换脚本：

```javascript
function transform(context, data) {
    var results = [];

    for (var i = 0; i < data.size(); i++) {
        var source = data.get(i);

        var target = {
            user_id: source.id,
            user_name: source.username,
            email_address: source.email,
            created_date: source.created_at
        };

        results.push(target);
    }

    log.info("转换了 {} 条数据", results.length);

    return results;
}

transform(context, data);
```

3. 点击「保存」

#### 3.3 输出节点配置

1. 点击「输出节点」→「配置」
2. 填写配置：

**数据源：** 选择 `PostgreSQL-Target`

**脚本内容：**
```javascript
function output(context, data) {
    importClass(com.bsi.utils.DBUtils);

    var successCount = 0;

    for (var i = 0; i < data.length; i++) {
        var row = data[i];

        var sql = "INSERT INTO dim_users (user_id, user_name, email_address, created_date) " +
                  "VALUES (?, ?, ?, ?) " +
                  "ON CONFLICT (user_id) DO UPDATE SET " +
                  "user_name = EXCLUDED.user_name, " +
                  "email_address = EXCLUDED.email_address";

        var params = [row.user_id, row.user_name, row.email_address, row.created_date];

        try {
            DBUtils.execute(sql, params, context.outputConf().dataSource);
            successCount++;
        } catch (e) {
            log.error("插入失败: {}", e.message);
        }
    }

    log.info("成功插入 {} 条数据", successCount);

    context.getResultLog().setValidSize(data.length);
    context.getResultLog().setSuccessSize(successCount);
}

output(context, data);
```

3. 点击「保存」

### 步骤 4：配置定时任务

1. 在任务编辑页面，找到「调度配置」部分
2. 选择「定时任务」
3. 配置 Cron 表达式：

```
0 */5 * * * ?    # 每5分钟执行一次
```

或使用可视化配置器：
- 访问：https://cron.qqe2.com/
- 选择执行频率
- 复制生成的 Cron 表达式

4. 点击「保存」

### 步骤 5：启动任务

1. 点击任务列表中的「启动」按钮
2. 任务开始按照设定的时间执行

### 步骤 6：查看执行结果

#### 6.1 查看执行历史

1. 点击任务名称进入详情页
2. 切换到「执行历史」标签
3. 查看每次执行的状态、时间、处理数量

#### 6.2 查看日志

1. 点击执行记录的「日志」按钮
2. 或使用命令行查看：

```bash
cd /bcp/logs/task
tail -f task-用户数据同步-*.log
```

#### 6.3 手动触发

如果想立即执行，可以：
1. 点击任务列表中的「执行」按钮
2. 或使用 API：

```bash
curl -X POST http://localhost:8080/api/task/{taskId}/execute
```

## API 集成快速入门

### 步骤 1：创建 API 上报数据源

1. 创建数据源，类型选择「API 上报」
2. 配置：

```
名称：BCP-Gateway
类型：API 上报
协议：http
是否验证：是
访问ID：your_api_key
访问密钥：your_api_secret
```

### 步骤 2：创建 API 任务

1. 创建新任务
2. 输入节点类型：选择「API 上报」
3. 数据源：选择「BCP-Gateway」
4. API 路径：`/api/data/process`

### 步骤 3：配置脚本

**输入节点：**
```javascript
function input(context) {
    importClass(com.bsi.utils.HttpRequestUtils);

    // 获取请求体
    var body = HttpRequestUtils.getRequestBody();
    var data = JSON.parse(body);

    log.info("接收到数据: {}", JSON.stringify(data));

    return [data];
}

input(context);
```

**转换节点：**
```javascript
function transform(context, data) {
    var item = data[0];

    // 数据处理逻辑
    var result = {
        id: item.id,
        processedAt: new Date().toISOString(),
        status: "processed"
    };

    return result;
}

transform(context, data);
```

**输出节点：**
```javascript
function output(context, data) {
    log.info("处理完成: {}", JSON.stringify(data));

    // 返回响应（可选）
    return {
        code: 200,
        message: "success",
        data: data
    };
}

output(context, data);
```

### 步骤 4：调用 API

```bash
# 获取 Token
curl -X POST http://localhost:8080/api/token \
  -H "Content-Type: application/json" \
  -d '{
    "key": "your_api_key",
    "secret": "your_api_secret"
  }'

# 调用业务 API
curl -X POST http://localhost:8080/api/data/process \
  -H "Content-Type: application/json" \
  -H "Authorization: <返回的token>" \
  -d '{
    "id": 123,
    "name": "test"
  }'
```

## 常见问题

### 1. 数据源连接失败

**检查项：**
- 数据库是否运行
- 网络是否通畅
- 用户名密码是否正确
- 防火墙设置

### 2. 任务执行失败

**排查步骤：**
1. 查看日志文件
2. 检查脚本语法
3. 验证数据源配置
4. 测试 SQL 语句

### 3. 脚本语法错误

**常见错误：**
```javascript
// 错误：使用了 ES6 语法
const data = getData();  // ❌

// 正确：使用 ES5 语法
var data = getData();    // ✓
```

### 4. 数据未同步

**检查：**
- 查询条件是否正确
- 时间戳字段是否更新
- 定时任务是否启动
- 查看执行历史

## 下一步

- 学习 [脚本开发进阶](script-development.md)
- 了解 [数据源配置](datasource-config.md)
- 查看 [完整示例](../examples/database-sync.md)
- 阅读 [工具类 API](../api/utility-classes.md)

## 获取帮助

- [GitHub Issues](https://github.com/paul-zhang-168/bcp-md-agent/issues)
- [GitHub Discussions](https://github.com/paul-zhang-168/bcp-md-agent/discussions)
- [故障排查指南](troubleshooting.md)
