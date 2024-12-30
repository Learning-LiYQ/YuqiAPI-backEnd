package com.lyq.apiProject.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyq.apiProject.annotation.AuthCheck;
import com.lyq.apiProject.common.BaseResponse;
import com.lyq.apiProject.common.ErrorCode;
import com.lyq.apiProject.common.ResultUtils;
import com.lyq.apiProject.constant.UserConstant;
import com.lyq.apiProject.exception.BusinessException;
import com.lyq.apiProject.mapper.InterfaceInfoMapper;
import com.lyq.apiProject.mapper.UserInterfaceInfoMapper;
import com.lyq.apiProject.model.vo.InterfaceInfoVO;
import com.lyq.apiProject.service.InterfaceInfoService;
import com.lyq.yuqiapicommon.model.entity.InterfaceInfo;
import com.lyq.yuqiapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析控制器
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;
    @Autowired
    private InterfaceInfoService interfaceInfoService;


    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfos = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        // 将接口信息按照接口ID分组
        Map<Long, List<UserInterfaceInfo>> interfaceInfoObjMap = userInterfaceInfos.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));

        // 构建条件包装器，查询接口信息
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoObjMap.keySet());
        // 查询获取接口信息列表
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 构建接口信息VO列表，使用流式处理将接口信息转换为接口信息VO对象，并加入列表中
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            // 获取调用次数
            int totalNum = interfaceInfoObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setInvokeNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(interfaceInfoVOList);
    }
}
