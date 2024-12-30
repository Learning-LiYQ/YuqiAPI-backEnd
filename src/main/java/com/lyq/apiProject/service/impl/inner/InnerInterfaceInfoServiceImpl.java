package com.lyq.apiProject.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyq.apiProject.common.ErrorCode;
import com.lyq.apiProject.exception.BusinessException;
import com.lyq.apiProject.mapper.InterfaceInfoMapper;
import com.lyq.yuqiapicommon.model.entity.InterfaceInfo;
import com.lyq.yuqiapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 根据URL和请求方法获取内部接口信息
     * @param url
     * @param method
     * @return 内部接口信息，若匹配不到则返回null
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);

        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
