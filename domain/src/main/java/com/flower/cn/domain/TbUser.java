package com.flower.cn.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TbUser {
    private BigInteger userId;
    private String  userName;
    private String  password;
}
