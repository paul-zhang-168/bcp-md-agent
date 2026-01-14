# OData 查询规范

MD-Agent 的 API 接口协议支持 OData 标准，可以使用 OData 规范进行数据查询。本文档介绍查询中常用的操作符和语法。

## 什么是 OData

OData (Open Data Protocol) 是一个开放的数据访问协议，提供标准化的 REST API 查询语言。

## 查询参数

OData 查询主要涉及以下参数：

- `$filter` - 过滤条件
- `$orderby` - 排序
- `$select` - 字段选择
- `$top` 和 `$skip` - 分页
- `$count` - 计数

## 比较操作符

### 等于（eq）

判断字段值是否等于指定值。

**语法：**
```
$filter=<field> eq <value>
```

**示例：**
```http
GET /api/products?$filter=Age eq 30
GET /api/users?$filter=Name eq 'John'
GET /api/orders?$filter=Status eq 'completed'
```

### 不等于（ne）

判断字段值是否不等于指定值。

**示例：**
```http
GET /api/products?$filter=Age ne 30
GET /api/users?$filter=Status ne 'inactive'
```

### 大于（gt）

判断字段值是否大于指定值。

**示例：**
```http
GET /api/products?$filter=Age gt 30
GET /api/orders?$filter=Amount gt 1000
```

### 大于等于（ge）

判断字段值是否大于或等于指定值。

**示例：**
```http
GET /api/products?$filter=Age ge 30
GET /api/products?$filter=Price ge 100
```

### 小于（lt）

判断字段值是否小于指定值。

**示例：**
```http
GET /api/products?$filter=Age lt 30
GET /api/orders?$filter=Total lt 500
```

### 小于等于（le）

判断字段值是否小于或等于指定值。

**示例：**
```http
GET /api/products?$filter=Age le 30
GET /api/inventory?$filter=Stock le 10
```

## 逻辑操作符

### and

组合多个条件，所有条件都要为真。

**示例：**
```http
GET /api/products?$filter=Age gt 30 and Name eq 'John'
GET /api/orders?$filter=Status eq 'pending' and Amount gt 1000
```

### or

组合多个条件，任一条件为真即可。

**示例：**
```http
GET /api/products?$filter=Age gt 30 or Name eq 'John'
GET /api/users?$filter=Role eq 'admin' or Role eq 'manager'
```

### not

用于反转条件。

**示例：**
```http
GET /api/products?$filter=not(Age eq 30)
GET /api/users?$filter=not(Status eq 'inactive')
```

## 字符串函数

### startswith

判断字符串是否以特定子字符串开始。

**示例：**
```http
GET /api/employees?$filter=startswith(Name, 'J')
GET /api/products?$filter=startswith(Code, 'PRD')
```

### endswith

判断字符串是否以特定子字符串结束。

**示例：**
```http
GET /api/employees?$filter=endswith(Name, 'n')
GET /api/files?$filter=endswith(FileName, '.pdf')
```

### contains

判断字符串中是否包含特定子字符串。

**示例：**
```http
GET /api/employees?$filter=contains(Name, 'Jo')
GET /api/products?$filter=contains(Description, 'premium')
```

## 集合和枚举操作符

### in

判断一个值是否在指定的集合中。

**示例：**
```http
GET /api/products?$filter=Age in (20, 30, 40)
GET /api/users?$filter=Status in ('active', 'pending')
```

### any

用于检查集合属性是否包含满足条件的元素。

**示例：**
```http
GET /api/customers?$filter=Addresses/any(a: a/City eq 'London')
GET /api/orders?$filter=Items/any(i: i/Price gt 100)
```

### all

用于检查集合中的所有元素是否都满足条件。

**示例：**
```http
GET /api/customers?$filter=Addresses/all(a: a/City eq 'London')
GET /api/orders?$filter=Items/all(i: i/InStock eq true)
```

## 空值检查

### 检查空值

```http
GET /api/products?$filter=Age eq null
GET /api/users?$filter=Email eq null
```

### 检查非空

OData 没有直接的 `not null` 操作符，但可以使用 `ne null`：

```http
GET /api/products?$filter=Age ne null
GET /api/users?$filter=Email ne null
```

## 日期和时间

日期时间值通常需要以 `datetime'yyyy-MM-ddTHH:mm:ss'` 的格式表示。

**示例：**
```http
GET /api/orders?$filter=Birthday eq datetime'1990-01-01T00:00:00'
GET /api/events?$filter=StartDate gt datetime'2026-01-01T00:00:00'
GET /api/logs?$filter=CreatedAt ge datetime'2026-01-14T00:00:00'
```

## 数学和算术操作符

### add（加法）

```http
GET /api/products?$filter=Age add 5 gt 30
GET /api/orders?$filter=Total add Tax gt 1000
```

### sub（减法）

```http
GET /api/products?$filter=Age sub 5 gt 30
GET /api/inventory?$filter=Stock sub Reserved lt 10
```

### mul（乘法）

```http
GET /api/products?$filter=Age mul 2 gt 60
GET /api/orders?$filter=Quantity mul Price gt 1000
```

### div（除法）

```http
GET /api/products?$filter=Age div 2 gt 30
GET /api/metrics?$filter=Total div Count gt 50
```

### mod（取模）

```http
GET /api/products?$filter=Age mod 2 eq 0
GET /api/records?$filter=Id mod 10 eq 0
```

## 排序（$orderby）

### 升序

```http
GET /api/products?$orderby=ItemCode asc
GET /api/users?$orderby=Name asc
```

### 降序

```http
GET /api/products?$orderby=ItemCode desc
GET /api/orders?$orderby=CreatedDate desc
```

### 多字段排序

```http
GET /api/products?$orderby=ItemCode asc, UpdateDate desc
GET /api/users?$orderby=Department asc, Name asc
```

## 字段选择（$select）

只返回指定的字段。

**示例：**
```http
GET /api/users?$select=Id,Name,Email
GET /api/products?$select=Code,Name,Price
```

## 分页

### $top

限制返回的记录数量。

**示例：**
```http
GET /api/products?$top=10
GET /api/users?$top=20
```

### $skip

跳过指定数量的记录。

**示例：**
```http
GET /api/products?$skip=10
GET /api/users?$skip=20&$top=10  # 第3页，每页10条
```

### 分页组合

```http
# 第1页（0-9）
GET /api/products?$top=10&$skip=0

# 第2页（10-19）
GET /api/products?$top=10&$skip=10

# 第3页（20-29）
GET /api/products?$top=10&$skip=20
```

## 计数（$count）

获取符合条件的记录总数。

**示例：**
```http
GET /api/products?$count=true
GET /api/users?$filter=Status eq 'active'&$count=true
```

## 复杂查询示例

### 示例 1：组合查询

查询价格在20到100之间的产品：

```http
GET /api/products?$filter=Price gt 20 and Price lt 100
```

### 示例 2：字符串查询

查询姓名以"A"开头的员工：

```http
GET /api/employees?$filter=startswith(Name, 'A')
```

### 示例 3：日期查询

查询2020年1月1日之后的订单：

```http
GET /api/orders?$filter=OrderDate ge datetime'2020-01-01T00:00:00'
```

### 示例 4：集合查询

查询有超过100元订单的客户：

```http
GET /api/customers?$filter=Orders/any(o: o/Amount gt 100)
```

### 示例 5：完整查询

查询激活状态、价格大于50、按创建日期倒序排列、返回前10条、只显示部分字段：

```http
GET /api/products?$filter=Status eq 'active' and Price gt 50&$orderby=CreatedDate desc&$top=10&$select=Id,Name,Price
```

### 示例 6：分页和排序

第2页数据（每页20条），按更新时间倒序：

```http
GET /api/products?$orderby=UpdateDate desc&$skip=20&$top=20
```

## 在 JavaScript 中使用

在 MD-Agent 的输入节点脚本中，可以动态构建 OData 查询：

```javascript
function input(context) {
    importClass(com.bsi.utils.HttpRequestUtils);

    // 获取查询参数
    var queryParam = HttpRequestUtils.getQueryParam();

    // 解析 OData 参数
    var filter = getParameter(queryParam, "$filter");
    var orderby = getParameter(queryParam, "$orderby");
    var top = getParameter(queryParam, "$top");
    var skip = getParameter(queryParam, "$skip");

    // 构建 SQL
    var sql = "SELECT * FROM products WHERE 1=1";

    if (filter) {
        sql += " AND " + convertODataFilter(filter);
    }

    if (orderby) {
        sql += " ORDER BY " + convertODataOrderBy(orderby);
    }

    if (top) {
        sql += " LIMIT " + top;
    }

    if (skip) {
        sql += " OFFSET " + skip;
    }

    log.info("执行SQL: {}", sql);

    // 查询数据
    var results = DBUtils.queryForList(sql, [], context.inputConf().dataSource);

    return results;
}

function getParameter(queryString, param) {
    var regex = new RegExp(param + "=([^&]*)");
    var match = queryString.match(regex);
    return match ? decodeURIComponent(match[1]) : null;
}
```

## 注意事项

1. **字符串值需要单引号**
   ```
   Name eq 'John'  ✓
   Name eq John    ✗
   ```

2. **日期格式要规范**
   ```
   datetime'2026-01-14T00:00:00'  ✓
   2026-01-14                      ✗
   ```

3. **空格敏感**
   ```
   Age gt 30  ✓
   Age gt30   ✗
   ```

4. **大小写敏感**
   OData 操作符通常是小写的：
   ```
   eq, ne, gt, ge, lt, le  ✓
   EQ, NE, GT, GE, LT, LE  ✗
   ```

## 参考资源

- [OData 官方文档](https://www.odata.org/)
- [OData 查询选项](https://www.odata.org/getting-started/basic-tutorial/#queryData)

## 下一步

- 了解 [REST API 文档](../api/rest-api.md)
- 查看 [快速入门指南](quickstart.md)
- 学习 [脚本开发](script-development.md)
