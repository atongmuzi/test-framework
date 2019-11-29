package io.cex.test.framework.common;


import static org.junit.Assert.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author shenqingyan
 * @create 2017/3/31 10:51
 * @desc 反射工具类
 **/
public class ReflectionUtil {

    /**
     * 获得成员变量
     *
     * @param targetType
     * @param fieldName
     * @return Field
     */
    public static Field getAccessibleField(Class<?> targetType, String fieldName) {
        assertNotNull("missing targetType", targetType);
        Field field = null;
        for (Class<?> c = targetType; c != null && field == null; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            } catch (Exception e) {
                fail(e.toString());
                return null;
            }
        }
        assertNotNull("field " + fieldName + " not found in " + targetType, field);
        field.setAccessible(true);
        return field;
    }

    /**
     * 获得成员方法
     *
     * @param targetType
     * @param methodName
     * @param argTypes
     * @return Method
     */
    public static Method getAccessibleMethod(Class<?> targetType, String methodName, Class<?>[] argTypes) {
        assertNotNull("missing targetType", targetType);
        Method method = null;
        for (Class<?> c = targetType; c != null && method == null; c = c.getSuperclass()) {
            try {
                method = c.getDeclaredMethod(methodName, argTypes);
            } catch (NoSuchMethodException e) {
            } catch (Exception e) {
                fail(e.toString());
                return null;
            }
        }
        assertNotNull("method " + methodName + " not found in " + targetType, method);
        method.setAccessible(true);
        return method;
    }

    /**
     * 获得成员变量的值
     *
     * @param <T>
     * @param target
     * @param fieldName
     * @param fieldType
     * @return T
     */
    public static <T> T getFieldValue(Object target, String fieldName, Class<T> fieldType) {
        return getFieldValue(target, null, fieldName, fieldType);
    }

    /**
     * 获得成员变量的值
     *
     * @param <T>
     * @param target
     * @param targetType
     * @param fieldName
     * @param fieldType
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object target, Class<?> targetType, String fieldName, Class<T> fieldType) {
        if (targetType == null && target != null) {
            targetType = target.getClass();
        }
        Field field = getAccessibleField(targetType, fieldName);
        Object value = null;
        try {
            value = field.get(target);
        } catch (Exception e) {
            fail(e.toString());
            return null;
        }
        if (fieldType != null) {
            return fieldType.cast(value);
        } else {
            return (T) value;
        }
    }

    /**
     * 调用方法
     *
     * @param <T>
     * @param target
     * @param methodName
     * @param argTypes
     * @param args
     * @param returnType
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException T
     */
    public static <T> T invokeMethod(Object target, String methodName, Class<?>[] argTypes, Object[] args,
                                     Class<T> returnType) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        return invokeMethod(target, null, methodName, argTypes, args, returnType);
    }

    /**
     * 调用方法
     *
     * @param <T>
     * @param target
     * @param targetType
     * @param methodName
     * @param argTypes
     * @param args
     * @param returnType
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException T
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Object target, Class<?> targetType, String methodName, Class<?>[] argTypes,
                                     Object[] args, Class<T> returnType) throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        if (targetType == null && target != null) {
            targetType = target.getClass();
        }
        Method method = getAccessibleMethod(targetType, methodName, argTypes);
        Object value = method.invoke(target, args);
        if (returnType != null) {
            return returnType.cast(value);
        } else {
            return (T) value;
        }
    }

    /**
     * 设置成员变量的值
     *
     * @param obj
     * @param fieldName
     * @param fieldValue void
     */
    public static void setFieldValueIgnoreModifiers(Object obj,String fieldName,Object  fieldValue) {
        Field field;
        try {
            field = getAccessibleField(obj.getClass(), fieldName);
            field.setAccessible(true);

            Field override = AccessibleObject.class
                    .getDeclaredField("override");
            override.setAccessible(true);

            Field modifiers = override.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);

            override.set(field, false);
            modifiers.set(field, Modifier.PUBLIC);

            field.set(obj, fieldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
