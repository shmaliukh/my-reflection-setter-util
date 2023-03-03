package xyz.vshmaliukh;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class MyAbstractSetterUtil implements SetterUtil {

    protected final LinkedHashSet<Class<?>> classTypeSet = new LinkedHashSet<>();

    public boolean setValue(boolean value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, boolean.class);
    }

    public boolean setValue(byte value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, byte.class);
    }

    public boolean setValue(short value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, short.class);
    }

    public boolean setValue(int value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, int.class);
    }

    public boolean setValue(long value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, long.class);
    }

    public boolean setValue(char value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, char.class);
    }

    public boolean setValue(float value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, float.class);
    }

    public boolean setValue(double value, Object targetObject, String propertyName) {
        return tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, double.class);
    }

    public boolean setValue(Object value, Object targetObject, String propertyName) {
        return value != null && tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, value.getClass());
    }

    public boolean setValue(Enum value, Object targetObject, String propertyName) {
        return value != null && tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, value.getClass());
    }

    public boolean setValue(Object value, Object targetObject, String propertyName, Class<?> valueType) {
        return valueType != null && tryToSetValueByPossibleClassTypes(value, targetObject, propertyName, valueType);
    }

    protected abstract boolean tryToSetValueByPossibleClassTypes(Object value, Object targetObject, String propertyName, Class<?> valueType);

    protected abstract boolean isSetterMethod(String propertyName, Method method);

    protected abstract String generateSetterName(String propertyName);

    protected boolean tryToSetValue(Object value, Object targetObject, String propertyName, Class<?> valueType) {
        Method setterMethod = findSetterMethod(targetObject, propertyName, valueType);
        return setterMethod != null && invokeMethod(setterMethod, targetObject, value);
    }

    protected Method findSetterMethod(Object targetObject, String propertyName, Class<?> valueType) {
        try {
            String setterName = generateSetterName(propertyName);
            //            setterMethod.setAccessible(true); // is needed because the method may be private
            return targetObject.getClass().getMethod(setterName, valueType);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    protected boolean invokeMethod(Method method, Object targetObject, Object value) {
        try {
            method.invoke(targetObject, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Set<Class<?>> getClassTypeSet() {
        return new LinkedHashSet<>(classTypeSet);
    }

}
