package com.wl2o2o.smartojbackendjudgeservice.judge.strategy;

import com.wl2o2o.smartojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 * @author WL2O2O
 * @create 2023/12/19 1:05
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
