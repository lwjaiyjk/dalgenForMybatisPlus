package com.yjk.framework.dalgen;

import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.google.common.base.MoreObjects;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/2 下午10:29
 * description：
 **/
public class I2MysqlTypeConvert implements ITypeConvert {
    public DbColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toUpperCase();
        if (t.contains("CHAR")) {
            return DbColumnType.STRING;
        } else if (t.contains("DATE") || t.contains("TIMESTAMP")) {
            return DbColumnType.DATE;
        } else if (t.contains("NUMBER")) {
            String unitStr = StringUtils.replace(t, "NUMBER(", StringPool.EMPTY);
            unitStr = StringUtils.replace(unitStr, ")", StringPool.EMPTY);
            int length;
            if (unitStr.contains(",")) {
                final String[] lengthAndPoint = StringUtils.split(unitStr, ",");
                length = MoreObjects.firstNonNull(Ints.tryParse(lengthAndPoint[0]), 0);
                return length <= 16 ? DbColumnType.DOUBLE : DbColumnType.BIG_DECIMAL;
            } else {
                length = MoreObjects.firstNonNull(Ints.tryParse(unitStr), 0);
                return length <= 10 ? DbColumnType.INTEGER : DbColumnType.LONG;
            }
        } else if (t.contains("FLOAT")) {
            return DbColumnType.FLOAT;
        } else if (t.contains("clob")) {
            return DbColumnType.STRING;
        } else if (t.contains("BLOB")) {
            return DbColumnType.OBJECT;
        } else if (t.contains("binary")) {
            return DbColumnType.BYTE_ARRAY;
        } else if (t.contains("RAW")) {
            return DbColumnType.BYTE_ARRAY;
        }else if(t.contains("DECIMAL")){
            return DbColumnType.BIG_DECIMAL;
        }else if(t.contains("INT")){
            return DbColumnType.INTEGER;
        }
        return DbColumnType.STRING;
    }
}