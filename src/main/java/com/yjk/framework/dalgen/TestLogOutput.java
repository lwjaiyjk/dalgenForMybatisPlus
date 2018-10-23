package com.yjk.framework.dalgen;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/19 13:26
 * description：
 **/
@Slf4j
public class TestLogOutput {

    public boolean print(Object object){
        log.info("----print---obj={}",object);
        System.out.println("---------XXX------");
        return true;
    }


}
