-- 整个项目的数据库脚本
-- 开始创建一个数据库
CREATE DATABASE seckill;

-- 使用数据库
USE seckill;

-- 创建秒杀库存表
CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
  `number` INT NOT NULL COMMENT '库存数量',
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '秒杀开启的时间',
  `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '秒杀结束的时间',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建的时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)ENGINE =InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 插入初始化数据
INSERT INTO
  seckill(name,number,start_time,end_time)
VALUES
( '1000元秒杀iPhoneX', 100, '2019-6-18 00:00:00', '2019-6-18 23:59:59' ),
( '500元秒杀iPadAir3', 200, '2019-6-18 00:00:00', '2019-6-18 23:59:59' ),
( '300元秒杀小米9', 300, '2019-6-18 00:00:00', '2019-6-18 23:59:59' ),
( '200元秒杀华为P20', 400, '2019-6-18 00:00:00', '2019-6-18 23:59:59' );

-- 秒杀成功明细表
-- 用户登录相关信息
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL COMMENT '秒杀商品ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标示:-1无效 0成功 1已付款',
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone), /*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE =InnoDB DEFAULT CHARSET =utf8 COMMENT ='秒杀成功明细表'