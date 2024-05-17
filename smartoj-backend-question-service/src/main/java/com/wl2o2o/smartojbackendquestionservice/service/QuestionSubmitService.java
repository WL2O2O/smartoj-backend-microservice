package com.wl2o2o.smartojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wl2o2o.smartojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wl2o2o.smartojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import com.wl2o2o.smartojbackendmodel.model.entity.User;
import com.wl2o2o.smartojbackendmodel.model.vo.QuestionSubmitVO;

/**
 *
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 通过id获取questionSubmit
     */

    QuestionSubmit getById(Long id);
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionQueryRequest);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
