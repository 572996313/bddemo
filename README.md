# 表格与表格功能维护系统（bdbt）

一个**元数据驱动**的低代码表格配置平台。通过对「表格 / 字段列 / 查询条件 / 按钮」四类元数据的维护，驱动前端动态渲染数据表格及其操作功能。

> 原型来源：`stitch_/` 下由 Stitch 生成的工业风（Industrial Logic）设计稿。
> 已落地原型 `_1`（按钮功能管理）、`_4`（字段列配置）、`_5`（查询条件配置）、`_6`（基础信息维护）、`_7/_12`（数据查询中心）。

## 技术栈

| 层 | 技术 |
|----|------|
| 后端 | Java 21 · Spring Boot 3.3 · Spring Data JPA · Flyway · H2 |
| 前端 | Vue 3 · TypeScript · Vite · Vue Router · Tailwind CSS · Axios |
| 设计 | Industrial Logic 设计系统（Deep Navy / Professional Blue，Inter + JetBrains Mono + Material Symbols） |

## 架构

```
┌─────────────────────────────┐        ┌──────────────────────────────┐
│   Vue 3 前端 (5173)          │  /api  │   Spring Boot 后端 (8088)      │
│   ├ 表格列表                  │ ─────► │   ├ TableConfigController     │
│   ├ 基础信息维护              │        │   ├ ColumnConfigController    │
│   ├ 字段列配置                │        │   ├ QueryConditionController  │
│   ├ 查询条件配置              │        │   ├ TableButtonController     │
│   ├ 按钮功能管理 ★            │        │   └ DataPreviewController     │
│   └ 数据查询中心(动态渲染)     │        │      │                        │
└─────────────────────────────┘        │      ▼  Flyway → H2 (文件库)   │
                                       └──────────────────────────────┘
```

**领域模型**（聚合根 `TableConfig` 三个一对多子表）：

- `table_config` — 表格名称、唯一编码、数据源、布局模式、状态
- `column_config` — 列显示名、数据键、列宽、对齐、可见、可排序
- `query_condition` — 查询标签、字段名、控件类型、匹配方式、默认值
- `table_button` — 按钮名称、编码、图标、操作类型、位置(表头/行内)、样式、启用、事件处理

## 快速开始

### 1. 后端

```bash
cd backend
# Windows / 本机 Maven 若 JAVA_HOME 指向旧路径，先修正：
#   export JAVA_HOME=<JDK21 路径>
mvn spring-boot:run        # 或：mvn package && java -jar target/bdbt-backend-1.0.0.jar
```

- 服务端口：`8088`（默认 8080，本仓库改为 8088 避免占用，见 `application.yml`）
- H2 控制台：http://localhost:8088/h2-console （JDBC `jdbc:h2:file:./data/bdbt`，用户 `sa`，空密码）
- 启动即由 Flyway 自动建表并写入 2 份样例配置（资产主表 / 汽运订单）

> 内网 Nexus 镜像异常时，使用仓库根目录提供的 `.mvn-settings.xml`（阿里云镜像）：
> `mvn -gs ../.mvn-settings.xml spring-boot:run`

### 2. 前端

```bash
cd frontend
npm install
npm run dev
```

- 访问：http://localhost:5173
- Vite 已配置 `/api` 代理到后端 8088，无需跨域配置即可联调
- 生产构建：`npm run build`，产物在 `frontend/dist`

## REST API 速览

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/tables` | 表格列表（摘要 + 子表计数） |
| GET | `/api/tables/{id}` | 表格详情（含字段/查询/按钮） |
| POST | `/api/tables` | 新建表格 |
| PUT | `/api/tables/{id}` | 更新基础信息 |
| PUT | `/api/tables/{id}/full` | 整体保存（全量替换子配置） |
| DELETE | `/api/tables/{id}` | 删除（级联） |
| PUT | `/api/tables/{id}/status?status=PUBLISHED` | 发布/转草稿 |
| POST/PUT/DELETE | `/api/tables/{id}/columns[/{cid}]` | 字段列 CRUD |
| PUT | `/api/tables/{id}/columns/reorder` | 列批量重排 |
| POST/PUT/DELETE | `/api/tables/{id}/queries[/{qid}]` | 查询条件 CRUD |
| POST/PUT/DELETE | `/api/tables/{id}/buttons[/{bid}]` | 按钮 CRUD |
| PUT | `/api/tables/{id}/buttons/{bid}/enabled?enabled=true` | 启停按钮 |
| POST/PUT/DELETE | `/api/tables/{id}/fields[/{fid}]` | 表单字段 CRUD |
| PUT | `/api/tables/{id}/fields/{fid}/move?direction=-1` | 字段上移/下移 |
| POST/PUT/DELETE | `/api/tables/{id}/subtables[/{sid}]` | 子表映射 CRUD |
| GET | `/api/preview/{id}` | 数据查询中心示例数据（表格，元数据驱动） |
| GET | `/api/form-preview/{id}` | 表单填写预览数据（表单，元数据驱动） |

所有响应统一为 `{ code, message, data }`。

## 功能页面对照

系统通过 `pageType` 区分两类页面形态：**表格（GRID）** 与 **表单（FORM）**，二者共用同一套元数据聚合根，工作区按类型分流不同维护 Tab。

### 表格类（GRID）— 4 种布局
| 页面 | 对应原型 | 能力 |
|------|----------|------|
| 表格配置总览 | — | 类型筛选(全部/表格/表单)、搜索、发布/删除 |
| 基础信息 | `_6` | 实体定义 + 页面类型选择 + 布局模式 |
| 字段配置 | `_4` | 列 CRUD、可见/可排序开关、对齐、列宽 |
| 查询条件 | `_5` | 控件类型、匹配方式、默认值 |
| 按钮功能 | `_1` | 按钮 CRUD、操作类型色块、**实时样式预览**、启停 |
| 数据查询中心 | `_7/_12` | 元数据驱动：纯列表 / **侧边抽屉** / **主从联动** / 增强查询 |

### 表单类（FORM）— 完整控件体系
| 页面 | 对应原型 | 能力 |
|------|----------|------|
| 字段配置 | `_2` | 按分组管理、12 种控件、必填/只读/可见、布局权重(半行/整行)、上移/下移 |
| 子表映射 | `_3` | 关联表、外键字段、过滤条件、显示规则、表高模式、**字段权限矩阵** |
| 表单按钮 | `_1` | 同表格按钮管理 |
| 表单预览 | `_10` | **真实可填写表单**：12 种控件动态渲染、分组布局、必填校验、嵌入可编辑子表明细行、实时数据预览 |

**表单字段支持的 12 种控件**：单行文本、数字、多行文本、下拉选择、单选、多选、日期、日期时间、开关、评分、关联选择(REF)、子表(SUBTABLE)。

## 内置样例配置

| ID | 类型 | 名称 | 说明 |
|----|------|------|------|
| 1 | GRID | 资产主表 `T_ASSET_MAIN` | 侧边抽屉，6 列 / 3 查询 / 5 按钮 |
| 2 | GRID | 汽运订单 `T_TRANS_ORDER_MAIN` | 主从联动，8 列 / 4 查询 / 6 按钮 |
| 3 | FORM | 汽运订单编辑表单 `FORM_TRANS_ORDER` | 4 分组 15 字段(全控件) + 1 子表 + 3 按钮 |
| 4 | FORM | 资产盘点表单 `FORM_ASSET_CHECK` | 2 分组 8 字段 + 1 按钮 |

## 端口与运行备注

- 后端 8088、前端 5173 均绑定 IPv4，避免 Windows 下 IPv6 回环连接问题。
- 数据库为 H2 文件库（`backend/data/bdbt.*`），重启不丢配置；删除该目录可重置为初始样例。
