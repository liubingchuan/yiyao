package com.yiyao.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.yiyao.app.model.PaperMysql;

@Mapper
@Component
public interface PaperMapper {

	@Select("select * from paper order by id asc limit #{index}, #{pageSize}")
    List<PaperMysql> getPapers(@Param("index") int index, @Param("pageSize") int pageSize);
	

}
