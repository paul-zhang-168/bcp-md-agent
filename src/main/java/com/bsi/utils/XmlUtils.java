package com.bsi.utils;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.Map.Entry;

/**
 * xml工具类
 * @author fish
 */
@Slf4j
public class XmlUtils {

    private static XStream xmlStream = new XStream(new Dom4JDriver());

    static {
        xmlStream.registerConverter( new MapEntryConverter());
    }

    /**
     * 对象转xml文本
     * @param obj
     * @return
     */
    public static String toXml(Object obj){
        return xmlStream.toXML( obj );
    }

    /**
     * json文件转换成xml文本
     * @param json
     * @return
     */
    public static String json2Xml(String json){
        Object obj = JSON.parseArray(json);
        return xmlStream.toXML( obj );
    }

    /**
     * xml文本转换成json文本
     * @param xml
     * @return
     */
    public static String xml2json(String xml){
        return JSON.toJSONString(xmlStream.fromXML(xml) );
    }

    /**
     * converter类
     */
    static class MapEntryConverter implements Converter{
        public MapEntryConverter(){
            super();
        }

        @Override
        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            log.info( JSON.toJSONString(value) );
            map2xml(value,writer,context);
        }
        /**
         * map转换成xml
         * @param value
         * @param writer
         * @param context
         */
        private void map2xml(Object value, HierarchicalStreamWriter writer,
                               MarshallingContext context) {
            Class cls = value.getClass();
            if(AbstractList.class.isAssignableFrom(cls)){
                log.info("list");
                List<Object> list = (List<Object>) value;
                for (Object v : list) {
                    write(writer,"child",v,context);
                }
            }else{
                Map<String,Object> map = (Map<String, Object>) value;
                Iterator it = map.entrySet().iterator();
                while( it.hasNext() ){
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                    write(writer,entry.getKey(),entry.getValue(),context);
                }
            }
        }

        /**
         * 节点输出
         * @param writer
         * @param k
         * @param v
         * @param context
         */
        private void write(HierarchicalStreamWriter writer,Object k,Object v,MarshallingContext context){
            writer.startNode(k.toString());
            if (v instanceof String) {
                writer.setValue((String) v);
            } else {
                map2xml(v, writer, context);
            }
            writer.endNode();
        }

        protected Object populateMap(HierarchicalStreamReader reader,
                                     UnmarshallingContext context) {
            boolean mapFlag = true;
            Map<String, Object> map = new HashMap<>();
            List<Object> list = new ArrayList<>();
            while (reader.hasMoreChildren()) {
                reader.moveDown();
                String key = reader.getNodeName();
                Object value = null;
                if (reader.hasMoreChildren()) {
                    value = populateMap(reader, context);
                } else {
                    value = reader.getValue();
                }
                if (mapFlag) {
                    if (map.containsKey(key)) {
                        mapFlag = false;
                        Iterator<Entry<String, Object>> iter = map.entrySet()
                                .iterator();
                        while (iter.hasNext())
                            list.add(iter.next().getValue());
                        list.add(value);
                    } else {
                        map.put(key, value);
                    }
                } else {
                    list.add(value);
                }
                reader.moveUp();
            }
            if (mapFlag)
                return map;
            else
                return list;
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            log.info("unmarshal");
            return populateMap(reader,context);
        }

        @Override
        public boolean canConvert(Class aClass) {
            return AbstractMap.class.isAssignableFrom(aClass)||AbstractList.class.isAssignableFrom(aClass);
        }
    }
    public static  void main(String[] ars){

//        List<AgDataSource> list = new ArrayList<>();
//        AgDataSource ds = new AgDataSource();
//        ds.setConfigValue("config");
//        ds.setName("fish");
//
//        AgDataSource ds1 = new AgDataSource();
//        ds1.setConfigValue("config");
//        ds1.setName("fish");
//        list.add(ds);
//        list.add(ds1);
//        String json = JSON.toJSONString(list);
//        System.out.println( toXml(ds) );
//        System.out.println( json );
//        System.out.println( json2Xml(json) );

//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "\n" +
//                "<com.alibaba.fastjson.JSONArray>\n" +
//                "  <list>\n" +
//
//                "    <tt>\n" +
//                "      <name>fish</name>\n" +
//                "      <configValue>config</configValue>\n" +
//                "    </tt>\n" +
//                "  </list>\n" +
//                "</com.alibaba.fastjson.JSONArray>";
//        log.info( xml2json( xml ) );
    }
}
