package com.wl2o2o.smartojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wl2o2o.smartojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface QuestionService extends IService<Question> {

    /**
     * 获取所有题目的ID列表
     * @return 题目ID的List
     */
    List<Long> getAllQuestionIds();


    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);
    

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionSubmitPage, HttpServletRequest request);
    //
    // // questionService.incrementSubmitNum(questionId);
    //
    // /**
    //  * submitNum + 1
    //  * @param questionId
    //  */
    // void incrementSubmitNum(Long questionId);
}
