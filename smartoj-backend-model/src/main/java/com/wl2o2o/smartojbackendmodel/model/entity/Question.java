package com.wl2o2o.smartojbackendmodel.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目表
 * @TableName question
 */
@TableName(value ="question")
@Data
@Component
public class Question implements Serializable {
    /**
     * 题目id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private String tags;



    /**
     * 题目难度
     */
    private String difficulty;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private String judgeCase;

    /**
     * 判题限制
     */
    private String judgeConfig;

    /**
     * 提交数量
     */
    private Integer submitNum;

    /**
     * 通过数量
     */
    private Integer acceptNum;

    /**
     * 点赞数量
     */
    private Integer thumbNum;

    /**
     * 收藏数量
     */
    private Integer favourNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    private Byte isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}