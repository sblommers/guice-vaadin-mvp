/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.spi;

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.components.Preconfigured;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.vaadin.ui.Component;

import java.lang.reflect.Field;

/**
 * VaadinControlsInjectionListener - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.01.13
 */
public class VaadinComponentsInjectionListener implements TypeListener {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject(optional = true)
    private TextBundle textBundle;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
        for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(Component.class)
                    && field.isAnnotationPresent(Preconfigured.class)) {
                Preconfigured preconfigured = field.getAnnotation(Preconfigured.class);
                typeEncounter.register(new VaddinComponentsInjector<T>(field, preconfigured, textBundle));
            }
        }
    }
}