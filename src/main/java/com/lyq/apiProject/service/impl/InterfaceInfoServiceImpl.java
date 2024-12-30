package com.lyq.apiProject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyq.apiProject.common.ErrorCode;
import com.lyq.apiProject.exception.BusinessException;
import com.lyq.apiProject.exception.ThrowUtils;
import com.lyq.apiProject.mapper.InterfaceInfoMapper;
import com.lyq.apiProject.service.InterfaceInfoService;
import com.lyq.yuqiapicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author lyq
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-09-25 21:21:35
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {
    
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
//        if (StringUtils.isNotBlank(title) && title.length() > 80) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
//        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }
}




