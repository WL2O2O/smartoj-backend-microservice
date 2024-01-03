package com.wl2o2o.smartojbackendserviceclient.service;

import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 * @author WL2O2O
 * @create 2023/12/18 19:47
 */
@FeignClient(name = "smartoj-backend-judge-service", path = "api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionId") long questionSubmitId);
}
