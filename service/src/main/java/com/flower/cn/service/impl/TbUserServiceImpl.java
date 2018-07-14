package com.flower.cn.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flower.cn.domain.TbUser;
import com.flower.cn.exception.ExceptionEnum;
import com.flower.cn.exception.TradeException;
import com.flower.cn.mapper.TbUserMapper;
import com.flower.cn.service.TbUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbUserServiceImpl implements TbUserService{
    private Logger logger = LoggerFactory.getLogger(TbUserServiceImpl.class);

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public String login(Map<String, Object> params) {
        String userName = (String) params.get("userName");
        String password = (String) params.get("password");
        try {
            List<TbUser> tbUsers = tbUserMapper.login(userName, password);
            if(tbUsers.size()>0){
                return  JSON.toJSONString(tbUsers.get(0));
            }else {
                throw new TradeException(ExceptionEnum.LOGIN_ERROR);
            }
        }catch (Exception e){
            logger.error("登录失败，失败原因："+e.getMessage());
            throw new TradeException(ExceptionEnum.INTERFACE_QUERY_ERROR);
        }
    }
}
