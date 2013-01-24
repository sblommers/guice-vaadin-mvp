/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.util;

import com.google.common.base.Preconditions;
import net.jcip.annotations.ThreadSafe;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility for resolving actual classes of type parameters.
 *
 * @author Alexey Krylov (lexx)
 * @see #getFirstTypeParameterClass(Class)
 * @see #getTypeParameterClass(Class, int)
 * @see #getTypeParameterClass(Class, Class)
 * @since 23.01.13
 */
@ThreadSafe
public class TypeUtil {

    /*===========================================[ CONSTRUCTORS ]===============*/

    private TypeUtil() {
    }

    /*===========================================[ CLASS METHODS ]==============*/

    /**
     * Finds and returns class of first parametrization parameter.
     *
     * @param aClass generic class
     *
     * @return parameter class or {@code null} if parameter can't be found
     *
     * @throws IllegalArgumentException if specified {@code aClass} is null
     */
    public static Class getFirstTypeParameterClass(Class aClass) {
        Preconditions.checkArgument(aClass != null);
        return getTypeParameterClass(aClass, 0);
    }

    /**
     * Finds and returns class of N parametrization parameter.
     *
     * @param aClass         generic class
     * @param parameterIndex parameter index
     *
     * @return parameter class or {@code null} if parameter can't be found
     *
     * @throws IllegalArgumentException if specified {@code aClass} is null or {@code parameterIndex} &lt; 0
     */
    public static Class getTypeParameterClass(Class aClass, int parameterIndex) {
        Preconditions.checkArgument(aClass != null);
        Preconditions.checkArgument(parameterIndex >= 0);

        List<Type> types = new ArrayList<Type>();

        // check interfaces
        getGenericInterfacesActualTypes(types, aClass);

        Class result = findAppropriateType(types, parameterIndex);
        if (result == null) {
            types.clear();
            // check superclasses
            getGenericSuperclassActualTypes(types, aClass);
        }
        return findAppropriateType(types, parameterIndex);
    }

    private static Class findAppropriateType(List<Type> types, int parameterIndex) {
        for (int i = 0; i < types.size(); i++) {
            if (i == parameterIndex) {
                Type type = types.get(i);
                if (type instanceof Class) {
                    return (Class) type;
                }
            }
        }
        return null;
    }

    /**
     * Finds and returns class of specified generic parametrization class.
     *
     * @param aClass                generic class
     * @param genericParameterClass generic parametrization class
     *
     * @return parameter class or {@code null} if parameter can't be found
     *
     * @throws IllegalArgumentException if specified {@code aClass}  or {@code genericParameterClass} is null
     */
    public static <T> Class<T> getTypeParameterClass(Class aClass, Class<T> genericParameterClass) {
        Preconditions.checkArgument(aClass!=null);
        Preconditions.checkArgument(genericParameterClass != null);

        List<Type> types = new ArrayList<Type>();

        // check interfaces
        getGenericInterfacesActualTypes(types, aClass);

        Class result = findAppropriateType(types, genericParameterClass);
        if (result == null) {
            types.clear();
            // check superclasses
            getGenericSuperclassActualTypes(types, aClass);
        }
        return findAppropriateType(types, genericParameterClass);
    }

    private static <T> Class<T> findAppropriateType(Collection<Type> types, Class<T> genericParameterClass) {
        for (Type type : types) {
            if (type instanceof Class && genericParameterClass.isAssignableFrom((Class<?>) type)) {
                return (Class) type;
            }
        }
        return null;
    }

    public static void getGenericInterfacesActualTypes(Collection<Type> types, Class aClass) {
        if (aClass != null && types != null) {
            Type[] interfaces = aClass.getGenericInterfaces();
            for (Type anInterface : interfaces) {
                if (anInterface instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) anInterface;
                    Type[] actualTypes = parameterizedType.getActualTypeArguments();
                    types.addAll(Arrays.asList(actualTypes));
                } else if (anInterface instanceof Class) {
                    Class typeClass = (Class) anInterface;
                    getGenericInterfacesActualTypes(types, typeClass);
                }
            }
        }
    }

    public static void getGenericSuperclassActualTypes(Collection<Type> types, Class aClass) {
        if (aClass != null && types != null) {
            Type superclass = aClass.getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) superclass;
                Type[] interfaces = parameterizedType.getActualTypeArguments();
                types.addAll(Arrays.asList(interfaces));
            } else if (superclass instanceof Class) {
                Class sClass = (Class) superclass;
                getGenericInterfacesActualTypes(types, sClass);
                getGenericSuperclassActualTypes(types, aClass.getSuperclass());
            }
        }
    }
}