package com.example.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@Component(value="CommonUtil")
public class CommonUtil {

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj)
    {
        if( obj instanceof String ) return obj==null || "".equals(obj.toString().trim());
        else if( obj instanceof List ) return obj==null || ((List<?>)obj).isEmpty();
        else if( obj instanceof Map ) return obj==null || ((Map<Object, Object>)obj).isEmpty();
        else if( obj instanceof Object[] ) return obj==null || Array.getLength(obj)==0;
        else return obj==null;
    }

    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }

    public static boolean isEquals(Object sobj, Object tobj)
    {
        if(CommonUtil.isNotEmpty(sobj))
        {
            return sobj.equals(tobj);
        }
        return false;
    }
    public static boolean isNotEquals(Object sobj, Object tobj)
    {
        return !isEquals(sobj,tobj);
    }
}
