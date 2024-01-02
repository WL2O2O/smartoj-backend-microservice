package com.wl2o2o.smartojbackendmodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @from <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目标题
     */
    private String language;

    /**
     * 题目标签
     */
    private String code;

    private static final long serialVersionUID = 1L;
}