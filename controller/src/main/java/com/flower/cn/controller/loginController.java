package com.flower.cn.controller;


import com.alibaba.fastjson.JSONObject;
import com.flower.cn.interf.reqDto.LoginReq;
import com.flower.cn.service.TbUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class loginController {

    private Logger logger = LoggerFactory.getLogger(loginController.class);

    @Autowired
    private TbUserService tbUserService;

    @PostMapping("/login")
    public String login(LoginReq loginReq) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName",loginReq.getUserName());
        params.put("password",loginReq.getPassword());
       String data=tbUserService.login(params);
        return data;

    }
}