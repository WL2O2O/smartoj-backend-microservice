package com.wl2o2o.smartojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableName;

import com.wl2o2o.smartojbackendmodel.model.dto.question.JudgeConfig;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 * @TableName question
 */
@TableName(value ="question")
@Data
public class QuestionVO implements Serializable {
    /**
     * 题目id
     */
    private Long id;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目难度
     */
    private String difficulty;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 判题限制
     */
    private JudgeConfig judgeConfig;

    /**
     * 提交数量
     */
    private Integer submitNum;

    /**
     * 通过数量
     */
    private Integer acceptNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionVO.setTags(tagList);

        String judgeConfig = question.getJudgeConfig();
        // question --> QuestionVO
        questionVO.setJudgeConfig(JSONUtil.toBean(judgeConfig, JudgeConfig.class));
        return questionVO;
    }

    private static final long serialVersionUID = 1L;
}