package com.nd.mapper;

import com.nd.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User getByUname(String uname);

    void save(User user);

}
