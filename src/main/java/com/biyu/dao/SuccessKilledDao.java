package com.biyu.dao;

import com.biyu.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * 插入一条详细的购买信息
     *
     * @param seckillId 秒杀商品的ID
     * @param userPhone 购买用户的手机号码
     * @return 成功插入就返回1, 否则就返回0
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     * 根据秒杀商品的ID查询SuccessKilled的明细信息.
     *
     * @param seckillId 秒杀商品的ID
     * @param userPhone 购买用户的手机号码
     * @return 秒杀商品的明细信息
     */
    SuccessKilled queryByIdWithSeckill(long seckillId, long userPhone);
}
