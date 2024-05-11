package com.wl2o2o.smartojbackendquestionservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wl2o2o.smartojbackendcommon.common.ErrorCode;
import com.wl2o2o.smartojbackendcommon.constant.CommonConstant;
import com.wl2o2o.smartojbackendcommon.exception.BusinessException;
import com.wl2o2o.smartojbackendcommon.exception.ThrowUtils;
import com.wl2o2o.smartojbackendcommon.utils.SqlUtils;
import com.wl2o2o.smartojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.User;
import com.wl2o2o.smartojbackendmodel.model.vo.QuestionVO;
import com.wl2o2o.smartojbackendmodel.model.vo.UserVO;
import com.wl2o2o.smartojbackendquestionservice.mapper.QuestionMapper;
import com.wl2o2o.smartojbackendquestionservice.service.QuestionService;
import com.wl2o2o.smartojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {
    
    @Resource
    private UserFeignClient userFeignClient;


    @Override
    public List<Long> getAllQuestionIds() {
        return baseMapper.selectList(new QueryWrapper<Question>().select("id")).stream()
                .map(Question::getId)
                .collect(Collectors.toList());
    }

    /**
     * 校验题目是否合法
     * @param question
     * @param add
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String answer = question.getAnswer();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询包装类
     * (用户可能根据某个字段进行查询，根据前端查询的请求对象，查询到 mybatis-plus 框架支持的 QueryWraper 类)
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        Long userId = questionQueryRequest.getUserId();
        String title = questionQueryRequest.getTitle();
        List<String> tags = questionQueryRequest.getTags();
        String difficulty = questionQueryRequest.getDifficulty();
        String content = questionQueryRequest.getContent();
        String answer = questionQueryRequest.getAnswer();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        queryWrapper.like(StringUtils.isNotBlank(difficulty), "difficulty", difficulty);


        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }
        UserVO userVO = userFeignClient.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userFeignClient.getUserVO(user));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    // @Override
    // public void incrementSubmitNum(Long questionId) {
    //     Integer submitNum = question.getSubmitNum();
    //     Question updateQuestion = new Question();
    //     synchronized (question.getSubmitNum()) {
    //         submitNum = submitNum + 1;
    //         updateQuestion.setId(questionId);
    //         updateQuestion.setSubmitNum(submitNum);
    //         boolean save = questionService.updateById(updateQuestion);
    //         if (!save) {
    //             throw new BusinessException(ErrorCode.OPERATION_ERROR, "数据保存失败");
    //         }
    //     }
    // }
}




