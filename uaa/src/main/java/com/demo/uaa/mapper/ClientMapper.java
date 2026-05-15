package com.demo.uaa.mapper;

import com.demo.uaa.entity.Client;

public interface ClientMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Client record);

    int insertSelective(Client record);

    Client selectByPrimaryKey(Long id);

    Client selectByClientId(String clientId);

    int updateByPrimaryKeySelective(Client record);

    int updateByPrimaryKey(Client record);
}