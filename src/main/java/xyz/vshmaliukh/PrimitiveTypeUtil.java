package xyz.vshmaliukh;

import java.util.*;
import java.util.stream.Collectors;

public final class PrimitiveTypeUtil {

    /*
        Primitive  =>   Wrapper   =>    Widening Primitive Class
        boolean	        Boolean	        N/A
        byte	        Byte	        short, int, long, float, double
        short	        Short	        int, long, float, double
        int	            Integer	        long, float, double
        long	        Long	        float, double
        float	        Float	        double
        double	        Double	        N/A
        char	        Character	    int, long, float, double
    */

    public static Map<Class<?>, List<Class<?>>> PRIMITIVE_WIDENING_MAP = new HashMap<>();

    static {
        PRIMITIVE_WIDENING_MAP.put(boolean.class, Collections.emptyList());
        PRIMITIVE_WIDENING_MAP.put(byte.class, Arrays.asList(short.class, int.class, long.class, float.class, double.class));
        PRIMITIVE_WIDENING_MAP.put(short.class, Arrays.asList(int.class, long.class, float.class, double.class));
        PRIMITIVE_WIDENING_MAP.put(int.class, Arrays.asList(long.class, float.class, double.class));
        PRIMITIVE_WIDENING_MAP.put(long.class, Arrays.asList(float.class, double.class));
        PRIMITIVE_WIDENING_MAP.put(float.class, Collections.singletonList(double.class));
        PRIMITIVE_WIDENING_MAP.put(double.class, Collections.emptyList());
        PRIMITIVE_WIDENING_MAP.put(char.class, Arrays.asList(int.class, long.class, float.class, double.class));
    }

    public static Class<?> getPrimitiveByWrapperClass(Class<?> valueType) {
        try {
            return (Class<?>) valueType.getField("TYPE").get(null);
        } catch (Exception e) {
//            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static boolean isWrapper(Class<?> classType) {
        List<Class<?>> classHierarchyList = getClassHierarchyList(classType);
        List<Class<?>> primitiveClassList = classHierarchyList.stream()
                .map(PrimitiveTypeUtil::getPrimitiveByWrapperClass)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return !primitiveClassList.isEmpty();
    }

    public static boolean isPrimitive(Class<?> classType) {
        return classType.isPrimitive();
    }

    public static List<Class<?>> getClassHierarchyList(Class<?> classType) {
        List<Class<?>> hierarchyList = new ArrayList<>();
        hierarchyList.add(classType);
        Class<?> superClass = classType.getSuperclass();
        while (superClass != null) {
            hierarchyList.add(superClass);
            superClass = superClass.getSuperclass();
        }
        return hierarchyList;
    }

    public static Class<?> getWrapperByPrimitiveClass(Class<?> primitiveClassType) {
        if (primitiveClassType.isPrimitive()) {
            try {
                String wrapperClassName = getWrapperNameByPrimitiveName(primitiveClassType.getName());
                return Class.forName("java.lang." + wrapperClassName);
            } catch (ClassNotFoundException e) {
                // TODO log.error();
            }
        }
        return primitiveClassType;
    }

    public static String getWrapperNameByPrimitiveName(String primitiveClassName) {
        switch (primitiveClassName) {
            case "int":
                return "Integer";
            case "char":
                return "Character";
            default:
                return Character.toUpperCase(primitiveClassName.charAt(0)) + primitiveClassName.substring(1);
        }
    }

}
