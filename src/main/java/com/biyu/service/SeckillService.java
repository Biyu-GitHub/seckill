package com.biyu.service;

import com.biyu.dto.Exposer;
import com.biyu.dto.SeckillExecution;
import com.biyu.entity.Seckill;
import com.biyu.exception.RepeatKillException;
import com.biyu.exception.SeckillCloseException;
import com.biyu.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开始十九输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
}
