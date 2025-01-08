package com.lyq.apiProject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码工具类
 * @author lyq
 */
public class PhoneNumberUtils {
    /**
     * 验证手机号码是否合法
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }
}
