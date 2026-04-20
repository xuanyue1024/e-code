# e-common 模块指南

## 模块职责

`e-common` 保存跨模块共享的基础能力，包括工具类、枚举、异常、注解、常量、上下文、JSON 序列化、统一返回结果和配置属性。这里的代码会被 `e-server` 和 `e-pojo` 依赖，任何改动都可能扩大影响面。修改前先确认能力是否确实通用；只服务单一业务流程的逻辑应留在 `e-server`。

## 目录边界

- `annotation`：业务切面注解，如访问检查、验证码、AI 会话校验。
- `constant`：跨模块常量，按业务域拆分类，不放可变配置。
- `context`：请求上下文和线程本地状态，使用后必须注意清理。
- `enumeration`：稳定枚举，如用户角色、状态、题目难度、响应码。
- `exception`：业务异常层级，新增异常继承现有基类。
- `json`：Jackson/Fastjson 相关配置和序列化器。
- `properties`：`@ConfigurationProperties` 配置载体。
- `result`：统一响应结构。
- `utils`：可复用工具，如 Docker 执行、S3、邮件、IP、文本差异、WebRTC。

## 实现约定

通用代码应保持无状态或清晰声明状态边界。工具类不要直接依赖 Controller、Service 或具体业务流程；确需外部服务客户端时，优先通过参数传入或在 `e-server` 配置 Bean。枚举值和响应码一旦暴露给前端或接口文档，修改需评估兼容性。新增属性类应使用清晰前缀，并与 `application-dev-example.yml` 保持一致。

## 依赖与兼容

避免在 `e-common` 引入重量级业务依赖，除非多个模块都需要。新增第三方依赖应先在根 `pom.xml` 的 `dependencyManagement` 中统一版本，再在模块 `pom.xml` 引用。不要把 `e-pojo` 的 DTO/VO/Entity 反向引入 `e-common`，以免形成不合理依赖。

## 测试与验证

纯工具、枚举映射、序列化、异常和上下文逻辑应优先补充单元测试。可用 `mvn -pl e-common test` 进行模块级验证；如果改动影响下游模块，再运行 `mvn test`。涉及线程上下文、Docker、网络、文件或对象存储的工具，测试应覆盖资源释放和异常路径。
