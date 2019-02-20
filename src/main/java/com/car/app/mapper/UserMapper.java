package com.car.app.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import com.car.app.mapper.BasedProvider;
import com.car.app.model.User;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    @Select("SELECT * FROM car_user")
    List<User> getAll();
    
    @Select("SELECT * FROM car_user where account = #{account}")
    List<User> getUserByAccount(@Param("account") String account);

    @InsertProvider(type = BasedProvider.class, method = BasedProvider.INSERT)
    int insertUser(User user);

    @Delete("DELETE FROM car_user")
    void deleteAll();
    
    @Update("update car_user set password = #{password} where account = #{account}")
    void updatePassword(@Param("account") String account, @Param("password") String password);
    
    @Update("update car_user set nick = #{nick} where account = #{account}")
    void updateNick(@Param("account") String account, @Param("nick") String nick);

}
