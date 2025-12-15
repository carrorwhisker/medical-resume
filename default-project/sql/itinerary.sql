-- 行程管理表
CREATE TABLE `project_itinerary` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '行程ID',
  `sign_up_id` bigint NOT NULL COMMENT '报名ID',
  `meeting_id` bigint NOT NULL COMMENT '会议ID',
  `uid` varchar(50) NOT NULL COMMENT '用户UID',
  `reception_location` varchar(200) DEFAULT NULL COMMENT '接待地点',
  `reception_expert` varchar(100) DEFAULT NULL COMMENT '接待专家',
  `reception_expert_phone` varchar(20) DEFAULT NULL COMMENT '接待专家电话',
  `notes` text COMMENT '行程备注',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间（时间戳）',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间（时间戳）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_sign_up_id` (`sign_up_id`),
  KEY `idx_meeting_id` (`meeting_id`),
  KEY `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='行程管理表';

-- 交通信息表
CREATE TABLE `project_transportation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '交通信息ID',
  `itinerary_id` bigint NOT NULL COMMENT '行程ID',
  `transport_type` tinyint NOT NULL COMMENT '交通类型 1飞机 2火车 3高铁 4汽车 5其他',
  `departure_location` varchar(100) NOT NULL COMMENT '出发地',
  `arrival_location` varchar(100) NOT NULL COMMENT '到达地',
  `departure_time` bigint NOT NULL COMMENT '出发时间（时间戳）',
  `arrival_time` bigint NOT NULL COMMENT '到达时间（时间戳）',
  `transport_number` varchar(50) DEFAULT NULL COMMENT '车次/航班号',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_image` varchar(500) DEFAULT NULL COMMENT '票据图片',
  `notes` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间（时间戳）',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间（时间戳）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_itinerary_id` (`itinerary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='交通信息表';

