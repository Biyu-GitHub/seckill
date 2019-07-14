package com.biyu.service.impl;

import com.biyu.dao.SeckillDao;
import com.biyu.dao.SuccessKilledDao;
import com.biyu.dto.Exposer;
import com.biyu.dto.SeckillExecution;
import com.biyu.entity.Seckill;
import com.biyu.entity.SuccessKilled;
import com.biyu.enums.SeckillStateEnum;
import com.biyu.exception.RepeatKillException;
import com.biyu.exception.SeckillCloseException;
import com.biyu.exception.SeckillException;
import com.biyu.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    private final String slat = "asSGFG123?@#%%@#gfWERg^*dghTO";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null)
            return new Exposer(false, seckillId);

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime())
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());


        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

        return md5;
    }

    @Override
    public SeckillExecution excuteSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || md5.equals(getMD5(seckillId)))
            throw new SeckillException("Seckill data reweite!");

        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                throw new SeckillCloseException("Seckill is closed!");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);

                if (insertCount <= 0)
                    throw new RepeatKillException("Seckill repeat!");
                else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("Seckill inner error: " + e.getMessage());
        }
    }
}
