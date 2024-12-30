package com.lyq.apiProject.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Id请求
 *
 * @author lyq
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
