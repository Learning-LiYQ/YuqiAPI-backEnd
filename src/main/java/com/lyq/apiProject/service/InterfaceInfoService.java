package com.lyq.apiProject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyq.yuqiapicommon.model.entity.InterfaceInfo;

/**
* @author lyq
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-09-25 21:21:35
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
