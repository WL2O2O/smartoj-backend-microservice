package com.wl2o2o.smartojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.wl2o2o.smartojbackendcommon.common.ErrorCode;
import com.wl2o2o.smartojbackendcommon.exception.BusinessException;
import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.CodeSandBoxFactory;
import com.wl2o2o.smartojbackendjudgeservice.judge.codesandbox.CodeSandBoxProxy;
import com.wl2o2o.smartojbackendjudgeservice.judge.strategy.JudgeContext;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.wl2o2o.smartojbackendmodel.model.codesandbox.JudgeInfo;
import com.wl2o2o.smartojbackendmodel.model.dto.question.JudgeCase;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import com.wl2o2o.smartojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.wl2o2o.smartojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author WL2O2O
 * @create 2023/12/18 19:55
 */
@Service
public class JudgeServiceImpl implements JudgeService{

    @Resource
    private Question question;

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1、传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在！");
        }
        // 通过提交的信息中的题目id 获取到题目的全部信息
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在！");
        }
        // 2、如果题目提交状态不为等待中
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中！");
        }
        // 3、更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4、调用沙箱，获取到执行结果
        // 创建示例代码沙箱的测试类对象
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        CodeSandBoxProxy codeSandBoxProxy = new CodeSandBoxProxy(codeSandBox);

        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 正则表达式，判断样例中的输入输出 --> 获取输入，然后用 .collect 转换为数组
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandBoxProxy.executeCode(executeCodeRequest);

        // 根据代码沙箱执行结果，判断运行结果是否则正确 --> 设置题目最终的判题状态与信息
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        // // 增加一种情况，就是当代码编译错误时，就设置判题状态为失败
        // if (judgeContext.getJudgeInfo() == null) {
        //     questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        // } else {
        //     questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        // }
        // if (judgeInfo.getMessage() == null) {
        //     questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        // }
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 再次查询数据库，返回最新提交信息
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
