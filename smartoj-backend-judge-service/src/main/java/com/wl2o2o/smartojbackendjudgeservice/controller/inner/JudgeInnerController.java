package com.wl2o2o.smartojbackendjudgeservice.controller.inner;

import com.wl2o2o.smartojbackendjudgeservice.judge.JudgeService;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import com.wl2o2o.smartojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 该服务仅模块内部之间进行调用
 *
 * @Author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @From <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 * @CreateTime 2024/1/3
 */

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    JudgeService judgeService;

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
