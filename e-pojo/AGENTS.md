# e-pojo 模块指南

## 模块职责

`e-pojo` 保存项目的数据结构，包括接口入参 DTO、接口出参 VO、数据库 Entity、分页 Query、WebSocket/JSON 消息对象和少量持久化辅助对象。该模块是 Controller、Service、Mapper、前端接口和数据库之间的契约层，修改字段时必须评估兼容性。

## 目录边界

- `dto`：请求入参和业务操作入参，例如新增、更新、登录、代码运行、AI 输入。
- `vo`：返回给前端或调用方的视图对象，例如分页结果、统计结果、登录结果。
- `entity`：数据库表或持久化对象，字段应与表结构、Mapper SQL 保持一致。
- `entity/po`：更贴近持久化或外部库的对象，不直接作为公开接口返回。
- `query`：分页和查询条件对象，优先复用 `PageQuery`。
- `json`：WebSocket 或特殊 JSON 数据结构。

## 字段与命名约定

类名后缀要表达用途：请求对象用 `DTO`，响应对象用 `VO`，分页查询用 `Query`，数据库对象用业务名或 `Entity` 语义。字段使用 `camelCase`，布尔含义要明确，避免含糊的 `flag`、`data`。新增字段时同步检查 JSON 序列化、校验注解、Knife4j/OpenAPI 注解、Mapper XML、前端字段名和数据库列名。

## 兼容性规则

不要随意删除、重命名或改变已有公开字段类型；确需调整时，应同时更新 Controller、Service、Mapper、前端调用和文档。Entity 字段变更必须对应 Flyway 数据库迁移脚本，迁移脚本通常放在 `e-server/src/main/resources/db/migration`，不要只修改初始化 SQL 或手工改库。VO 不应暴露密码、密钥、Token 原文、内部路径或不必要的权限字段。DTO 的校验规则应靠近数据入口，配合 `spring-boot-starter-validation` 使用。

## 测试与验证

修改 DTO/VO 后至少验证对应接口序列化结果和前端所需字段。修改 Entity 后运行相关 Mapper 或服务测试，确认 SQL 字段、别名和分页映射正确。可使用 `mvn -pl e-pojo test` 做模块验证；跨模块字段改动应运行 `mvn test` 或至少 `mvn -pl e-server -am test`。
