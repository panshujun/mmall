package com.mmall.util;

import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

/**
 * @author Pan shujun
 * @version V1.0.0
 * Description description
 * @date 2018/011/28 13:43
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 序列化

        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        // 所有的日期格式都记为以下格式 "yyyy-MM-dd HH:mm:ss"
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 反序列化
    }
    //将对象序列化为String
    public static <T> String obj2String(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            log.warn("Parse String to Object error");
            return null;
        }
    }
    public static <T> String obj2StringPretty(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }catch (Exception e){
            log.warn("Parse String to Object error");
            return null;
        }
    }

    //用来格式化封装好的String
    public static <T> T string2Obj(String str, Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz == null){
            return  null;
        }
        try {
            return clazz.equals(String.class)? (T)str :objectMapper.readValue(str,clazz);
        }catch (Exception e){
            log.warn("Parse String to Object error",e);
            return  null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> tTypeReference){
        if (StringUtils.isEmpty(str) || tTypeReference == null){
            return  null;
        }
        try {
            return (T)(tTypeReference.getType().equals(String.class)? str :objectMapper.readValue(str,tTypeReference));
        }catch (Exception e){
            log.warn("Parse String to Object error",e);
            return  null;
        }
    }
        public static void  main(String [] orgs){

            User user =new User();
            user.setUsername("psj");
            user.setId(1);
            String json =JsonUtil.obj2String(user);
            String json2 =JsonUtil.obj2StringPretty(user);
            JsonUtil.string2Obj(json, User.class);
            log.info(json);
            log.info(json2);

        }
}
