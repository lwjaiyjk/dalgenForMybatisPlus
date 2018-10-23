package com.yjk.framework.dalgen.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/22 9:33
 * description：sql操作信息
 **/
@Data
@NoArgsConstructor
public class SqlOpInfo {

    /**
     * sql 操作对应的模板后缀，是select，insert还是delete
     */
    private String templateSuffix;

    /**
     * sql 语句
     */
    private String sql;

    /**
     * 生成之后的sql map
     */
    private String sqlMap;

    /**
     * 简单返回值类型
     */
    private String simpleReturnType;

    /**
     * 参数列表
     */
    private List<ColumnInfo> params;

    /**
     * 操作名称
     */
    private String name;
}
