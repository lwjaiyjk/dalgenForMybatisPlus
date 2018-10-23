package com.yjk.framework.dalgen;


import com.yjk.framework.dalgen.config.IWalletConfig;
import com.yjk.framework.dalgen.config.IWalletConfigException;

import java.io.File;

/**
 * @author yujiakui
 * @version 1.0
 * Email: jkyu@haiyi-info.com
 * date: 2018/10/19 9:42
 * description：sql解析工具
 **/
public class SqlParserUtil {

    public static IWalletConfig parse(String filePath) throws IWalletConfigException {
        //InputStream inputStream = SqlParserUtil.class.getResourceAsStream("sql/tables-config.xml");
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("文件不存在");
        }

        IWalletConfig.init(file);

        IWalletConfig iWalletConfig = IWalletConfig.getInstance();

       /* IWalletTableConfig iWalletTableConfig = iWalletConfig.getTableConfig("user_show_info");
        System.out.println(iWalletTableConfig);
        System.out.println(iWalletConfig);*/
        return iWalletConfig;
    }


    public static void main(String[] args) throws IWalletConfigException {
        parse("D:\\workProject\\generatecode\\src\\main\\resources\\sql\\tables-config.xml");
    }
}
