/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.util;

import com.google.inject.Injector;

import javax.servlet.ServletContext;

/**
 * InjectorProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class InjectorProvider {

    /*===========================================[ CONSTRUCTORS ]=================*/

    private InjectorProvider() {
    }

    /*===========================================[ CLASS METHODS ]================*/

    public static Injector getInjector(ServletContext context) {
        return (Injector) context.getAttribute(Injector.class.getName());
    }
}
