package com.lyq.apiProject.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author lyq
 * @from liyuqi home
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}