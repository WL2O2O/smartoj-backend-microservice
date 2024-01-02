package com.wl2o2o.smartojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * 判题用例
 * @author wl2o2o
 * @create 2023/11/30 15:31
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;
}
