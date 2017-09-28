/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.wangshuai.tankwar.utils;

import java.io.InputStream;

/**
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-27 16:40
 */
public class ResourceHelper {

    private ResourceHelper() {

    }

    public static InputStream getResourceInputStream(String url) {
        return ResourceHelper.class.getClassLoader().getResourceAsStream(url);
    }

}
