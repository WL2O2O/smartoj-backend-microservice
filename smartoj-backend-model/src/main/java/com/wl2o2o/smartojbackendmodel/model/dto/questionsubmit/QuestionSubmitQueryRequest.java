package com.wl2o2o.smartojbackendmodel.model.dto.questionsubmit;

import com.wl2o2o.smartojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @from <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 用户id
     */
    private Long userId;


    /**
     * 题目标题
     */
    private String language;

    /**
     * 题目标签
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}