# 贡献指南

感谢你对 MD-Agent 项目的关注！我们欢迎任何形式的贡献，包括但不限于代码提交、问题报告、文档改进和功能建议。

## 行为准则

本项目采用了[贡献者公约行为准则](CODE_OF_CONDUCT.md)。参与此项目即表示你同意遵守其条款。我们致力于为每个人提供友好、安全和欢迎的环境。

## 如何贡献

### 报告问题

如果你发现了 Bug 或有功能建议，请通过 GitHub Issues 提交：

1. 搜索现有的 Issues，确保问题尚未被报告
2. 创建新的 Issue，并提供以下信息：
   - 问题的清晰描述
   - 复现步骤（如果是 Bug）
   - 期望行为与实际行为
   - 环境信息（JDK 版本、操作系统等）
   - 相关日志或截图

### 提交代码

1. **Fork 仓库**

   在 GitHub 上 Fork 本仓库到你的账号

2. **克隆到本地**
   ```bash
   git clone https://github.com/你的用户名/md-agent.git
   cd md-agent
   ```

3. **创建分支**
   ```bash
   git checkout -b feature/你的功能名称
   # 或
   git checkout -b fix/你要修复的问题
   ```

4. **开发与测试**
   ```bash
   # 构建项目
   mvn clean package

   # 运行测试
   mvn test
   ```

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: 添加某功能"
   ```

6. **推送并创建 Pull Request**
   ```bash
   git push origin feature/你的功能名称
   ```
   然后在 GitHub 上创建 Pull Request

## 开发规范

### 环境要求

- JDK 1.8+
- Maven 3.x

### 代码风格

- 遵循 Java 标准命名规范
- 类名使用 PascalCase
- 方法名和变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE
- 代码缩进使用 4 个空格

### 提交信息规范

使用语义化的提交信息格式：

```
<类型>: <描述>

[可选的详细说明]
```

类型包括：
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `style`: 代码格式调整（不影响代码逻辑）
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建或辅助工具变动

示例：
```
feat: 添加 PostgreSQL 数据源支持
fix: 修复 Kafka 消费者连接超时问题
docs: 更新 API 文档
```

### 分支命名

- 功能分支: `feature/功能名称`
- 修复分支: `fix/问题描述`
- 文档分支: `docs/文档内容`

## Pull Request 指南

- 确保代码能够正常编译
- 如果添加新功能，请考虑添加相应测试
- 更新相关文档（如有必要）
- PR 描述应清晰说明更改内容和原因
- 一个 PR 应专注于单一功能或修复

## 项目结构

```
md-agent/
├── src/main/java/com/bsi/
│   ├── md/agent/           # 主要业务代码
│   │   ├── controller/     # 控制器
│   │   ├── datasource/     # 数据源管理
│   │   ├── engine/         # 集成引擎核心
│   │   ├── email/          # 邮件服务
│   │   ├── proxy/          # API 代理
│   │   └── sap/            # SAP 连接
│   ├── factory/            # 工厂类
│   └── utils/              # 工具类
└── src/main/resources/     # 配置文件
```

## 许可证

通过提交代码，你同意将你的贡献按照 [MIT License](LICENSE) 许可证进行授权。

## 联系方式

如有任何问题，欢迎通过 GitHub Issues 与我们联系。

再次感谢你的贡献！
