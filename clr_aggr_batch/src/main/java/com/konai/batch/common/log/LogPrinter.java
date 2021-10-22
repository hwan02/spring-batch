package com.konai.batch.common.log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Project : springbootmybatis
 * Developer : jbhan
 * Date : 2017-02-08
 */
public class LogPrinter {

    private static ObjectMapper objectMapper;
//    @Autowired
//    public ObjectMapper objectMapper;
    /**
     * Prints the log vo.
     *
     * @param vo the vo
     * @return the string
     */
    public static String printLogVO(Object vo){

    	if ( vo == null ){
    		return "null";
    	}

        Class<? extends Object> clazz = vo.getClass();

        StringBuffer sb = new StringBuffer();

        List<Field> fieldList = new ArrayList<Field>();

        List<Field> paramClazzList = new ArrayList<Field>();

        if( vo.getClass().getSimpleName().equals(ArrayList.class.getSimpleName())){
        	for ( Object obj: (List<Object>) vo ){
        		if(sb.length() != 0){
        			sb.append(", ");
        		}

				sb.append("{")
				.append(LogPrinter.printLogVO(obj))
				.append("}");
        	}
    	}

        if(vo.getClass().getSuperclass() != null)
        {
            fieldList.addAll(getSuperFiled(vo.getClass().getSuperclass()));
        }

        paramClazzList =  new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
        fieldList.addAll(paramClazzList);

        for (Field field : fieldList) {

        	if (field.getAnnotation(LogParam.class) == null) {
        		continue;
        	}

        	field.setAccessible(true);

            Object value;

            try {
                value = field.get(vo);
            } catch (IllegalArgumentException e) {
                continue;
            } catch (IllegalAccessException e) {
                continue;
            }

            if (sb.length() != 0) {
                sb.append(" ");
            }

            sb.append(field.getName());
            sb.append("[");
            if (value != null) {
                sb.append(value.toString());
            } else {
                sb.append("null");
            }
            sb.append("]");
//			}
        }
        return sb.toString();
    }

    /**
     * Gets the super filed.
     *
     * @param clazz the clazz
     * @return the super filed
     */
    private static List<Field> getSuperFiled(Class<? extends Object> clazz) {
        List<Field> fieldList = new ArrayList<Field>();

        List<Field> clazzList = new ArrayList<Field>();

        Class<? extends Object> superclazz = clazz.getSuperclass();
        if(superclazz != null)
        {
            fieldList.addAll(getSuperFiled(superclazz));
        }

        clazzList = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));

        fieldList.addAll(clazzList);

        return fieldList;

    }

    public static String toParamString(Object item) {
		StringBuffer result = new StringBuffer();

		Class<? extends Object> cls = item.getClass();
		for (Field field: cls.getDeclaredFields()){
			field.setAccessible(true);
			if(field.getAnnotation(LogVO.class) != null){
				if(result.length() != 0){
					result.append(" ");
				}

				if( !StringUtils.isEmpty(field.getName()) ){
					Object logVo = null;
					result.append(field.getName());
					result.append("[");

					try {
						logVo = FieldUtils.readField(field, item);
					} catch (IllegalAccessException e) {
						result.append("null");
					}

					if ( logVo == null ){
						result.append("null");
					} else {
						result.append(LogPrinter.printLogVO(logVo));
					}
					result.append("]");
					}

				} else if ( field.getAnnotation(LogParam.class) != null ) {
					if(result.length() != 0){
						result.append(" ");
				}
				Object value;

				try {
					value = field.get(item);
				} catch (IllegalArgumentException e) {
					continue;
				} catch (IllegalAccessException e) {
					continue;
				}

				result.append(field.getName());
				result.append("[");
				if (value != null) {
					result.append(value.toString());
				} else {
					result.append("null");
				}
				result.append("]");
			}
		}

		return result.toString();
	}


}

