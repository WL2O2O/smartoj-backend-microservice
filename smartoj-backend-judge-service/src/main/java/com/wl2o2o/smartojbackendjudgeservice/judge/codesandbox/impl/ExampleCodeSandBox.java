package com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.impl;

import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.JudgeInfo;
import com.wl2o2o.smartojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.wl2o2o.smartojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱接口
 * @author WL2O2O
 * @create 2023/12/16 16:58
 */
@Slf4j
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        
        executeCodeResponse.setJudgeInfo(judgeInfo);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
