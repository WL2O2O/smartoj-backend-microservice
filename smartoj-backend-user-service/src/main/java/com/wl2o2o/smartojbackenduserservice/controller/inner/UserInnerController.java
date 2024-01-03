package com.wl2o2o.smartojbackenduserservice.controller.inner;

import com.wl2o2o.smartojbackendmodel.model.entity.User;
import com.wl2o2o.smartojbackendserviceclient.service.UserFeignClient;
import com.wl2o2o.smartojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 该服务仅模块内部之间进行调用
 *
 * @Author <a href="https://github.com/wl2o2o">程序员CSGUIDER</a>
 * @From <a href="https://wl2o2o.github.io">CSGUIDER博客</a>
 * @CreateTime 2024/1/3
 */

@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    UserService userService;

    /**
     * 根据 id 获取用户
     *
     * @param userId
     * @return
     */
    @Override
    @GetMapping("/get/id")
    public User getById(@RequestParam("userId") long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据 id 获取用户列表
     *
     * @param idList
     * @return
     */
    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> idList) {
        return userService.listByIds(idList);
    }
}
