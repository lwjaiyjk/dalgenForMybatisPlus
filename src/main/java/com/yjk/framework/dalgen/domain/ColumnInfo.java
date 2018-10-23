package com.yjk.framework.dalgen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/22 9:47
 * description：参数信息
 **/
@Data
@NoArgsConstructor
public class ColumnInfo {

    /**
     * 参数名称
     */
    private String variableName;

    /**
     * 参数对应的java类型
     */
    private String simpleJavaType;

    /**
     * 参数对应的列名称
     */
    private String colName;

    /**
     * 参数对应的列类型
     */
    private String colType;
}
