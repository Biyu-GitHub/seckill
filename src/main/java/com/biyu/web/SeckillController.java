package com.biyu.web;

import com.biyu.dto.Exposer;
import com.biyu.dto.SeckillExecution;
import com.biyu.dto.SeckillResult;
import com.biyu.entity.Seckill;
import com.biyu.enums.SeckillStateEnum;
import com.biyu.exception.RepeatKillException;
import com.biyu.exception.SeckillCloseException;
import com.biyu.exception.SeckillException;
import com.biyu.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    private final Logger logger = LoggerFactory.getLogger((this.getClass()));

    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, @PathVariable("model") Model model) {
        if (seckillId == null)
            return "redirect:/seckill/list";

        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null)
            return "forward:/seckill/list";

        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId) {
        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> excute(@PathVariable("seckillId") Long seckillId,
                                                  @PathVariable("md5") String md5,
                                                  @CookieValue(value = "killPhone", required = false) Long phone) {

        if (phone == null)
            return new SeckillResult<SeckillExecution>(false, "用户未注册");

        SeckillResult<SeckillExecution> result;
        try {

            SeckillExecution execution = seckillService.excuteSeckill(seckillId, phone, md5);
            return new SeckillResult<>(true, execution);

        } catch (RepeatKillException e) {

            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<>(false, execution);

        } catch (SeckillCloseException e) {

            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<>(false, execution);

        } catch (Exception e) {

            logger.info(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<>(false, execution);
        }
    }


    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<>(true, now.getTime());
    }
}