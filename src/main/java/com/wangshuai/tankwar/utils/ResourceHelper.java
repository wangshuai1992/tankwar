/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.wangshuai.tankwar.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-27 16:40
 */
public class ResourceHelper {

    private ResourceHelper() {

    }

    /**
     * 根据classpath相对路径获取输入流
     * @param url classpath相对路径
     * @return
     */
    public static InputStream getResourceInputStream(String url) {
        return ResourceHelper.class.getClassLoader().getResourceAsStream(url);
    }

    /**
     * 获取当前classpath在操作系统的绝对路径
     * @return
     * @throws IOException
     */
    public static String getClassPath() throws IOException {
        URL url = ResourceHelper.class.getClassLoader().getResource("");
        if(null == url) {
            return "";
        }
        return url.getPath();
    }

}
