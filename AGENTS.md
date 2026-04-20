# Repository Guidelines

## 指南读取规则

本仓库采用渐进式披露的 `AGENTS.md` 结构。修改任何代码前，先读取根目录 `AGENTS.md`，再读取目标路径向上最近的局部 `AGENTS.md`。当前局部指南位于 `e-server/AGENTS.md`、`e-common/AGENTS.md`、`e-pojo/AGENTS.md`。如果一次改动跨模块，必须同时参考所有相关模块指南。若修改后模块边界、命令、配置方式或约定发生变化，应同步更新对应的 `AGENTS.md`。

## 项目结构

这是 Java 17 + Spring Boot 3.4.4 的 Maven 多模块后端项目。根 `pom.xml` 统一管理依赖版本和模块声明。`e-server` 是应用入口和业务实现，`e-common` 提供跨模块基础能力，`e-pojo` 保存接口与持久化数据结构。文档、截图和部署资源位于 `README.md`、`assets/`、`Dockerfile`、`docker-compose.yaml`、`nginx.conf`。

## 常用命令

- `mvn clean package`：编译全部模块、运行测试并打包。
- `mvn clean package -DskipTests`：跳过测试打包，适合只验证编译或构建镜像前使用。
- `mvn test`：运行全部测试。
- `mvn -pl e-server -am spring-boot:run`：连同依赖模块启动后端服务。
- `docker compose up -d`：启动 Compose 中定义的部署或依赖环境。

首次本地启动前，将 `e-server/src/main/resources/application-dev-example.yml` 复制为 `application-dev.yml`，填入本地数据库、Redis、对象存储、Docker、OAuth、AI 等配置。不要提交真实配置和密钥。

## 全局编码约定

使用 UTF-8、Java 17、4 空格缩进。包名保持 `com.ecode` 层级；类名使用 `PascalCase`，方法和字段使用 `camelCase`，常量使用 `UPPER_SNAKE_CASE`。优先沿用现有分层：Controller 只处理请求和响应，Service 承载业务，Mapper 负责持久化访问，DTO/VO/Entity 放在 `e-pojo`。创建对象时通常不手写 getter、setter、构造方法等样板代码，优先使用 Lombok。

## Java 注释规范

常规注释使用 `//`，只写在能帮助理解意图、边界条件、关键业务规则、非显而易见的算法步骤、兼容性处理或外部系统约束的位置。不要逐行解释代码，也不要写“设置变量”“调用方法”这类重复代码含义的注释。生成代码时，如果存在复杂判断、权限分支、资源释放、异步流程、缓存一致性、沙箱执行、AI 工具调用或第三方接口适配，应补充简短中文注释说明目的。

Javadoc 遵循 Java 标准文档注释规范，使用 `/** ... */`。公共类、公共接口、公共方法、枚举、注解、配置属性类以及对外暴露的 DTO/VO/Entity 字段，应在必要时补充 Javadoc，说明职责、参数、返回值、异常和使用限制。标准 Javadoc 应包含 `@author 竹林听雨`；方法 Javadoc 使用 `@param`、`@return`、`@throws` 等标签，描述业务语义而不是重复类型信息。AI 修改或新增某个 Java 文件时，凡是被新增或改动的 Javadoc 块都必须增加 `@Assisted-by` 字段，值为当前 AI 模型名称；如果已有不同模型名称，应保留原值并用英文逗号分隔累加。重写父类或接口方法时，若行为无变化可使用 `{@inheritDoc}` 或省略；若行为有差异，必须说明差异。Javadoc 内容使用中文，专业名词、类名、参数名和协议名保持原文。

## 数据库迁移与 SQL 变更

数据库结构、索引、约束、初始化数据和修复数据的变更应采用 Flyway 数据库迁移标准管理，不直接依赖手工执行 SQL。迁移脚本放在应用模块的标准目录 `src/main/resources/db/migration`，文件名使用 Flyway 版本规范，如 `V20260421_001__add_problem_index.sql`。每个脚本只描述一次明确变更，已合入或已发布的迁移不得修改；需要修正时新增下一版迁移。修改 Entity、Mapper XML、查询字段、枚举落库值或初始化数据时，必须同步评估是否需要新增 Flyway 脚本，并在 PR 中说明迁移影响和回滚思路(中文说明)。

## 测试与验证

测试放在各模块 `src/test/java`，测试类以 `*Test` 结尾并尽量贴近被测包路径。新增业务逻辑、权限判断、数据转换、沙箱执行、AI 工具调用或 Mapper SQL 时，应补充相应单元测试或集成测试。涉及数据库、Redis、Docker、S3、外部 AI 的测试必须隔离真实生产凭据。

## 提交与 PR

提交信息沿用历史风格：`feat:代码评测`、`fix:跨域问题`、`doc:直播`。建议使用 `feat:`、`fix:`、`doc:`、`refactor:`、`test:` 后接简短中文说明，必要时追加 `close #25`。PR 按 `.github/pull_request_template.md` 填写关联 Issue、主要改动、变更类型、自测情况；接口、配置或行为变化需附日志、截图或请求示例。
