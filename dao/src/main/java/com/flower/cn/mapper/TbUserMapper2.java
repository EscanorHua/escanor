package com.flower.cn.mapper;

import com.flower.cn.domain.TbUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Mapper
@Component("tbUserMapper2")
public interface TbUserMapper2 {
    @Select("SELECT count(*) from tb_user")
    Long getCount();

    @Insert("insert into tb_user(user_id,user_name,password) values (#{userId},#{userName},#{password}})")
    public int insert(TbUser tbUser) throws SQLException;
}
