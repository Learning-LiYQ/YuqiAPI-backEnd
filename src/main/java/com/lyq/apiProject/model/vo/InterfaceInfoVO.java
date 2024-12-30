package com.lyq.apiProject.model.vo;

import cn.hutool.json.JSONUtil;
import com.lyq.apiProject.model.entity.Post;
import com.lyq.yuqiapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接口信息封装视图
 *
 * @author lyq
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    /**
     * 调用次数
     */
    private Integer invokeNum;

    private static final long serialVersionUID = 1L;
}
