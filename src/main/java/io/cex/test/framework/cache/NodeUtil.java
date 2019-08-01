package io.cex.test.framework.cache;


import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

/**
 * @author shenqingyan
 * @create 2017/6/30 16:13
 * @desc Node处理工具类
 **/
public class NodeUtil {
    /**
    * @desc Node转为string
    * @param  node 待转换node
     * @param  defaultVal 默认string
    **/
    public static String parseNode2String(Node node,String defaultVal){
        if(null != node){
            String nodeString = node.getNodeValue();
            if(nodeString != null){
                return nodeString;
            }
        }
        return defaultVal;
    }
    /**
     * @desc Node转为int
     * @param  node 待转换node
     * @param  defaultVal 默认int
     **/
    public static Integer parseNode2Integer(Node node,Integer defaultVal){
        if(null != node){
            String nodeString = node.getNodeValue();
            if(nodeString != null && StringUtils.isNumeric(nodeString)){
                return Integer.parseInt(nodeString);
            }
        }
        return defaultVal;
    }
    /**
     * @desc Node转为double
     * @param  node 待转换node
     * @param  defaultVal 默认double
     **/
    public static double parseNode2Double(Node node,double defaultVal){
        if(null != node){
            String nodeString = node.getNodeValue();
            if(nodeString != null){
                return Double.parseDouble(nodeString);
            }
        }
        return defaultVal;
    }
    /**
     * @desc Node转为float
     * @param  node 待转换node
     * @param  defaultVal 默认float
     **/
    public static float parseNode2Float(Node node,float defaultVal){
        if(null != node){
            String nodeString = node.getNodeValue();
            if(nodeString != null){
                return Float.parseFloat(nodeString);
            }
        }
        return defaultVal;
    }
    /**
     * @desc Node转为bool
     * @param  node 待转换node
     * @param  defaultVal 默认bool
     **/
    public static Boolean pareNode2Boolean(Node node,Boolean defaultVal){
        if(null != node){
            String nodeString = node.getNodeValue();
            if(nodeString != null){
                return Boolean.parseBoolean(nodeString);
            }
        }
        return defaultVal;
    }
}
