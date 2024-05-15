package com.wl2o2o.smartojbackendjudgeservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.wl2o2o.smartojbackendcommon.common.ErrorCode;
import com.wl2o2o.smartojbackendcommon.exception.BusinessException;
import com.wl2o2o.smartojbackendjudgeservice.judge.JudgeService;
import com.wl2o2o.smartojbackendmodel.model.entity.Question;
import com.wl2o2o.smartojbackendmodel.model.entity.QuestionSubmit;
import com.wl2o2o.smartojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.wl2o2o.smartojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.wl2o2o.smartojbackendserviceclient.service.QuestionFeignClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @From <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 * @CreateTime 2024/1/5
 */
@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    JudgeService judgeService;

    @Resource
    private QuestionFeignClient questionFeignClient;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    private void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("接收到消息 ： {}", message);
        long questionSubmitId = Long.parseLong(message);

        if (message == null) {
            // 消息为空，则拒绝消息（不重试），进入死信队列
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NULL_ERROR, "消息为空");
        }
        try {
            judgeService.doJudge(questionSubmitId);
            QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
            if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.SUCCEED.getValue())) {
                channel.basicNack(deliveryTag, false, false);
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "判题失败");
            }
            log.info("新提交的信息：" + questionSubmit);
            // 如果doJudge()后成功AC，那么设置通过数++
            // 获取判题信息JSON对象
            String judgeInfo = questionSubmit.getJudgeInfo();
            // 解析 JSON 字符串
            JsonNode jsonNode = new ObjectMapper().readTree(judgeInfo);
            // 获取 message 字段
            String judgeMessage = jsonNode.get("message").asText();
            if (judgeMessage.equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
                Long questionId = questionSubmit.getQuestionId();
                log.info("题目:" + questionId);
                Question question = questionFeignClient.getQuestionById(questionId);
                Integer acceptedNum = question.getAcceptNum();
                Question updateQuestion = new Question();
                synchronized (question.getAcceptNum()) {
                    acceptedNum = acceptedNum + 1;
                    updateQuestion.setId(questionId);
                    updateQuestion.setAcceptNum(acceptedNum);
                    boolean save = questionFeignClient.updateQuestion(updateQuestion);
                    if (!save) {
                        throw new BusinessException(ErrorCode.OPERATION_ERROR, "保存数据失败");
                    }
                }
            }
            // 手动确认消息
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            // 消息为空，则拒绝消息，进入死信队列
            channel.basicNack(deliveryTag, false, false);
            throw new RuntimeException(e);
        }
    }
}
