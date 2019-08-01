package io.cex.test.framework.common;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shenqingyan
 * @create 2017/5/29 17:17
 * @desc 将下划线连接的字符串替换为驼峰风格,方便JavaBean拷贝
 **/
public class CamelCasingHashMap<K, V> extends LinkedHashMap<K, V>{

    private static final long serialVersionUID = 1L;
    /**
    * @desc 是否包含key
    * @param  key key值
    **/
    public boolean containsKey(Object key) {
        return super.containsKey(toCamelCasing(key.toString()));
    }
    /**
    * @desc  获取map 中key的value值
    * @param  key key值
    **/
    public V get(Object key) {
        return super.get(toCamelCasing(key.toString()));
    }

    /**
    * @desc 往map中put值
     * @param  key key值
     * @param value value值
    **/
    public V put(K key, V value) {
        String tmp = key.toString();
        if(Character.isUpperCase(tmp.charAt(0))){
            tmp = tmp.toLowerCase();
        }
        return super.put((K) toCamelCasing(tmp), value);
    }

    /**
    * @desc put整个map
    * @param  m map
    **/
    public void putAll(Map m) {
        Iterator iter = m.keySet().iterator();
        while (iter.hasNext()) {
            K key = (K) iter.next();
            V value = (V) m.get(key);
            this.put(key, value);
        }
    }
    /**
    * @desc 删除key
    * @param  key 删除对应key
    **/
    public V remove(Object key) {
        return super.remove(toCamelCasing(key.toString()));
    }


    /**
    * @desc 将下划线连接的字符串替换为驼峰风格,方便JavaBean拷贝
    * @param  s 字符串
    **/
    public String toCamelCasing(String s) {
        if (s == null) {
            return s;
        }

        StringBuffer buffer = new StringBuffer();
        //遍历字符串
        for (int i = 0; i < s.length() - 1; i++) {
            char ch = s.charAt(i);
            if (ch != '_') {
                buffer.append(ch);
            } else {

                char nextChar = s.charAt(i + 1);
                if (nextChar != '_') {
                    if (buffer.toString().length() < 2) {
                        //如果是第一个_则替换下个字符为小写
                        buffer.append(Character.toLowerCase(nextChar));
                    } else {
                        //如果不是第一个_则替换下个字符为大写
                        buffer.append(Character.toUpperCase(nextChar));
                    }
                    i++;
                }
            }
        }
        char lastChar = s.charAt(s.length() - 1);
        if (lastChar != '_') {
            buffer.append(lastChar);
        }

        return buffer.toString();
    }

}
