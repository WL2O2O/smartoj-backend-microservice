package com.wl2o2o.smartojbackendmodel.model.dto.question;

import com.wl2o2o.smartojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @from <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目id
     */
    private Long id;

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
     * 题目难度
     */
    private String difficulty;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目答案
     */
    private String answer;

    private static final long serialVersionUID = 1L;
}