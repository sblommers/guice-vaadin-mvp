/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.components;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * AbstractMVPApplicationModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.01.13
 */
public class VaadinComponentPreconfigurationModule extends AbstractModule {

    /*===========================================[ INTERFACE METHODS ]============*/
    @Override
    protected void configure() {
        // support for @Preconfigured
        bindListener(Matchers.any(), new VaadinComponentsInjectionListener());
    }
}