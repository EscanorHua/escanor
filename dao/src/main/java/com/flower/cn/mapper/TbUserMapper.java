package com.flower.cn.mapper;

import com.flower.cn.domain.TbUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component("tbUserMapper")
public interface TbUserMapper {
    public Long getCount();
    public TbUser selectByPrimaryKey(Long orderId);
    public int insert(TbUser tbUser) throws SQLException;
    public List<TbUser> login(@Param("userName") String userName, @Param("password") String password);
}
