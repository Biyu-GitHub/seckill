package com.biyu.service;

import com.biyu.dto.Exposer;
import com.biyu.dto.SeckillExecution;
import com.biyu.entity.Seckill;
import com.biyu.exception.RepeatKillException;
import com.biyu.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1001);
        logger.info("seckill={}", seckill);
    }

//    @Test
//    public void exportSeckillUrl() {
//        Exposer exposer = seckillService.exportSeckillUrl(1001);
//        logger.info("exposer={}", exposer);
//    }
//
//    @Test
//    public void excuteSeckill() {
//        long id = 1001;
//        long userPhone = 43215678908L;
//        String md5 = "e2b1b89b8295b0f93e43d9f8cd0ae620";
//
//        try {
//            SeckillExecution seckillExecution = seckillService.excuteSeckill(id, userPhone, md5);
//            logger.info("result={}", seckillExecution);
//        } catch (RepeatKillException e) {
//            logger.info(e.getMessage());
//        } catch (SeckillCloseException e) {
//            logger.info(e.getMessage());
//        }
//    }

    @Test
    public void testSeckillLogic() {
        long id = 1002;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long userPhone = 43215678908L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution seckillExecution = seckillService.excuteSeckill(id, userPhone, md5);
                logger.info("result={}", seckillExecution);
            } catch (RepeatKillException e) {
                logger.info(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.info(e.getMessage());
            }
        } else {
            logger.warn("exposer={}", exposer);
        }
    }
}