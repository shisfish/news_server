package com.shisfish.news.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * @author shisfish
 * @date 2023/9/20
 * @Description 反射工具类
 */
public class ReflectUtil {

    public static Object getFieldValueString2(Object object, String fieldName) throws Exception {
        Object value;
        if (object == null) {
            return "";
        }
        if (StringUtils.isNotEmpty(fieldName)) {
            value = getFieldValue(object, fieldName);
        } else {
            value = object;
        }
        return value;


    }


    public static String getFieldValueString(Object object, String fieldName, String dataType, String format) throws Exception {
        Object value;
        if (object == null) {
            return "";
        }
        if (StringUtils.isNotEmpty(fieldName)) {
            value = getFieldValue(object, fieldName);
        } else {
            value = object;
        }

        if (value == null) {
            return "";
        }

        if ("DATE".equals(dataType)) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(value);
        } else if ("NUMB".equals(dataType)) {
            DecimalFormat df = new DecimalFormat(format);
            return df.format(value);
        } else if ("STR".equals(dataType)) {
            String strValue = value.toString();
            String strSubstring = strValue;
            int intLength = Integer.parseInt(format);
            if (intLength >= 0) {
                strSubstring = strValue.substring(0, intLength);
            } else if (intLength < 0) {
                strSubstring = strValue.substring(0, intLength * -1);
            }
            return strSubstring;
        } else if ("LIST".equals(dataType)) {
            String strValue = value.toString();
            if (!format.endsWith(";")) {
                format = format + ";";
            }
            int intBegin = format.indexOf(value.toString() + ":");
            int intLenght = value.toString().length() + 1;
            return format.substring(intBegin + intLenght, format.indexOf(';', intBegin));
        } else if ("ABS".equals(dataType)) {
            //绝对值函数  格式化 效果   举例 {CardPoint.apply?ABS=2} 取绝对值保留2位小数
            String re = "";
            if (value.toString().indexOf("-") == 0) {
                re = value.toString().substring(1);
            } else {
                re = value.toString();
            }
            BigDecimal bg = new BigDecimal(Double.parseDouble(re));
            BigDecimal res = bg.setScale(Integer.parseInt(format), BigDecimal.ROUND_HALF_UP);
            return res + "";
        } else {
            return value.toString();
        }
    }

    /**
     * 试图获取object.fieldName的值，
     *
     * @param object
     * @param fieldName
     * @return
     * @throws Exception
     */
    public static Object getFieldValue(Object object, String fieldName) throws Exception {
        if (object == null) {
            return "";
        }
        Object fieldValue = null;
        try {
            fieldValue = getValueByField(object, fieldName);
        } catch (Exception e) {
            try {
                fieldValue = getValueByMethod(object, fieldName);
            } catch (Exception e2) {
                throw new Exception("指定的属性不存在:" + object.getClass().getName() + "." + fieldName);
            }
        }

        return fieldValue;
    }

    /**
     * 根据属性名查找 object.fieldName的值
     *
     * @param object
     * @param fieldName
     * @return
     * @throws NoSuchFieldException
     */
    private static Object getValueByField(Object object, String fieldName) throws NoSuchFieldException {
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                try {
                    field.setAccessible(true);
                    return field.get(object);
                } catch (IllegalArgumentException e) {
                    System.out.println("访问指定的属性参数错误:" + object.getClass().getName() + "." + fieldName);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.out.println("指定的属性访问修饰级不足:" + object.getClass().getName() + "." + fieldName);
                    e.printStackTrace();
                }
            } catch (SecurityException e) {
                System.out.println("指定的属性访问修饰级不足" + object.getClass().getName() + "." + fieldName);
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                //什么都不做，继续循环
                //e.printStackTrace();
            }
        }
        throw new NoSuchFieldException();
    }


    /**
     * 根据方法名查找 object.fieldName的值,因为很可能是私有方法，只暴露了get方法
     *
     * @param object
     * @param fieldName
     * @return
     * @throws NoSuchMethodException
     */
    private static Object getValueByMethod(Object object, String fieldName) throws NoSuchMethodException {
        Method method = null;
        String methodName = fieldName;
        if (!fieldName.startsWith("get")) {
            methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        Class<?> clazz = object.getClass();
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, null);
                try {
                    method.setAccessible(true);
                    return method.invoke(object, null);
                } catch (IllegalArgumentException e) {
                    System.out.println("访问指定的方法参数错误:" + object.getClass().getName() + "." + methodName);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.out.println("指定的方法访问修饰级不足:" + object.getClass().getName() + "." + methodName);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    System.out.println("指定的方法:" + object.getClass().getName() + "." + methodName);
                    e.printStackTrace();
                }
            } catch (SecurityException e) {
                System.out.println("指定的方法访问修饰级不足：" + object.getClass().getName() + "." + methodName);
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                //什么都不做继续循环
            }
        }
        throw new NoSuchMethodException();
    }

    /**
     * 根据方法名查找 object.fieldName的值,因为很可能是私有方法，只暴露了set方法
     *
     * @param object    对象实参
     * @param fieldName 属性名
     * @param value     属性值
     * @return void
     * @throws NoSuchMethodException
     * @author Cairo
     * @date 2014-06-05
     */
    public static void setValueByMethod(Object object, String fieldName, Object value) throws NoSuchMethodException {
        Method method = null;
        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Class<?> clazz = object.getClass();
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, new Class<?>[]{value.getClass()});
                try {
                    method.setAccessible(true);
                    method.invoke(object, value);
                    return;
                } catch (IllegalArgumentException e) {
                    System.out.println("访问指定的方法参数错误:" + object.getClass().getName() + "." + methodName);
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.out.println("指定的方法访问修饰级不足:" + object.getClass().getName() + "." + methodName);
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    System.out.println("指定的方法:" + object.getClass().getName() + "." + methodName);
                    e.printStackTrace();
                }
            } catch (SecurityException e) {
                System.out.println("指定的方法访问修饰级不足：" + object.getClass().getName() + "." + methodName);
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                System.out.println("指定的方法访问修饰级不足：" + object.getClass().getName() + "." + methodName);
                e.printStackTrace();
            }

        }
        throw new NoSuchMethodException();
    }

}
