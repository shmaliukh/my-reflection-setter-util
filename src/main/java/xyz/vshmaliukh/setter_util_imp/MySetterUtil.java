package xyz.vshmaliukh.setter_util_imp;

import org.apache.commons.lang3.StringUtils;
import xyz.vshmaliukh.MyAbstractSetterUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static xyz.vshmaliukh.PrimitiveTypeUtil.*;

public class MySetterUtil extends MyAbstractSetterUtil {

    /*
        // primitive to setter
        | boolean   | => | boolean   | Boolean | Object  | -      | -       | -         | -      | -      | -      |
        | byte      | => | byte      | short   | int     | long   | float   | double    | Byte   | Number | Object |
        | short     | => | short     | int     | long    | float  | double  | Byte      | Number | Object | -      |
        | int       | => | int       | long    | float   | double | Integer | Number    | Object | -      | -      |
        | long      | => | long      | float   | double  | Long   | Number  | Object    | -      | -      | -      |
        | char      | => | char      | int     | long    | float  | double  | Character | Object | -      | -      |
        | float     | => | float     | double  | Float   | Number | Object  | -         | -      | -      | -      |
        | double    | => | double    | Double  | Number  | Object | -       | -         | -      | -      | -      |
        // wrapper to setter
        | Boolean   | => | Boolean   | Object  | boolean | -      | -       | -         | -      | -      | -      |
        | Byte      | => | Byte      | Number  | Object  | byte   | short   | int       | long   | float  | double |
        | Short     | => | Short     | Number  | Object  | short  | int     | long      | float  | double | -      |
        | Integer   | => | Integer   | Number  | Object  | int    | long    | float     | double | -      | -      |
        | Long      | => | Long      | Number  | Object  | long   | float   | double    | -      | -      | -      |
        | Character | => | Integer   | Number  | Object  | int    | long    | float     | double | -      | -      |
        | Float     | => | Float     | Number  | Object  | float  | double  | -         | -      | -      | -      |
        | Double    | => | Double    | Number  | Object  | double | -       | -         | -      | -      | -      |
        // object to setter
        | MyNumber2 | => | MyNumber  | Number  | Object  | -      | -       | -         | -      | -      | -      |
    */

    @Override
    protected boolean tryToSetValueByPossibleClassTypes(Object value, Object targetObject, String propertyName, Class<?> valueType) {
        if (targetObject != null && StringUtils.isNotBlank(propertyName)) {
            createListOfAvailableClasses(valueType);
            for (Class<?> classType : classTypeSet) {
                if (tryToSetValue(value, targetObject, propertyName, classType)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void createListOfAvailableClasses(Class<?> valueType) {
        boolean isWrapperType = isWrapper(valueType);
        boolean isPrimitiveType = isPrimitive(valueType);
        if (isWrapperType) {
            classTypeSet.addAll(getClassHierarchyList(valueType));
            Class<?> primitiveByWrapperPrimitiveClass = getPrimitiveByWrapperClass(valueType);
            classTypeSet.addAll(getPrimitiveClassesToCheck(primitiveByWrapperPrimitiveClass));
        } else if (isPrimitiveType) {
            classTypeSet.addAll(getPrimitiveClassesToCheck(valueType));
            Class<?> wrapperByPrimitiveClass = getWrapperByPrimitiveClass(valueType);
            classTypeSet.addAll(getClassHierarchyList(wrapperByPrimitiveClass));
        } else {
            classTypeSet.addAll(getClassHierarchyList(valueType));
        }
    }

    protected static List<Class<?>> getPrimitiveClassesToCheck(Class<?> valueType) {
        List<Class<?>> primitiveClassesToCheckList = new ArrayList<>();
        primitiveClassesToCheckList.add(valueType);
        primitiveClassesToCheckList.addAll(PRIMITIVE_WIDENING_MAP.getOrDefault(valueType, Collections.emptyList()));
        return primitiveClassesToCheckList;
    }

    protected boolean isSetterMethod(String propertyName, Method method) {
        String expectedSetterName = generateSetterName(propertyName);
        return method.getName().equals(expectedSetterName) && method.getParameterCount() == 1;
    }

    protected String generateSetterName(String propertyName) {
        return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }

}
