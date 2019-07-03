package com.biyu.dao;

import com.biyu.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     * 根据传过来的seckillId去减少商品的库存.
     *
     * @param seckillId 秒杀商品ID
     * @param killTime  秒杀的精确时间
     * @return 如果秒杀成功就返回1, 否则就返回0
     */
    int reduceNumber(long seckillId, Date killTime);

    /**
     * 根据传过来的seckillId去查询秒杀商品的详情.
     *
     * @param seckillId 秒杀商品ID
     * @return 对应商品ID的的数据
     */
    Seckill queryById(long seckillId);

    /**
     * 查询秒杀的商品列表
     *
     * @param offet 偏移量
     * @param limit 限制查询的数据个数
     * @return 符合偏移量查出来的数据个数
     */
    List<Seckill> queryAll(int offet, int limit);
}
