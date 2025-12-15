# 医简历后台管理系统迁移说明

## 迁移概述

本文档说明了将"后台管理源码"项目的医简历管理功能迁移到"default-ui"项目的详细信息。

## 已完成的迁移内容

### 1. API接口层 (`src/api/medical/`)

已创建以下API模块：

- **admin.js** - 管理员管理接口
  - 管理员列表查询
  - 新增/编辑/删除管理员
  - 管理员登录

- **resume.js** - 简历管理接口
  - 简历列表查询
  - 新增/编辑/删除简历
  - 简历审核
  - 附件管理
  - 科室列表查询

- **meeting.js** - 会议管理接口
  - 会议列表查询
  - 新增/编辑/删除会议
  - 区域列表查询

- **registration.js** - 报名管理接口
  - 报名列表查询
  - 报名审核
  - 会议列表查询

- **user.js** - 用户管理接口
  - 用户列表查询
  - 用户信息保存/删除
  - 用户卡片管理

- **doctor.js** - 医生管理接口
  - 医生列表查询
  - 医生信息保存

- **upload.js** - 文件上传接口
  - 图片上传
  - 文件上传

### 2. 工具类 (`src/utils/`)

- **medical-request.js** - 医简历专用的axios实例
  - 配置医简历API基础路径
  - 实现Token认证
  - 统一错误处理
  - 登录过期处理

- **medical-download.js** - 文件下载工具
  - 支持文件下载功能

- **city.json** - 城市数据（简化版）
  - 包含主要城市数据
  - 支持级联选择

### 3. 组件 (`src/components/`)

- **MedicalImageUpload** - 图片上传组件
  - 支持多图上传
  - 图片预览
  - 文件大小和格式验证

- **MedicalFileUpload** - 文件上传组件
  - 支持文档上传（PDF、Word、PPT等）
  - 文件名自动获取
  - 文件大小和格式验证

### 4. 页面视图 (`src/views/medical/`)

#### 4.1 医简历首页 (`index.vue`)
- 显示当前登录用户信息
- 显示系统信息
- 显示当前IP地址

#### 4.2 简历管理 (`resume/index.vue`)
- 简历列表展示（分页）
- 简历搜索（按名称）
- 新增/编辑简历
- 简历详情查看
- 简历审核
- 简历导出（批量）
- 附件管理（上传、下载、重命名）
- 科室选择
- 城市级联选择

#### 4.3 会议管理 (`meeting/index.vue`)
- 会议列表展示（分页）
- 会议搜索（名称、城市、付费状态）
- 新增/编辑会议
- 会议删除
- 会议封面上传
- 富文本编辑器（会议详情）
- 区域选择
- 时间选择

#### 4.4 报名列表 (`registration/index.vue`)
- 报名列表展示（分页）
- 报名搜索（会议、姓名）
- 报名审核（通过/拒绝）
- 审核状态显示

#### 4.5 管理员管理 (`admin/index.vue`)
- 管理员列表展示（分页）
- 新增/编辑管理员
- 管理员删除
- 管理员类型显示

#### 4.6 用户管理
- **用户列表** (`user/list.vue`)
  - 用户列表展示（分页）
  - 用户搜索（姓名、手机号）
  - 编辑用户信息
  - VIP状态管理
  - 用户删除

- **用户卡片** (`user/card.vue`)
  - 用户卡片列表
  - 新增/编辑用户卡片
  - 卡片删除

#### 4.7 医生管理
- **医生列表** (`doctor/list.vue`)
  - 医生列表展示（分页）
  - 医生搜索（姓名）
  - 编辑医生信息
  - 医生删除

- **医生卡片** (`doctor/card.vue`)
  - 医生卡片列表
  - 新增/编辑医生卡片
  - 卡片删除

### 5. 路由配置 (`src/router/index.js`)

已添加以下路由：

```javascript
/medical-home/index          - 医简历首页
/medical/resume              - 简历管理
/medical/meeting             - 会议管理
/medical/registration        - 报名列表
/medical/admin               - 管理员管理
/medical/user/list           - 用户列表
/medical/user/card           - 用户卡片
/medical/doctor/list         - 医生列表
/medical/doctor/card         - 医生卡片
```

## 环境配置

### 环境变量

需要在 `.env.development` 和 `.env.production` 中配置：

```env
# 医简历API地址
VUE_APP_MEDICAL_API=https://card.xixinyiban.com/Main/action
```

### Token存储

医简历系统使用Cookie存储Token和用户信息：
- `token` - 认证令牌
- `userName` - 用户名
- `is_root` - 用户类型（0:超级管理员, 1:管理员）

## 技术栈

- **Vue 2.6.12** - 前端框架
- **Element UI 2.15.14** - UI组件库
- **Axios** - HTTP请求库
- **Vue Router 3.4.9** - 路由管理
- **Vuex 3.6.0** - 状态管理
- **js-cookie** - Cookie管理

## API说明

### 请求配置

所有医简历相关的API请求都通过 `medical-request.js` 发送，该实例配置了：

1. **基础URL**: `https://card.xixinyiban.com/Main/action`
2. **请求头**: 
   - `Content-Type: application/json;charset=utf-8`
   - `Token: [从Cookie获取]`
3. **超时时间**: 10秒

### 响应处理

- **成功**: `code === 200`
- **登录过期**: `code === 201` - 自动跳转登录页
- **其他错误**: 显示错误消息

## 注意事项

### 1. 城市数据

当前使用的是简化版城市数据（`city.json`），仅包含主要城市。如需完整城市数据，请从原项目复制完整的 `city.json` 文件。

### 2. 富文本编辑器

会议管理页面使用了富文本编辑器组件（Editor），需要确保该组件已在项目中正确配置。

### 3. 文件上传

文件上传功能依赖后端API：
- 图片上传: `/Admin/Admin/uploadImage`
- 文件上传: `/Admin/Admin/uploadFile`

### 4. 权限控制

当前路由配置在 `dynamicRoutes` 中，可以根据实际需求配置权限标识。

### 5. 导出功能

简历导出功能使用直接下载方式，需要后端支持：
- 导出接口: `Admin/AdminCard/cardExcel`

## 待完善功能

1. **完整城市数据** - 需要复制完整的城市数据文件
2. **权限控制** - 根据用户角色动态显示菜单和功能
3. **数据统计** - 首页添加数据统计图表
4. **批量操作** - 完善批量删除、批量审核等功能
5. **导出优化** - 优化导出功能，支持更多格式

## 使用说明

### 1. 安装依赖

```bash
npm install
```

### 2. 开发环境运行

```bash
npm run dev
```

### 3. 生产环境构建

```bash
npm run build:prod
```

### 4. 访问系统

开发环境默认访问地址: `http://localhost:80`

## 联系方式

如有问题，请联系开发团队。

---

迁移完成时间: 2025-12-10

