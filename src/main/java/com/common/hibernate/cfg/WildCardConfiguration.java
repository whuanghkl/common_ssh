package com.common.hibernate.cfg;

import com.common.util.ClassFinder;
import org.hibernate.MappingException;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * 功能:自动生成建表SQL语句
 * Created by whuanghkl on 17/5/31.<br />
 * 继承org.hibernate.cfg.Configuration <br />
 * 为了支持:<br />
 * <mapping  package="com.hw.entity" />
 */
public class WildCardConfiguration extends Configuration {

    @Override
    public Configuration addPackage(String packageName) throws MappingException {
        List<Class<?>> classes = ClassFinder.find(packageName);
        int size = classes.size();
        for (int i = 0; i < size; i++) {
            super.addAnnotatedClass(classes.get(i));
        }
        return this;
    }
}
