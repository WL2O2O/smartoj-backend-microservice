package com.wl2o2o.smartojbackendserviceclient.service;


import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 针对表 question 的数据库操作 service
 * @author WL2O2O
 * @create 2023/12/18 19:47
 */
@FeignClient(name = "smartoj-backend-question-service", path = "api/question/inner")
public interface QuestionFeignClient {

    /**
     * 根据 id 获取题目信息
     * @param questionId
     * @return
     */
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId")long questionId);

    /**
     * 根据 id 获取题目提交信息
     * @param questionId
     * @return
     */
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionId") long questionId);
    /**
     * 根据 id 获取题目更新信息
     * @param questionSubmit
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    /**
     * 保存数据
     * @param question
     * @return
     */
    @PostMapping("/question/save")
    boolean updateQuestion(@RequestBody Question question);
}
