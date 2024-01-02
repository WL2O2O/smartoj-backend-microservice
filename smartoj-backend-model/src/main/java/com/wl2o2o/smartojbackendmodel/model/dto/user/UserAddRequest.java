package com.wl2o2o.smartojbackendmodel.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @from <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}