package com.czkj.common;

/**
 * @author SunMin
 * @description 常量配置
 * @create 2020/4/15
 * @since 1.0.0
 */
public class SystemConstant {

    /** 头像保存路径 */
    public static final String WINDOWS_PROFILES_PATH = "C:/Users/admin/Pictures/头像上传/";

    /** linux保存路径*/
    public static final String LINUX_PROFILES_PATH = "/root/super_meeting/profiles/";

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG("1"),
        /**
         * 菜单
         */
        MENU("2"),
        /**
         * 按钮
         */
        BUTTON("3");

        private String value;

        MenuType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
