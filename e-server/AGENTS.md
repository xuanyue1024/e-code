# e-server 模块指南

## 模块职责

`e-server` 是 Spring Boot 应用模块，包含启动类 `com.ecode.EServerApplication`、HTTP 接口、业务服务、MyBatis Mapper、WebSocket、AOP、AI 工具、认证登录策略和运行时配置。修改本模块时同时参考根目录 `AGENTS.md`；若变更接口契约或数据结构，还要检查 `e-pojo/AGENTS.md`；若新增通用工具、枚举、异常或配置属性，应检查是否更适合放入 `e-common`。

## 目录边界

- `src/main/java/com/ecode/controller`：接口入口，按 `open`、`user`、`student`、`teacher` 等现有分区放置。
- `src/main/java/com/ecode/service` 与 `service/impl`：业务接口和实现，新增业务保持接口与实现成对出现。
- `src/main/java/com/ecode/mapper` 与 `src/main/resources/mapper`：MyBatis Mapper 接口和 XML，名称需一一对应。
- `src/main/java/com/ecode/config`：Spring、Sa-Token、Redis、WebSocket、WebRTC、S3、AI、MyBatis 等配置。
- `src/main/java/com/ecode/ai`：AI 工具、知识库和文件仓储相关逻辑。
- `src/main/java/com/ecode/websocket`：扫码登录、直播和消息编码相关 WebSocket 逻辑。
- `src/main/resources/templates`、`captcha`：邮件模板和验证码资源。

## 实现约定

Controller 应保持薄层，参数校验、权限检查和业务编排下沉到 Service 或切面。响应统一使用项目既有 `Result` 风格，异常优先抛出 `e-common` 中的业务异常并交给 `GlobalExceptionHandler` 处理。新增 Mapper 方法时同步更新 XML、接口、Service 调用和必要的分页/权限条件。涉及学生、教师、班级、题目、文件、AI 会话等数据访问时，优先复用现有注解和切面，如数据访问检查、验证码校验、AI 会话校验。

数据库变更统一采用 Flyway 迁移管理。涉及建表、改表、索引、约束、初始化数据或修复数据时，在 `src/main/resources/db/migration` 新增版本化 SQL，例如 `V20260421_001__add_problem_index.sql`。不要修改已发布迁移脚本；修正历史问题时新增下一版迁移，并同步更新 Entity、Mapper XML、Service 逻辑和测试。

## 配置与安全

配置项优先放入 `application-dev-example.yml` 和对应 `@ConfigurationProperties` 类，不要硬编码密钥、路径、Token 或外部服务地址。真实 `application-dev.yml`、日志和构建产物不得提交。涉及 Docker 沙箱、S3、OAuth、WebAuthn、AI 模型、Redis Stream 或直播回调时，必须考虑超时、资源释放、鉴权和错误日志。

## 测试与验证

启动验证使用 `mvn -pl e-server -am spring-boot:run`，编译验证使用 `mvn -pl e-server -am test` 或根目录 `mvn test`。新增接口至少验证成功、权限失败、参数非法和关键异常路径。修改 Mapper XML 后应覆盖对应查询条件和分页行为；修改 WebSocket、AI 流式响应或沙箱执行时，应记录可复现的手工验证步骤。
