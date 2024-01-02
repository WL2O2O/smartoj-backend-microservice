package com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox;

import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WL2O2O
 * @create 2023/12/18 16:25
 */
@Slf4j
public class CodeSandBoxProxy implements CodeSandBox{

    private final CodeSandBox codeSandBox;

    // 使用下面的显式构造函数或者使用 LomBook 的注解形式都行
    public CodeSandBoxProxy(CodeSandBox codeSandBox) {
        this.codeSandBox = codeSandBox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱相应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
