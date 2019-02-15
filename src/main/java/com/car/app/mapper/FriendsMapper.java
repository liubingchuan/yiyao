package com.car.app.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import com.car.app.mapper.BasedProvider;
import com.car.app.model.Friends;
import com.car.app.model.User;

import java.util.List;

@Mapper
@Component
public interface FriendsMapper {

//    @Select("SELECT * FROM friends")
//    List<Friends> getAll();
    
    @Select("SELECT * FROM friends where account = #{account} and friendaccount = #{friendaccount}")
    List<Friends> getFriendsByAccountAndFriend(@Param("account") String account, @Param("friendaccount") String friendaccount);
    
    @Select("SELECT * FROM friends where account = #{account} and operation = #{operation} and mesend = #{mesend}")
    List<Friends> getFriendsByAccountAndOperationAndMesend(@Param("account") String account, @Param("operation") String operation, @Param("mesend") String mesend);
    
    @Select("SELECT * FROM friends where account = #{account}")
    List<Friends> getFriendsByAccount(@Param("account") String account);
    
    @Select("SELECT * FROM friends where friendaccount = #{friendaccount} and operation = #{operation}")
    List<Friends> getFriendsByFriendaccountAndOperation(@Param("friendaccount") String friendaccount, @Param("operation") String operation);
    
    @Select("SELECT * FROM friends where friendaccount = #{friendaccount} and operation = #{operation} and mereceive = #{mereceive}")
    List<Friends> getFriendsByFriendaccountAndOperationAndMereceive(@Param("friendaccount") String friendaccount, @Param("operation") String operation, @Param("mereceive") String mereceive);
    
    @Select("SELECT * FROM friends where friendaccount = #{friendaccount}")
    List<Friends> getFriendsByFriendaccount(@Param("friendaccount") String friendaccount);

    @InsertProvider(type = BasedProvider.class, method = BasedProvider.INSERT)
    int insertFriends(Friends friends);

    @Delete("DELETE FROM friends where account = #{account} and friendaccount = #{friendaccount}")
    void deleteByAccountAndFriend(@Param("account") String account, @Param("friendaccount") String friendaccount);
    
    @Update("update friends set operation = #{operation},mesend = #{mesend},mereceive = #{mereceive} where account = #{account} and friendaccount = #{friendaccount}")
    void updateFriend(@Param("operation") String operation, @Param("account") String account, @Param("friendaccount") String friendaccount, @Param("mesend") String mesend, @Param("mereceive") String mereceive);

}
