/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.localization;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * ResourceBundleInjectionModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 07.02.13
 */
public class ResourceBundleInjectionModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new ResourceBundleTypeListener());
    }
}
