package com.yjk.framework.dalgen;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yjk.framework.dalgen.config.IWalletConfig;
import com.yjk.framework.dalgen.config.IWalletOperationConfig;
import com.yjk.framework.dalgen.config.IWalletTableConfig;
import com.yjk.framework.dalgen.domain.ColumnInfo;
import com.yjk.framework.dalgen.domain.SqlOpInfo;
import com.yjk.framework.dalgen.domain.TableOpInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/19 8:53
 * description：
 **/
@Slf4j
public class MyVelocityTemplateEngine extends AbstractTemplateEngine {

    private ConfigBuilder configBuilder;

    private static final String DOT_VM = ".vm";
    private VelocityEngine velocityEngine;

    @Override
    public MyVelocityTemplateEngine init(ConfigBuilder configBuilder) {
        super.init(configBuilder);
        if (null == velocityEngine) {
            Properties p = new Properties();
            p.setProperty(ConstVal.VM_LOADPATH_KEY, ConstVal.VM_LOADPATH_VALUE);
            p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, "");
            p.setProperty(Velocity.ENCODING_DEFAULT, ConstVal.UTF8);
            p.setProperty(Velocity.INPUT_ENCODING, ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode", "true");

            velocityEngine = new VelocityEngine(p);
            velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_INSTANCE,
                    log);

            velocityEngine.setProperty(RuntimeConstants.RUNTIME_LOG_NAME,
                    "log");
            velocityEngine.init();
        }
        this.configBuilder = configBuilder;
        return this;
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isEmpty(templatePath)) {
            return;
        }
        TableInfo tableObj = (TableInfo) objectMap.get("table");
        System.out.println(tableObj);
        Map<String, TableField> tableFieldMap = Maps.uniqueIndex(tableObj.getFields(), new Function<TableField, String>() {
            public String apply(@Nullable TableField tableField) {
                return tableField.getName();
            }
        });
        objectMap.put("fieldMap", tableFieldMap);
        IWalletConfig iWalletConfig = (IWalletConfig) configBuilder.getInjectionConfig().getMap().get("sqlCfg");
        objectMap.put("tableOpInfo", assembleTableOpInfo(iWalletConfig, tableObj, tableFieldMap));
        objectMap.put("cfg", configBuilder.getInjectionConfig());
        Template template = velocityEngine.getTemplate(templatePath, ConstVal.UTF8);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
        template.merge(new VelocityContext(objectMap), writer);
        writer.close();
        logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }


    @Override
    public String templateFilePath(String filePath) {
        if (null == filePath || filePath.contains(DOT_VM)) {
            return filePath;
        }
        StringBuilder fp = new StringBuilder();
        fp.append(filePath).append(DOT_VM);
        return fp.toString();
    }

    /**
     * 组装table 操作信息
     *
     * @param walletConfig
     * @param tableInfo
     * @param tableFieldMap
     * @return
     */
    private TableOpInfo assembleTableOpInfo(IWalletConfig walletConfig, TableInfo tableInfo, Map<String, TableField> tableFieldMap) {
        TableOpInfo tableOpInfo = new TableOpInfo();
        tableOpInfo.setTableName(tableInfo.getName());

        List<SqlOpInfo> sqlOpInfos = Lists.newArrayList();
        // 获取当前表对应的操作配置
        IWalletTableConfig iWalletTableConfig = walletConfig.getTableConfig(tableInfo.getName());
        if (null != iWalletTableConfig) {
            if (CollectionUtils.isNotEmpty(iWalletTableConfig.getOperations())) {
                for (IWalletOperationConfig operationConfig : iWalletTableConfig.getOperations()) {
                    SqlOpInfo sqlOpInfo = new SqlOpInfo();
                    sqlOpInfo.setName(operationConfig.getName());
                    sqlOpInfo.setSql(operationConfig.getSql());
                    // 设置对应的后缀和返回值类型
                    setSuffixAndReturnType(sqlOpInfo, operationConfig, tableInfo.getEntityName());

                    String sqlMap = operationConfig.getSql();
                    List<ColumnInfo> columnInfos = Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(operationConfig.getSqlParser().getParams())) {
                        for (Object paramObj : operationConfig.getSqlParser().getParams()) {
                            ColumnInfo columnInfo = new ColumnInfo();
                            columnInfo.setColName((String) paramObj);
                            TableField tableField = tableFieldMap.get((String) paramObj);
                            columnInfo.setColType(tableField.getColumnType().getType());
                            columnInfo.setVariableName(tableField.getPropertyName());
                            columnInfo.setSimpleJavaType(tableField.getPropertyType());

                            columnInfos.add(columnInfo);
                            sqlMap = sqlMap.replaceFirst("\\?","#{"+tableField.getPropertyName() + "}");
                        }
                    }
                    sqlOpInfo.setSqlMap(sqlMap);
                    sqlOpInfo.setParams(columnInfos);

                    sqlOpInfos.add(sqlOpInfo);
                }
            }
        }

        tableOpInfo.setSqlOpInfos(sqlOpInfos);
        return tableOpInfo;
    }

    /**
     * 设置后缀和返回值类型
     *
     * @param sqlOpInfo
     * @param operationConfig
     * @param doType
     */
    private void setSuffixAndReturnType(SqlOpInfo sqlOpInfo, IWalletOperationConfig operationConfig, String doType) {
        String tempSql = operationConfig.getSql().trim().toLowerCase();
        if (tempSql.startsWith("select")) {
            sqlOpInfo.setTemplateSuffix("select");
            // 设置对应的返回值类型
            setSelectReturnType(sqlOpInfo, operationConfig, doType);
        } else if (tempSql.startsWith("update")) {
            sqlOpInfo.setTemplateSuffix("update");
            sqlOpInfo.setSimpleReturnType("int");
        } else if (tempSql.startsWith("insert")) {
            sqlOpInfo.setTemplateSuffix("insert");
            sqlOpInfo.setSimpleReturnType(doType);
        } else if (tempSql.startsWith("delete")) {
            sqlOpInfo.setTemplateSuffix("delete");
            sqlOpInfo.setSimpleReturnType("int");
        }
    }

    /**
     * 设置select 返回值类型
     *
     * @param sqlOpInfo
     * @param operationConfig
     * @param doType
     */
    private void setSelectReturnType(SqlOpInfo sqlOpInfo, IWalletOperationConfig operationConfig, String doType) {
        String singleReturnType = getSelectReturnTypeForOne(operationConfig, doType);
        if ("many".equals(operationConfig.getMultiplicity())) {
            sqlOpInfo.setSimpleReturnType("List<" + singleReturnType + ">");
        } else {
            sqlOpInfo.setSimpleReturnType(singleReturnType);
        }
    }

    /**
     * 设置one 返回值对应的类型
     *
     * @param operationConfig
     * @param doType
     * @return
     */
    private String getSelectReturnTypeForOne(IWalletOperationConfig operationConfig, String doType) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(operationConfig.getResultMap())) {
            return operationConfig.getResultMap();
        } else if (org.apache.commons.lang.StringUtils.isNotBlank(operationConfig.getResultClass())) {
            return operationConfig.getResultClass();
        } else {
            return doType;
        }
    }


}
