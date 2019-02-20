package com.car.app.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import com.car.app.mapper.BasedProvider;
import com.car.app.model.ImeiUser;

import java.util.List;

@Mapper
@Component
public interface ImeiUserMapper {

    @Select("SELECT * FROM imei_user")
    List<ImeiUser> getAll();
    
    @Select("SELECT * FROM imei_user where imei = #{imei} and count = #{count}")
    List<ImeiUser> getIMEIUserByIMEIAndCount(@Param("imei") String imei, @Param("count") Integer count);
    
    @Select("SELECT * FROM imei_user where imei = #{imei}")
    List<ImeiUser> getIMEIUserByIMEI(@Param("imei") String imei);
    
    @Select("SELECT * FROM imei_user where account = #{account}")
    List<ImeiUser> getIMEIUserByAccount(@Param("account") String account);

    @InsertProvider(type = BasedProvider.class, method = BasedProvider.INSERT)
    int insertIMEIUser(ImeiUser iu);

    @Delete("DELETE FROM imei_user")
    void deleteAll();
    
    @Delete("DELETE FROM imei_user where account = #{account}")
    void deleteByAccount(@Param("account") String account);
    
    @Update("update imei_user set count = #{count} where imei = #{imei}")
    void updateImei(@Param("imei") String imei, @Param("count") Integer count);


}
