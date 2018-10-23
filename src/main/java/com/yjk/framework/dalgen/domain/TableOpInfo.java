package com.yjk.framework.dalgen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/22 9:43
 * description：表操作信息
 **/
@Data
@NoArgsConstructor
public class TableOpInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表对应的sql 操作信息
     */
    private List<SqlOpInfo> sqlOpInfos;
}
