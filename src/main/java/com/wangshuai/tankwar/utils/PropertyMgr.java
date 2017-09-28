package com.wangshuai.tankwar.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {

    private static Properties props = new Properties();

    static {
        try {
            props.load(ResourceHelper.getResourceInputStream("config/tank.properties"));
        } catch (IOException e) {
            System.out.println("配置文件加载出错");
        }
    }

    private PropertyMgr() {

    }

    public static String getProperty(String key) {
        if(null == props) {
            throw new NullPointerException("配置文件加载失败");
        }
        return props.getProperty(key);
    }
}
