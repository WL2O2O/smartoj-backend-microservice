package com.wl2o2o.smartojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 判题限制
 * @author wl2o2o
 * @create 2023/11/30 15:31
 */
@Data
public class JudgeConfig {

    /**
     * 时间限制（kb）
     */
    private Long timeLimit;

    /**
     * 内存限制（ms）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（kb）
     */
    private Long stackLimit;
}
