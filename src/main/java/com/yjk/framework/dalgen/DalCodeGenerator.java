package com.yjk.framework.dalgen;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vip.vjtools.vjkit.base.Platforms;
import com.vip.vjtools.vjkit.base.PropertiesUtil;
import com.vip.vjtools.vjkit.io.ResourceUtil;
import com.yjk.framework.dalgen.config.IWalletConfig;
import com.yjk.framework.dalgen.config.IWalletConfigException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 代码生成器演示
 * </p>
 */
@Slf4j
public class DalCodeGenerator {

    private static final String XML_DIR_TPL = "{0}{1}src{1}main{1}resources{1}mapper{1}{2}{1}";
    private static final String SERVICE_DIR_TPL = "{0}{1}src{1}main{1}java{1}com{1}yjk{1}framework{1}dao{1}";

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) throws IWalletConfigException {

        final IWalletConfig iWalletConfig = SqlParserUtil.parse(
                "D:\\workProject\\dalgenForMybatisPlus\\src\\main\\resources\\sql\\tables-config.xml");
        Map<String, Object> injectCfgMap = Maps.newHashMap();
        injectCfgMap.put("sqlCfg", iWalletConfig);
        injectCfgMap.put("log", new TestLogOutput());
        generateByTables("com.yjk.framework.dao",
                "framework", "service"
                , injectCfgMap
                , iWalletConfig.getAllTableNames().toArray(new String[0]));

    }

    protected static void generateByTables(String packageName,
                                           String logicModuleName,
                                           String serviceName, final Map<String, Object> injectCfgMap,
                                           String... tableNames) {
        final String projectLocalPath = Platforms.WORKING_DIR;

        final String servicePackPath = new File(projectLocalPath).getParent() + File.separator + serviceName;

        final String username = System.getProperty("user.name");

        GlobalConfig config = new GlobalConfig();
        final DataSourceConfig dataSourceConfig = getDataSourceConfig();
        StrategyConfig strategyConfig = getStrategyConfig(tableNames);
        config.setActiveRecord(true)
                .setAuthor(username)
                .setOutputDir(StrUtil.indexedFormat("{0}{1}src{1}main{1}java{1}", projectLocalPath, File.separator))
                // 如果需要覆盖，则设置为true
                .setFileOverride(true);

        config.setBaseResultMap(true);
        config.setEnableCache(false);
        config.setBaseColumnList(false);
        config.setOpen(false);
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(injectCfgMap);
            }
        };

        final String xmlDir = StrUtil.indexedFormat(XML_DIR_TPL, projectLocalPath, File.separator, logicModuleName);
        final String serviceDir = StrUtil.indexedFormat(SERVICE_DIR_TPL, servicePackPath, File.separator, logicModuleName);
        final String serviceImplDir = StrUtil.indexedFormat("{0}impl{1}", serviceDir, File.separator);

        FileUtil.mkdir(xmlDir);
        FileUtil.mkdir(serviceDir);
        FileUtil.mkdir(serviceImplDir);


        List<FileOutConfig> focList = Lists.newArrayList();

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                final String entityName = tableInfo.getEntityName();
                return StrUtil.indexedFormat("{0}{1}Mapper.xml", xmlDir, entityName);
            }
        });
        focList.add(new FileOutConfig("/templates/service.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                final String entityName = tableInfo.getEntityName();
                return StrUtil.indexedFormat("{0}I{1}Service.java", serviceDir, entityName);
            }
        });
        final FileOutConfig serviceOutConfig = new FileOutConfig("/templates/serviceImpl.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                final String entityName = tableInfo.getEntityName();
                return StrUtil.indexedFormat("{0}{1}ServiceImpl.java", serviceImplDir, entityName);
            }
        };
        focList.add(serviceOutConfig);
        cfg.setFileOutConfigList(focList);

        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        // 关闭Controller的代码生成
        tc.setController(null);
        // 关闭Service的代码生成
        tc.setService(null);
        // 关闭serviceImpl的代码生成
        tc.setServiceImpl(null);

        new AutoGenerator()
                .setGlobalConfig(config)
                .setCfg(cfg)
                .setTemplate(tc)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity")
                )
                .setTemplateEngine(new MyVelocityTemplateEngine())
                .execute();
    }

    private static DataSourceConfig getDataSourceConfig() {

        try {
            final Properties config = PropertiesUtil.loadFromString(ResourceUtil.toString("code_gen.properties"));

            final String dbTypeStr = PropertiesUtil.getString(config, "db.type", "mysql");
            final DbType dbType = DbType.valueOf(StringUtils.upperCase(dbTypeStr));
            final DataSourceConfig dataSourceConfig = new DataSourceConfig();
            if (dbType == DbType.ORACLE) {
                dataSourceConfig.setTypeConvert(new I2OracleTypeConvert());
            } else if (dbType == DbType.MYSQL) {
                dataSourceConfig.setTypeConvert(new I2MysqlTypeConvert());
            }

            final String dbUrl = PropertiesUtil.getString(config, "db.url", StringPool.EMPTY);
            if (StringUtils.isEmpty(dbUrl)) {
                throw new RuntimeException("Tip: [code_gen.properties] db.url is not empty");
            }
            final String dbUsername = PropertiesUtil.getString(config, "db.username", StringPool.EMPTY);
            if (StringUtils.isEmpty(dbUsername)) {
                throw new RuntimeException("Tip: [code_gen.properties] db.username is not empty");
            }
            final String dbPassword = PropertiesUtil.getString(config, "db.password", StringPool.EMPTY);
            if (StringUtils.isEmpty(dbPassword)) {
                throw new RuntimeException("Tip: [code_gen.properties] db.password is not empty");
            }
            final String dbDriver = PropertiesUtil.getString(config, "db.driver", StringPool.EMPTY);
            if (StringUtils.isEmpty(dbDriver)) {
                throw new RuntimeException("Tip: [code_gen.properties] db.driver is not empty");
            }
            dataSourceConfig.setDbType(dbType)
                    .setTypeConvert(dataSourceConfig.getTypeConvert())
                    .setUrl(dbUrl)
                    .setUsername(dbUsername)
                    .setPassword(dbPassword)
                    .setDriverName(dbDriver);
            return dataSourceConfig;
        } catch (IOException e) {
            throw new RuntimeException("Tip: [code_gen.properties] read error!", e);
        }

    }

    private static StrategyConfig getStrategyConfig(String[] tableNames) {

        try {
            final Properties config = PropertiesUtil.loadFromString(ResourceUtil.toString("code_gen.properties"));
            final String tablePrefix = PropertiesUtil.getString(config, "table.prefix", StringPool.EMPTY);
            StrategyConfig strategyConfig = new StrategyConfig();
            if (StringUtils.isNotEmpty(tablePrefix)) {
                final String[] tablePrefixs = StringUtils.split(tablePrefix, StringPool.COMMA);
                strategyConfig.setTablePrefix(tablePrefixs);
            }
            final boolean emtityLombok = PropertiesUtil.getBoolean(config, "entity.lombok", false);
            final boolean capitalMode = PropertiesUtil.getBoolean(config, "capital.mode", true);

            final String strNaming = PropertiesUtil.getString(config, "naming", "underline_to_camel");
            final NamingStrategy namingStrategy = NamingStrategy.valueOf(strNaming);
            strategyConfig
                    .setCapitalMode(capitalMode)
                    .setEntityLombokModel(emtityLombok)
                    .setDbColumnUnderline(true)
                    .setNaming(namingStrategy)
                    //修改替换成你需要的表名，多个表名传数组
                    .setInclude(tableNames);
            return strategyConfig;
        } catch (IOException e) {
            throw new RuntimeException("Tip: [code_gen.properties] read error!", e);
        }


    }

}