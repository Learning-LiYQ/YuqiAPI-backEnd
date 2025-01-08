package com.lyq.apiProject.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户发送手机短信验证码
 * @author lyq
 */
@Data
public class UserSmsDTO implements Serializable {

    private static final long serialVersionUID = 7217985836723517366L;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 短信验证码
     */
    private String captchaCode;

    public UserSmsDTO(String phoneNumber, String captchaCode) {
        this.phoneNumber = phoneNumber;
        this.captchaCode = captchaCode;
    }
}
