package com.wjk.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {




    @Insert("insert into `user` (name) values (#{name})")
    void addUser(@Param("name") String name);
}
