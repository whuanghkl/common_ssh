/**
 * Copyright (C) 2016 - 2017 youtongluan.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有这个标志，就说明是软删除
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SoftDelete {
    /**
     * 数据库中字段的名字
     */
    String value();

    /**
     * 只能是String、Int、Byte、Short、Long
     *
     * @return
     */
    Class<?> columnType() default int.class;

    /**
     * 如果是数字类型，会被转化成数字类型
     *
     * @return
     */
    String validValue() default "1";

    /**
     * 如果是数字类型，会被转化成数字类型
     *
     * @return
     */
    String inValidValue() default "2";
}
