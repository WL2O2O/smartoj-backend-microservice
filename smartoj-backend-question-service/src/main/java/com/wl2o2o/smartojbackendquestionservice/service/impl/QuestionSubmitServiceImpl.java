package com.wl2o2o.smartojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wl2o2o.smartojbackendcommon.common.ErrorCode;
import com.wl2o2o.smartojbackendcommon.constant.CommonConstant;
import com.wl2o2o.smartojbackendcommon.exception.BusinessException;
import com.wl2o2o.smartojbackendcommon.utils.SqlUtils;
import com.wl2o2o.smartojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wl2o2o.smartojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import com.wl2o2o.smartojbackendmodel.model.entity.User;
import com.wl2o2o.smartojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.wl2o2o.smartojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.wl2o2o.smartojbackendmodel.model.vo.QuestionSubmitVO;
import com.wl2o2o.smartojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.wl2o2o.smartojbackendquestionservice.message.MyMessageProducer;
import com.wl2o2o.smartojbackendquestionservice.service.QuestionService;
import com.wl2o2o.smartojbackendquestionservice.service.QuestionSubmitService;
import com.wl2o2o.smartojbackendserviceclient.service.JudgeFeignClient;
import com.wl2o2o.smartojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;


    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // todo 检验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (enumByValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言异常");
        }

        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // // 更新题目的submitNum字段
        // questionService.incrementSubmitNum(questionId);
        // 设置提交数
        Integer submitNum = question.getSubmitNum();
        Question updateQuestion = new Question();
        synchronized (question.getSubmitNum()) {
            submitNum = submitNum + 1;
            updateQuestion.setId(questionId);
            updateQuestion.setSubmitNum(submitNum);
            boolean save = questionService.updateById(updateQuestion);
            if (!save) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据保存失败");
            }
        }

        // 是否已题目提交
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(questionSubmitAddRequest.getLanguage());
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        // 执行判题相关的业务：把题目提交 ID 传入消息队列
        Long questionSubmitId = questionSubmit.getId();
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));

        // 这是原来的异步调用判题逻辑
        // CompletableFuture.runAsync(()->{
        //     judgeFeignClient.doJudge(questionSubmitId);
        // });

        return questionSubmitId;

        // 每个用户串行题目提交
        // // 锁必须要包裹住事务方法
        // QuestionSubmitService questionSubmitService = (QuestionSubmitService) AopContext.currentProxy();
        // synchronized (String.valueOf(userId).intern()) {
        //     return questionSubmitService.doQuestionSubmitInner(userId, questionId);
        // }


    }
    /**
     * 获取查询包装类
     * (用户可能根据某个字段进行查询，根据前端查询的请求对象，查询到 mybatis-plus 框架支持的 QueryWraper 类)
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        String sortField = questionSubmitQueryRequest.getSortField();


        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);


        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅提交用户和管理员可以看见自己提交代码
        Long userId = loginUser.getId();
        // 处理脱敏
        if (!userFeignClient.isAdmin(loginUser) && userId != questionSubmit.getUserId()) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> { return getQuestionSubmitVO(questionSubmit, loginUser); })
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    // /**
    //  * 封装了事务的方法
    //  *
    //  * @param userId
    //  * @param questionId
    //  * @return
    //  */
    // @Override
    // @Transactional(rollbackFor = Exception.class)
    // public int doQuestionSubmitInner(long userId, long questionId) {
    //     QuestionSubmit questionSubmit = new QuestionSubmit();
    //     questionSubmit.setUserId(userId);
    //     questionSubmit.setQuestionId(questionId);
    //     QueryWrapper<QuestionSubmit> thumbQueryWrapper = new QueryWrapper<>(questionSubmit);
    //     QuestionSubmit oldQuestionSubmit = this.getOne(thumbQueryWrapper);
    //     boolean result;
    //     // 已题目提交
    //     if (oldQuestionSubmit != null) {
    //         result = this.remove(thumbQueryWrapper);
    //         if (result) {
    //             // 题目提交数 - 1
    //             result = questionService.update()
    //                     .eq("id", questionId)
    //                     .gt("thumbNum", 0)
    //                     .setSql("thumbNum = thumbNum - 1")
    //                     .update();
    //             return result ? -1 : 0;
    //         } else {
    //             throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    //         }
    //     } else {
    //         // 未题目提交
    //         result = this.save(questionSubmit);
    //         if (result) {
    //             // 题目提交数 + 1
    //             result = questionService.update()
    //                     .eq("id", questionId)
    //                     .setSql("thumbNum = thumbNum + 1")
    //                     .update();
    //             return result ? 1 : 0;
    //         } else {
    //             throw new BusinessException(ErrorCode.SYSTEM_ERROR);
    //         }
    //     }
    // }

}




