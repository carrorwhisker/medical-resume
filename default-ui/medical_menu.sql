-- =============================================
-- 医简历管理系统菜单SQL
-- 创建时间: 2025-12-10
-- 说明: 包含医简历管理的所有菜单和权限配置
-- =============================================

-- 一级菜单：医简历首页（独立菜单）
INSERT INTO `sys_menu` VALUES (2001, '医简历首页', 0, 4, 'medical-home', NULL, '', '', 1, 0, 'M', '0', '0', '', 'dashboard', 'admin', NOW(), '', NULL, '医简历首页目录');
INSERT INTO `sys_menu` VALUES (2002, '医简历首页', 2001, 1, 'index', 'medical/index', '', 'MedicalHome', 1, 0, 'C', '0', '0', 'medical:home:view', 'dashboard', 'admin', NOW(), '', NULL, '医简历首页菜单');

-- 一级菜单：医简历管理
INSERT INTO `sys_menu` VALUES (2010, '医简历管理', 0, 5, 'medical', NULL, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', NOW(), '', NULL, '医简历管理目录');

-- 二级菜单：简历管理
INSERT INTO `sys_menu` VALUES (2011, '简历管理', 2010, 1, 'resume', 'medical/resume/index', '', 'MedicalResume', 1, 0, 'C', '0', '0', 'medical:resume:list', 'user', 'admin', NOW(), '', NULL, '简历管理菜单');
-- 简历管理按钮权限
INSERT INTO `sys_menu` VALUES (2012, '简历查询', 2011, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2013, '简历新增', 2011, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2014, '简历修改', 2011, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '简历删除', 2011, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '简历审核', 2011, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:examine', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '简历导出', 2011, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:export', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '附件管理', 2011, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:resume:attachment', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：会议管理
INSERT INTO `sys_menu` VALUES (2020, '会议管理', 2010, 2, 'meeting', 'medical/meeting/index', '', 'MedicalMeeting', 1, 0, 'C', '0', '0', 'medical:meeting:list', 'date', 'admin', NOW(), '', NULL, '会议管理菜单');
-- 会议管理按钮权限
INSERT INTO `sys_menu` VALUES (2021, '会议查询', 2020, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:meeting:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '会议新增', 2020, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:meeting:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '会议修改', 2020, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:meeting:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '会议删除', 2020, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:meeting:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2025, '会议导出', 2020, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:meeting:export', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：报名列表
INSERT INTO `sys_menu` VALUES (2030, '报名列表', 2010, 3, 'registration', 'medical/registration/index', '', 'MedicalRegistration', 1, 0, 'C', '0', '0', 'medical:registration:list', 'list', 'admin', NOW(), '', NULL, '报名列表菜单');
-- 报名列表按钮权限
INSERT INTO `sys_menu` VALUES (2031, '报名查询', 2030, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:registration:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '报名审核', 2030, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:registration:examine', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2033, '报名导出', 2030, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:registration:export', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：管理员管理
INSERT INTO `sys_menu` VALUES (2040, '管理员管理', 2010, 4, 'admin', 'medical/admin/index', '', 'MedicalAdmin', 1, 0, 'C', '0', '0', 'medical:admin:list', 'peoples', 'admin', NOW(), '', NULL, '管理员管理菜单');
-- 管理员管理按钮权限
INSERT INTO `sys_menu` VALUES (2041, '管理员查询', 2040, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:admin:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '管理员新增', 2040, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:admin:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2043, '管理员修改', 2040, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:admin:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '管理员删除', 2040, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:admin:remove', '#', 'admin', NOW(), '', NULL, '');

-- 一级菜单：用户管理
INSERT INTO `sys_menu` VALUES (2050, '用户管理', 0, 6, 'medical-user', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', NOW(), '', NULL, '用户管理目录');

-- 二级菜单：用户列表
INSERT INTO `sys_menu` VALUES (2051, '用户列表', 2050, 1, 'list', 'medical/user/list', '', 'MedicalUserList', 1, 0, 'C', '0', '0', 'medical:user:list', 'user', 'admin', NOW(), '', NULL, '用户列表菜单');
-- 用户列表按钮权限
INSERT INTO `sys_menu` VALUES (2052, '用户查询', 2051, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2053, '用户修改', 2051, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2054, '用户删除', 2051, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2055, '用户导出', 2051, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:export', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：用户卡片
INSERT INTO `sys_menu` VALUES (2060, '用户卡片', 2050, 2, 'card', 'medical/user/card', '', 'MedicalUserCard', 1, 0, 'C', '0', '0', 'medical:user:card', 'post', 'admin', NOW(), '', NULL, '用户卡片菜单');
-- 用户卡片按钮权限
INSERT INTO `sys_menu` VALUES (2061, '卡片查询', 2060, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:card:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2062, '卡片新增', 2060, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:card:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2063, '卡片修改', 2060, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:card:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2064, '卡片删除', 2060, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:user:card:remove', '#', 'admin', NOW(), '', NULL, '');

-- 一级菜单：医生管理
INSERT INTO `sys_menu` VALUES (2070, '医生管理', 0, 7, 'medical-doctor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', NOW(), '', NULL, '医生管理目录');

-- 二级菜单：医生列表
INSERT INTO `sys_menu` VALUES (2071, '医生列表', 2070, 1, 'list', 'medical/doctor/list', '', 'MedicalDoctorList', 1, 0, 'C', '0', '0', 'medical:doctor:list', 'user', 'admin', NOW(), '', NULL, '医生列表菜单');
-- 医生列表按钮权限
INSERT INTO `sys_menu` VALUES (2072, '医生查询', 2071, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2073, '医生修改', 2071, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2074, '医生删除', 2071, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2075, '医生导出', 2071, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:export', '#', 'admin', NOW(), '', NULL, '');

-- 二级菜单：医生卡片
INSERT INTO `sys_menu` VALUES (2080, '医生卡片', 2070, 2, 'card', 'medical/doctor/card', '', 'MedicalDoctorCard', 1, 0, 'C', '0', '0', 'medical:doctor:card', 'post', 'admin', NOW(), '', NULL, '医生卡片菜单');
-- 医生卡片按钮权限
INSERT INTO `sys_menu` VALUES (2081, '卡片查询', 2080, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:card:query', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2082, '卡片新增', 2080, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:card:add', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2083, '卡片修改', 2080, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:card:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT INTO `sys_menu` VALUES (2084, '卡片删除', 2080, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'medical:doctor:card:remove', '#', 'admin', NOW(), '', NULL, '');

-- =============================================
-- 菜单层级结构说明
-- =============================================
-- 2001 医简历首页（一级目录）
--   └─ 2002 医简历首页（菜单）
--
-- 2010 医简历管理（一级目录）
--   ├─ 2011 简历管理（菜单）
--   │   ├─ 2012-2018 按钮权限
--   ├─ 2020 会议管理（菜单）
--   │   ├─ 2021-2025 按钮权限
--   ├─ 2030 报名列表（菜单）
--   │   ├─ 2031-2033 按钮权限
--   └─ 2040 管理员管理（菜单）
--       └─ 2041-2044 按钮权限
--
-- 2050 用户管理（一级目录）
--   ├─ 2051 用户列表（菜单）
--   │   ├─ 2052-2055 按钮权限
--   └─ 2060 用户卡片（菜单）
--       └─ 2061-2064 按钮权限
--
-- 2070 医生管理（一级目录）
--   ├─ 2071 医生列表（菜单）
--   │   ├─ 2072-2075 按钮权限
--   └─ 2080 医生卡片（菜单）
--       └─ 2081-2084 按钮权限
-- =============================================

-- =============================================
-- 字段说明
-- =============================================
-- menu_id: 菜单ID (2001-2084)
-- menu_name: 菜单名称
-- parent_id: 父菜单ID (0表示一级菜单)
-- order_num: 显示顺序
-- path: 路由地址
-- component: 组件路径
-- route_name: 路由名称
-- is_frame: 是否为外链 (0是 1否)
-- is_cache: 是否缓存 (0缓存 1不缓存)
-- menu_type: 菜单类型 (M目录 C菜单 F按钮)
-- visible: 菜单状态 (0显示 1隐藏)
-- status: 菜单状态 (0正常 1停用)
-- perms: 权限标识
-- icon: 菜单图标
-- =============================================

