package io.cex.test.framework.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.beanutils.BeanUtils;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.excel.XlsDataSet;

public class XlsUtil {


    /**
     * 读取<code>excel</code>文件一个页签中某一行的数据到一个<code>bean</code>中
     *
     * @param clazz 类定义
     * @param excelDir 文件路径 例如resources/demo.xls
     * @param sheetName 页签名称
     * @param line 行
     * @param bean 成员变量要与文件标题行一致
     */
    public static Object readForObject(Class<?> clazz, String excelDir, String sheetName, int line, Object bean) {
        Map<String, List<Map<String, Object>>> allData = readData(clazz, excelDir);
        return readForObject(allData, sheetName, line, bean);
    }

    /**
     * 读取<code>excel</code>文件一个页签中某一行的数据到一个<code>bean</code>中
     *
     * @param allData 数据
     * @param sheetName 页签名称
     * @param line 行
     * @param bean 成员变量要与文件标题行一致
     */
    public static Object readForObject(Map<String, List<Map<String, Object>>> allData,
                                       String sheetName,int line,Object bean) {
        List<Map<String, Object>> sheet = allData.get(sheetName);

        if(line < 0 || line > sheet.size()){
            throw new IllegalArgumentException("行已经超过excle的总行数了");
        }

        return (Object) PopulateUtil.populate(bean, sheet.get(line));

    }

    /**
     * 读取<code>excel</code>文件一个页签中某一行的数据到一个<code>bean</code>中
     *
     * @param <T>
     * @param clazz
     * @param excelDir
     * @param sheetName
     * @param line
     * @param beanClazz
     */
    public static <T> T readForObject(Class<?> clazz, String excelDir, String sheetName,int line,Class<T> beanClazz) {
        Map<String, List<Map<String, Object>>> allData = readData(clazz, excelDir);
        return readForObject(allData, sheetName, line, beanClazz);
    }

    /**
     * 读取<code>excel</code>文件一个页签中某一行的数据到一个<code>bean</code>中
     *
     * @param <T>
     * @param allData
     * @param sheetName
     * @param line
     * @param clazz
     */
    public static <T> T readForObject(Map<String, List<Map<String, Object>>> allData,
                                      String sheetName,int line,Class<T> clazz)  {
        List<Map<String, Object>> sheet = allData.get(sheetName);
        if(line < 0 || line > sheet.size()){
            throw new IllegalArgumentException("行已经超过excle的总行数了");
        }
        return PopulateUtil.populate(clazz, sheet.get(line));
    }

    /**
     * 读取<code>excel</code>文件一个页签中的数据到一个<code>list</code>中
     *
     * @param clazz
     * @param excelDir
     * @param sheetName
     * @param bean
     */
    public static List<Object> readForObjectList(Class<?> clazz, String excelDir, String sheetName, Object bean) {
        Map<String, List<Map<String, Object>>> allData = readData(clazz, excelDir);
        return readForObjectList(allData, sheetName, bean);

    }

    /**
     * 读取<code>excel</code>文件一个页签中的数据到一个<code>list</code>中
     *
     * @param allData
     * @param sheetName
     * @param bean
     */
    public static List<Object> readForObjectList(Map<String, List<Map<String, Object>>> allData,
                                                 String sheetName,Object bean) {
        try {
            List<Map<String, Object>> sheet = allData.get(sheetName);
            List<Object> res = new ArrayList<Object>();
            for (int i = 0; i < sheet.size(); i++) {
                res.add((Object) PopulateUtil.populate(BeanUtils.cloneBean(bean),sheet.get(i)));
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取<code>excel</code>文件一个页签中的数据到一个<code>list</code>中
     *
     * @param <T>
     * @param clazz
     * @param excelDir
     * @param sheetName
     * @param beanClazz
     */
    public static <T> List<T> readForObjectList(Class<?> clazz, String excelDir,String sheetName,Class<T> beanClazz){
        Map<String, List<Map<String, Object>>> allData = readData(clazz, excelDir);
        return readForObjectList(allData, sheetName, beanClazz);
    }

    /**
     * 读取<code>excel</code>文件一个页签中的数据到一个<code>list</code>中
     *
     * @param <T>
     * @param allData
     * @param sheetName
     * @param clazz
     */
    public static <T> List<T> readForObjectList(Map<String, List<Map<String, Object>>> allData,
                                                String sheetName,Class<T> clazz){
        List<Map<String, Object>> sheet = allData.get(sheetName);
        List<T> res = new ArrayList<T>();
        for(int i = 0 ; i < sheet.size() ; i ++) {
            try {
                res.add((T) PopulateUtil.populate(clazz, sheet.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 读取excle文件数据
     *
     * @param clazz 类定义
     * @param execlDir 文件路径
     */
    public static Map<String, List<Map<String, Object>>> readData(Class<?> clazz, String execlDir) {
        // key 页签名称  value 每一行数据 (key 列名 value 值)
        Map<String, List<Map<String, Object>>> allData
                = new HashMap<String, List<Map<String, Object>>>();
        List<Map<String, Object>> sheet = null;
        String excelRealPath = execlDir;
        IDataSet dataSet;
        InputStream inputStream = null;
        try {
            inputStream = clazz.getResourceAsStream(excelRealPath);
            dataSet = new XlsDataSet(inputStream);
            String[] allDataTable = dataSet.getTableNames();
            ITable dataTable = null;
            ITableMetaData meta = null;
            Column[] columns = null;
            Map<String, Object> row = null;
            String columnName = null;
            Object obj = null;
            // 读取每一个sheet页签
            for (int d = 0; d < allDataTable.length; d++) {
                dataTable = dataSet.getTable(allDataTable[d]);
                meta = dataTable.getTableMetaData();
                columns = meta.getColumns();
                sheet = new ArrayList<Map<String, Object>>();
                // 读取每一行
                for (int k = 0; k < dataTable.getRowCount(); k++) {
                    row =  Collections.synchronizedMap(new CamelCasingHashMap<String, Object>());;
                    for (int i = 0; i < columns.length; i++) {
                        columnName = columns[i].getColumnName();
                        obj = dataTable.getValue(k, columnName);
                        if(obj != null && "".equals(obj)) {
                            obj = null;
                        }
                        row.put(columnName, obj);
                    }
                    sheet.add(k, row);
                }

                allData.put(allDataTable[d], sheet);
            }
        } catch (Throwable  e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(inputStream != null) {
                try{
                    inputStream.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return allData;
    }

    /**
     * 获取页签
     *
     * @param clazz 类定义
     * @param execlDir 文件路径
     * @return String[]
     */
    public static String[] getTableNames(Class<?> clazz, String execlDir) {
        InputStream inputStream = null;
        IDataSet dataSet;
        String[] allDataTable = new String[0];
        try{
            inputStream = clazz.getResourceAsStream(execlDir);
            dataSet = new XlsDataSet(inputStream);
            allDataTable = dataSet.getTableNames();
        } catch(Throwable e){
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if(inputStream != null) {
                try{
                    inputStream.close();
                }
                catch(IOException e){
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        return allDataTable;
    }

    public static void writeExcel(String fileName, String sheetName){
        WritableWorkbook wwb = null;
        try {
            // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
            wwb = Workbook.createWorkbook(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if(wwb != null){
            //创建一个可写入的工作表
            //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
            WritableSheet ws = wwb.createSheet(sheetName, 0);
            //下面开始添加单元格
            for(int i = 0;i < 10;i++){
                for(int j = 0;j < 5; j++){
                    //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行
                    Label labelC = new Label(j, i, "这是第"+(i+1)+"行，第"+(j+1)+"列");
                    try {
                        //将生成的单元格添加到工作表中
                        ws.addCell(labelC);
                    } catch (RowsExceededException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    } catch (WriteException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                //从内存中写入文件中
                wwb.write();
                //关闭资源，释放内存
                wwb.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (WriteException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeExcelBean(String fileName, String sheetName, List<Object> list){
        WritableWorkbook wwb = null;
        try {
            // 首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象
            wwb = Workbook.createWorkbook(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if(wwb != null){
            //创建一个可写入的工作表
            //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置
            WritableSheet ws = wwb.createSheet(sheetName, 0);
            //下面开始添加单元格
            if(list != null && !list.isEmpty()) {
                // 写表头
                for(int i = 0; i < 1; i++) {
                    Object obj = list.get(i);
                    Method []methods = compareMethod(obj);
/*	            		for(int k = 0; k < methods.length; k++) {
	            			System.out.println(methods[k].getName());
	            		}*/
                    if(methods != null) {
                        int index = 0;
                        for(int j = 0; j < methods.length; j++) {
                            Method method = methods[j];
                            if(method.getName().startsWith("get") || method.getName().startsWith("is")) {
                                String value ="";
                                if(method.getName().startsWith("get")) {
                                    value = method.getName().substring(3, method.getName().length());
                                } else if(method.getName().startsWith("is")) {
                                    value = method.getName().substring(2, method.getName().length());
                                }
                                value = value.toUpperCase();
                                Label labelC = new Label(index++, i, value);
                                try {
                                    //将生成的单元格添加到工作表中
                                    ws.addCell(labelC);
                                } catch (RowsExceededException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                } catch (WriteException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
                // 写表体
                for(int i = 0; i < list.size(); i++) {
                    Object obj = list.get(i);
//		            	Method []methods = obj.getClass().getDeclaredMethods();
                    Method []methods = compareMethod(obj);
                    if(methods != null) {
                        int index = 0;
                        for(int j = 0; j < methods.length; j++) {
                            Method method = methods[j];
                            if(method.getName().startsWith("get") || method.getName().startsWith("is")) {
                                String value = getBeanMethodValue(obj, method);
                                Label labelC = new Label(index++, i + 1, value);
                                try {
                                    //将生成的单元格添加到工作表中
                                    ws.addCell(labelC);
                                } catch (RowsExceededException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                } catch (WriteException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }

                        }
                    }
                }
            }
            try {
                //从内存中写入文件中
                wwb.write();
                //关闭资源，释放内存
                wwb.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (WriteException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private static Method[] compareMethod(Object obj) {
        Method []orMethods = obj.getClass().getDeclaredMethods();
        List<Method> orMethodsList = Arrays.asList(orMethods);
        Collections.sort(orMethodsList, new Comparator<Method>(){
            public int compare(Method o1, Method o2){
                String name1 = o1.getName();
                String name2 = o2.getName();
                return name1.compareTo(name2);
            }
        });
        Method[] methods = orMethodsList.toArray(new Method[orMethodsList.size()]);
        return methods;
    }

    private static String getBeanMethodValue(Object target, Method method) {
        String value = "";
        Class<?> rType = method.getReturnType();
        try{
            if(rType == byte.class) {
                byte bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Byte.class);
                value = String.valueOf(bValue);
            } else if(rType == short.class) {
                short bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Short.class);
                value = String.valueOf(bValue);
            } else if(rType == int.class) {
                int bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Integer.class);
                value = String.valueOf(bValue);
            } else if(rType == long.class) {
                long bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Long.class);
                value = String.valueOf(bValue);
            } else if(rType == String.class) {
                String bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, String.class);
                if(bValue == null) {
                    bValue = "";
                } else {
                    value = String.valueOf(bValue);
                }
            } /*else if(rType == GUID.class) {
				GUID bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, GUID.class);
				if(bValue != null) {
					value = bValue.toString();
				} else {
					value = "";
				}*/
            else if(rType == boolean.class) {
                boolean bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Boolean.class);
                value = String.valueOf(bValue);
            } /*else if(rType == char.class) {
				Char bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Char.class);
				value = String.valueOf(bValue);
			}*/ else if(rType == double.class) {
                double bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Double.class);
                value = String.valueOf(bValue);
            } else if(rType == float.class) {
                float bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Float.class);
                value = String.valueOf(bValue);
            } else {
                Object bValue = ReflectionUtil.invokeMethod(target, method.getName(), method.getParameterTypes(), null, Object.class);
                if(bValue == null) {
                    value = "";
                } else {
                    value = String.valueOf(bValue);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            value = "";
        }
        return value;
    }
}
