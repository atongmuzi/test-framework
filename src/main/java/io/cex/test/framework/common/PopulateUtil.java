package io.cex.test.framework.common;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

public class PopulateUtil {

    /**
    * @desc 把Map里面的属性复制到Bean中
    * @param bean java实体
     * @param properties map
    **/
    public static Object populate(Object bean, Map properties) {
        try {
            BeanUtils.populate(bean, properties);
        } catch (Exception e) {
            throw new RuntimeException("不能复制属性值到目标对象",
                    e);
        }
        return bean;
    }

    /**
     * @desc 把Map里面的属性复制到Bean中
     * @param type 范型
     * @param map map
     **/
    public static <T> T populate(Class<T> type, Map map) {
        Object bean = null;
        try {
            bean = type.newInstance();
            Iterator it = map.entrySet().iterator();
            Map tmp = new HashMap();
            boolean hasSub = false;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();

                if(key.toString().contains(".")) {
                    hasSub = true;
                    tmp.put(key.toString().substring(0,key.toString().indexOf(".")), null);
                } else {
                    tmp.put(key, null);
                }

            }
            //If you include the child objects
            if(hasSub) {
                PropertyDescriptor[] props = BeanUtilsBean.getInstance()
                        .getPropertyUtils().getPropertyDescriptors(bean);
                for (int i = 0; i < props.length; i++) {
                    Class subType = props[i].getPropertyType();
                    if(!subType.getName().startsWith("java.")
                            && !subType.getName().startsWith("[Ljava.")
                            &&!subType.isPrimitive()){
                        try {
                            if(tmp.containsKey(props[i].getName())) {
                                props[i].getWriteMethod().invoke(bean, subType.newInstance());
                            }
                        } catch (Exception e) {

                        }
                    }
                }
            }
            BeanUtils.populate(bean, map);
        } catch (Exception e) {
            throw new RuntimeException("不能复制属性值到目标对象",
                    e);
        }
        return (T)bean;
    }

}
