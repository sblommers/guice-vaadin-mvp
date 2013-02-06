/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.localization;

import com.google.code.vaadin.localization.InjectBundle;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

/**
 * BundleLocaleTypeListener - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 07.02.13
 */
class ResourceBundleTypeListener implements TypeListener {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
        for (Field field : type.getRawType().getDeclaredFields()) {
            if (ResourceBundle.class.isAssignableFrom(field.getType())
                    && field.isAnnotationPresent(InjectBundle.class)) {
                InjectBundle injectBundle = field.getAnnotation(InjectBundle.class);
                encounter.register(new ResourceBundleInjector<T>(field, injectBundle));
            }
        }
    }
}