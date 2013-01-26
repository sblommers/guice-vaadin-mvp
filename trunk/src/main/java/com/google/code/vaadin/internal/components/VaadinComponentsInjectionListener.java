/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.components;

import com.google.code.vaadin.components.Preconfigured;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.vaadin.ui.Component;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;

/**
 * Listens for fields of type {@link Component} and marked with {@link Preconfigured} annotation.
 * For this kind of fields {@link VaadinComponentsInjector} will be registered.
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
class VaadinComponentsInjectionListener implements TypeListener {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private ServletContext servletContext;

	/*===========================================[ CONSTRUCTORS ]=================*/

    VaadinComponentsInjectionListener(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
            if (Component.class.isAssignableFrom(field.getType())
                    && field.isAnnotationPresent(Preconfigured.class)) {
                Preconfigured preconfigured = field.getAnnotation(Preconfigured.class);
                typeEncounter.register(new VaadinComponentsInjector<T>(field, preconfigured, servletContext));
            }
        }
    }
}