package com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.impl;

import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱接口（调用网上现成的代码沙箱）
 * @author WL2O2O
 * @create 2023/12/16 16:58
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
