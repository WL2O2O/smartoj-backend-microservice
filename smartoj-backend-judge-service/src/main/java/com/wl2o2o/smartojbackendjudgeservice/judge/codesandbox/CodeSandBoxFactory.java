package com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox;

import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandBox;
import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandBox;
import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工厂， 可根据字符串来指定使用什么代码沙箱，更加通用
 * @author WL2O2O
 * @create 2023/12/17 20:27
 */
public class CodeSandBoxFactory {
    /**
     * 创建代码沙箱示例
     * @param type
     * @return
     */
    public static CodeSandBox newInstance (String type) {
        switch (type) {
            case "example" :
                return new ExampleCodeSandBox();
            case "remote" :
                return new RemoteCodeSandBox();
            case "thirdParty" :
                return new ThirdPartyCodeSandBox();
            default:
                return new ExampleCodeSandBox();
        }
    }




}
