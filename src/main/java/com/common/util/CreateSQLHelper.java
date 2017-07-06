package com.common.util;

import com.common.hibernate.cfg.WildCardConfiguration;
import com.string.widget.util.ValueWidget;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Created by whuanghkl on 17/5/31.
 */
public class CreateSQLHelper {
    /***
     * 不会真正在数据库中创建表,仅生成SQL语句<br />
     * 会自动读取classpath中的hibernate.cfg.xml,若没有该文件,请创建
     */
    public static void generateCreateTableSql() {
        generateCreateTableSql(null);
    }

    /***
     * 生成建表的SQL语句
     * @param resource
     */
    public static void generateCreateTableSql(String resource) {
        org.hibernate.cfg.Configuration configuration = null;
        if (ValueWidget.isNullOrEmpty(resource)) {
            //会自动读取classpath中的hibernate.cfg.xml,若没有该文件,请创建
            configuration = new
                    WildCardConfiguration().configure();
        } else {
            configuration = new
                    WildCardConfiguration().configure(resource);
        }
        new SchemaExport(configuration).create(true, false);
    }
}
