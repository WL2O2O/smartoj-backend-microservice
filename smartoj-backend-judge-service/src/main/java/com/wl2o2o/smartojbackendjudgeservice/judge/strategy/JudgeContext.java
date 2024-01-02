package com.wl2o2o.smartojbackendjudgeservice.judge.strategy;

import com.wl2o2o.smartojbackendmodel.model.codesandbox.JudgeInfo;
import com.wl2o2o.smartojbackendmodel.model.dto.question.JudgeCase;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 * @author WL2O2O
 * @create 2023/12/19 1:03
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
