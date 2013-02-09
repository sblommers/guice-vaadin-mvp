/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit;

import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.inject.Inject;

/**
 * TestScopedUIProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 09.02.13
 */
public class TestScopedUIProvider extends ScopedUIProvider {
/*===========================================[ STATIC VARIABLES ]=============*/
/*===========================================[ INSTANCE VARIABLES ]===========*/
/*===========================================[ CONSTRUCTORS ]=================*/
/*===========================================[ CLASS METHODS ]================*/
    @Inject
    protected void initScope(){
        createInstance(UITestBase.TestUI.class);
    }
}
