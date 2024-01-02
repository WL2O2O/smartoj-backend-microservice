package com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox;

import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 *
 * @author WL2O2O
 * @create 2023/12/16 16:20
 */
public interface CodeSandBox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
