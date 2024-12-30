package com.lyq.apiProject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyq.yuqiapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author lyq
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-10-23 22:30:55
* @Entity com.lyq.apiProject.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    //select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
    //查询用户接口信息表中，总调用次数前limit个的接口信息
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




