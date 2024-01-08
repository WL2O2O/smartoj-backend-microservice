package com.wl2o2o.smartojbackendquestionservice.controller.inner;

import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import com.wl2o2o.smartojbackendquestionservice.service.QuestionService;
import com.wl2o2o.smartojbackendquestionservice.service.QuestionSubmitService;
import com.wl2o2o.smartojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

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
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    QuestionService questionService;

    @Resource
    QuestionSubmitService questionSubmitService;

    /**
     * 根据 id 获取题目信息
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    /**
     * 根据 id 获取题目提交信息
     * @param questionId
     * @return
     */
    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionId) {
        return questionSubmitService.getById(questionId);
    }

    /**
     * 根据 id 获取题目更新信息
     * @param questionSubmit
     * @return
     */
    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    public boolean updateQuestion(Question question) {
        return questionService.updateById(question);
    }
}
