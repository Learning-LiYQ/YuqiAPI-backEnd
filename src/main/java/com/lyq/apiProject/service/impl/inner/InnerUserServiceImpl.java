package com.lyq.apiProject.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyq.apiProject.common.ErrorCode;
import com.lyq.apiProject.exception.BusinessException;
import com.lyq.apiProject.mapper.UserMapper;
import com.lyq.yuqiapicommon.model.entity.User;
import com.lyq.yuqiapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据密钥获取内部用户信息
     * @param accessKey
     * @return 内部用户信息，如果没有匹配的用户则返回null
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建查询条件包装器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);

        return userMapper.selectOne(queryWrapper);
    }
}
