package com.wl2o2o.smartojbackendjudgeservice.judge;

import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 * @author WL2O2O
 * @create 2023/12/18 19:47
 */
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
