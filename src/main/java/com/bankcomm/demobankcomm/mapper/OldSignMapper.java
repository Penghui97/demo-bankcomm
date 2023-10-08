package com.bankcomm.demobankcomm.mapper;

import com.bankcomm.demobankcomm.entity.OldSign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OldSignMapper extends BaseMapper<OldSign> {
    @Select("select * from old_sign where id like '${idYearMonth}%'")
    List<OldSign> selectAll(@Param("idYearMonth") String idYearMonth);
}
