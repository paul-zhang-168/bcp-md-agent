# 监控和日志

本指南介绍 MD-Agent 的日志管理、监控配置和可观测性最佳实践。

## 日志管理

### 日志目录结构

MD-Agent 使用分类日志存储结构：

```
/bcp/logs/
├── task/           # 任务执行日志
│   ├── task-任务名称-任务ID.log
│   ├── task-任务名称-任务ID-repair.log  # 手动补数据日志
│   └── backup/     # 历史日志压缩文件（默认保留7天）
├── info/           # 系统信息日志
│   └── info.log
└── error/          # 错误日志
    └── error.log
```

**IoT Edge 环境：**
```
/var/IoTEdge/log/sys_industry_dc_bsi/logs/
```

### 日志类型

#### 1. 任务日志（task/）

每个任务都会在 task 目录下生成独立的日志文件：

**命名规则：**
- 正常执行：`task-任务名称-任务ID.log`
- 补数据：`task-任务名称-任务ID-repair.log`

**示例：**
```
task-数据同步任务-763.log
task-MES2ERP产品主数据-763.log
task-订单同步-123-repair.log
```

#### 2. 信息日志（info/）

记录系统运行的关键信息：
- 任务启动和完成
- 数据同步成功
- 配置更新

#### 3. 错误日志（error/）

记录系统异常和错误：
- 任务执行失败
- 数据库连接错误
- API调用异常

### 日志查看方式

#### 方式一：FinalShell（推荐）

1. **安装 FinalShell**
   - Windows: http://www.hostbuf.com/downloads/finalshell_install.exe
   - macOS: http://www.hostbuf.com/downloads/finalshell_install.pkg

2. **连接服务器**
   - 打开 FinalShell
   - 新建 SSH 连接
   - 输入服务器 IP、端口、用户名、密码

3. **查看日志**
   ```bash
   cd /var/IoTEdge/log/sys_industry_dc_bsi/logs/task
   ls  # 列出所有日志文件
   ```

4. **使用文本编辑器查看**
   - 右键点击日志文件
   - 选择"打开"或"下载"
   - 使用 Ctrl + F 全文搜索

#### 方式二：命令行

```bash
# 进入日志目录
cd /var/IoTEdge/log/sys_industry_dc_bsi/logs/task

# 查看最近100行
tail -n 100 task-MES2ERP产品主数据-763.log

# 实时查看日志
tail -f task-数据同步任务-763.log

# 查看错误日志
grep "ERROR" task-数据同步任务-763.log

# 按时间范围查看
sed -n '/2026-01-14 10:00/,/2026-01-14 11:00/p' task-数据同步任务-763.log
```

### 日志配置

在 `application.yml` 中配置日志级别：

```yaml
logging:
  level:
    root: INFO
    com.bsi.md.agent: DEBUG
  file:
    name: logs/md-agent.log
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 日志归档

默认保留 7 天的日志，超过7天的日志会被压缩归档到 `backup/` 目录。

**手动清理日志：**
```bash
# 删除7天前的日志
find /bcp/logs/task -name "*.log" -mtime +7 -delete

# 压缩并归档
tar -czf backup/logs-$(date +%Y%m%d).tar.gz *.log
```

## 脚本中的日志记录

### 基本用法

```javascript
// 信息日志
log.info("任务开始执行");
log.info("处理了 {} 条数据", dataList.length);

// 错误日志
log.error("调用接口异常，错误信息: {}", e.message);

// 调试日志
log.debug("调试信息: {}", JSON.stringify(data));

// 警告日志
log.warn("数据格式可能有问题: {}", data.field);
```

### 日志最佳实践

#### 1. 合理使用日志级别

```javascript
function processData(data) {
    // 重要信息使用 INFO
    log.info("开始处理数据，共 {} 条", data.length);

    // 调试信息使用 DEBUG
    log.debug("数据详情: {}", JSON.stringify(data));

    // 异常使用 ERROR
    try {
        // 处理逻辑
    } catch (e) {
        log.error("处理失败: {}", e.message);
    }

    // 完成使用 INFO
    log.info("数据处理完成");
}
```

#### 2. 包含上下文信息

```javascript
log.info("任务ID: {}, 任务名称: {}, 处理数量: {}",
    context.getTaskId(),
    context.getTaskName(),
    dataList.length
);
```

#### 3. 错误处理

```javascript
try {
    var result = HttpUtils.post(url, headers, body);

    if (result.code != 200) {
        log.error("API调用失败 - URL: {}, Code: {}, Response: {}",
            url, result.code, result.result);

        // 记录错误数据
        var errorData = {
            "taskName": context.getTaskName(),
            "execTime": DateUtils.nowDate("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            "errorMsg": "接口调用失败: " + result.result,
            "requestBody": body
        };
        context.setErrorData(JSON.stringify(errorData));
    }
} catch (e) {
    log.error("异常: {}", e.message);
    throw e;
}
```

## 可观测性（Metrics）

MD-Agent 支持 Metrics 指标，可与 Promtail + Loki + Grafana 集成实现可视化监控。

### 指标类型

#### 1. 性能指标

```javascript
// 输入节点耗时
logMetric("input.costMs", Date.now() - startTime);

// 转换节点耗时
logMetric("transform.costMs", Date.now() - startTime);

// 输出节点耗时
logMetric("output.costMs", Date.now() - startTime);
```

#### 2. 业务指标

```javascript
// 记录处理数量
logMetric("input.recordCount", dataList.length);

// 记录BOM数量
logMetric("transform.bomCount", bomList.length);

// 记录成功数量
logMetric("output.successCount", successCount);
```

#### 3. 事件记录

```javascript
// 输入完成事件
logEvent("input.finish", {
    count: dataList.length,
    traceId: traceId
});

// 转换完成事件
logEvent("transform.finish", {
    bom: bomList.length,
    traceId: traceId
});

// 输出完成事件
logEvent("output.finish", {
    size: dataList.length,
    traceId: traceId
});
```

#### 4. 错误记录

```javascript
// 输入失败
logError("input.fail", {
    message: e.message,
    traceId: traceId
});

// 转换失败
logError("transform.fail", {
    error: e.message,
    traceId: traceId
});

// API调用失败
logError("lixiang.apiFail", {
    error: errorMsg,
    apiPath: context.outputConf().path,
    traceId: traceId
});
```

### 完整示例

```javascript
importClass(com.bsi.utils.HttpUtils);
importClass(com.bsi.utils.DateUtils);

function input(context) {
    var start = Date.now();
    log.info("1.开始输入节点...");

    // 生成 Trace ID
    var traceId = genTraceId();
    context.env.put("traceId", traceId);
    log.info("trace_id={}", traceId);

    try {
        // 业务逻辑
        var dataList = fetchData();

        if (!dataList || dataList.length === 0) {
            log.info("无待同步数据");
            logMetric("input.recordCount", 0);
            logMetric("input.costMs", Date.now() - start);
            return null;
        }

        log.info("获取 {} 条记录", dataList.length);

        // 记录指标
        logMetric("input.recordCount", dataList.length);
        logMetric("input.costMs", Date.now() - start);
        logEvent("input.finish", {
            count: dataList.length,
            traceId: traceId
        });

        return dataList;

    } catch (e) {
        logError("input.fail", {
            message: e.message || e,
            traceId: traceId
        });
        return null;
    }
}

// 生成Trace ID
function genTraceId() {
    return "trace-" + Date.now() + "-" + Math.random().toString(36).substring(7);
}
```

### 在 Grafana 中可视化

通过 Loki 查询语言，可以在 Grafana 中创建仪表板：

```promql
# 查询性能指标
{job="md-agent"} |= "METRIC|input.costMs"

# 查询错误
{job="md-agent"} |= "ERROR|input.fail"

# 查询事件
{job="md-agent"} |= "EVENT|input.finish"
```

## 告警配置

### 配置告警方式

1. **邮件告警**

```yaml
ag:
  email:
    enabled: true
    host: smtp.example.com
    port: 587
    username: alert@example.com
    password: ${EMAIL_PASSWORD}
    from: alert@example.com
```

2. **飞书机器人告警**

在任务配置中设置告警参数：

```javascript
// 在输出节点设置错误数据
var errorData = {
    "taskName": "数据同步任务",
    "execTime": DateUtils.nowDate("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    "errorMsg": "接口调用失败：" + error
};
context.setErrorData(JSON.stringify(errorData));
```

### 告警触发条件

- 任务执行失败
- 错误数据超过阈值
- API调用异常
- 数据库连接失败

## 性能监控

### JVM 监控

```bash
# 查看GC情况
jstat -gc <pid> 1000

# 生成堆转储
jmap -dump:format=b,file=heap.bin <pid>

# 线程转储
jstack <pid> > thread-dump.txt
```

### Docker 日志

```bash
# 查看容器日志
docker logs -f md-agent-container

# 查看日志位置
docker inspect md-agent-container | grep LogPath

# 清理日志
echo "" > /var/lib/docker/containers/<容器ID>/<容器ID>-json.log
```

## 下一步

- 查看 [故障排查指南](troubleshooting.md)
- 了解 [部署指南](deployment.md)
- 参考 [性能优化](performance.md)
