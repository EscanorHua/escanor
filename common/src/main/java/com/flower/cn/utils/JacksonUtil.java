package com.flower.cn.utils;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class JacksonUtil // 一些文件格式之间转换的函数。
{
    private static final Logger log = LoggerFactory.getLogger(JacksonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static XmlMapper xmlMapper = new XmlMapper();

    static
    {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd ")); // 设置默认日期格式
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false)); //提供其它默认设置
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
       // objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        xmlMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        xmlMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // 转换对象到json 格式字符串，如果转换失败 返回null
    public final static String toJsonString(Object object, String... properties)
    {
        try 
        {
            SimpleFilterProvider fileter = new SimpleFilterProvider();
            fileter.addFilter(AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class).value(), SimpleBeanPropertyFilter.filterOutAllExcept(properties));
            return objectMapper.writer(fileter).writeValueAsString(object);
        } catch (JsonProcessingException e) { log.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + e); return null; }
    }

    public final static String toJsonString(Object object)
    {
        try { return objectMapper.writeValueAsString(object); } 
        catch (JsonProcessingException e) { log.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + e); return null; }
    }

    public static String toXmlString(Object object) {
        try {
            return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("转换对象【" + object.toString() + "】到xml格式字符串失败：" + e); return null;
        }
    }

    // json格式字符串转换成对象
    public final static  <T> T json2Object (String json, Class<T> clazz)
    {
        try { return objectMapper.readValue(json,clazz); } 
        catch (IOException e) { log.error("转换json【" + json + "】到对象"+ clazz.toString() + "失败：" + e); return null; }
    }

    public final static <T> List<T> json2ObjectList(String json, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("转换json【" + json + "】到对象"+ clazz.toString() + "失败：" + e);
            return null;
        }
    }

    public final static  <T> T xml2Object (String json, Class<T> clazz)
    {
        try { return xmlMapper.readValue(json,clazz); }
        catch (IOException e) { log.error("转换xml【" + json + "】到对象"+ clazz.toString() + "失败：" + e); return null; }
    }
    
    public static String toJsonString(Object object, Class<?> rootType){
        try { return objectMapper.writerFor(rootType).writeValueAsString(object); } 
        catch (JsonProcessingException e) { log.error("转换对象【" + object.toString() + "】到json格式字符串失败：" + e); return null; }
    
    }

    public static ObjectMapper getOjm() {
        return objectMapper;
    }

    public static <T> T mapToObject(Map<String,Object> map, Class<T> clazz){
        return  objectMapper.convertValue(map,clazz);
    }

    public static final void main(String... args) {
        String test = "";
    }

}