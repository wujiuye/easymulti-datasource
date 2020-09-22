package com.github.wujiuye.mybatisplus.generator.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author wujiuye
 * @version 1.0 on 2019/4/24 {描述：}
 */
public class PropertiesUtils {

    /**
     * 获取配置文件内容
     *
     * @return
     */
    public static <T> T getPropertiesConfig(Class<T> configClass, String prefix, String filePath) throws Exception {
        ClassLoader loader = configClass.getClassLoader();
        T obj = configClass.newInstance();
        Properties properties = new Properties();
        if (StringUtils.isBlank(prefix)) {
            prefix = "";
        } else {
            prefix += ".";
        }
        try (InputStream in = loader.getResourceAsStream(filePath);
             InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            properties.load(reader);
            parsingPropertiesWithType(obj, prefix, properties);
        }
        return obj;
    }

    /**
     * 递归解析
     *
     * @param obj
     * @param prefix
     */
    private static void parsingPropertiesWithType(final Object obj, final String prefix, final Properties properties) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            if (Modifier.isInterface(field.getModifiers()) || Modifier.isAbstract(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            if (field.getType() == Integer.class || field.getType() == int.class) {
                String value = properties.getProperty(prefix + field.getName());
                if (value != null) {
                    field.set(obj, Integer.valueOf(value));
                }
            } else if (field.getType() == Long.class || field.getType() == long.class) {
                String value = properties.getProperty(prefix + field.getName());
                if (value != null) {
                    field.set(obj, Long.valueOf(value));
                }
            } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                String value = properties.getProperty(prefix + field.getName());
                if (value != null) {
                    field.set(obj, Boolean.valueOf(value));
                }
            } else if (field.getType() == String.class) {
                String value = properties.getProperty(prefix + field.getName());
                if (value != null) {
                    field.set(obj, value);
                }
            } else {
                try {
                    Object fieldValue = field.getType().newInstance();
                    if (fieldValue != null) {
                        field.set(obj, fieldValue);
                        parsingPropertiesWithType(fieldValue, prefix + field.getName() + ".", properties);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

}
