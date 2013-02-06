/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.localization;

import com.google.code.vaadin.localization.InjectBundle;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.MembersInjector;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

/**
 * ResourceBundleInjector - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 07.02.13
 */
class ResourceBundleInjector<T> implements MembersInjector<T> {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Field field;
    private InjectBundle injectBundle;

	/*===========================================[ CONSTRUCTORS ]=================*/

    ResourceBundleInjector(Field field, InjectBundle injectBundle) {
        this.field = field;
        this.injectBundle = injectBundle;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        try {
            field.setAccessible(true);
            field.set(instance, ResourceBundle.getBundle(injectBundle.baseName(), new EncodedControl(injectBundle.encoding())));
        } catch (Exception e) {
            throw new MVPApplicationException(e);
        }
    }
}

