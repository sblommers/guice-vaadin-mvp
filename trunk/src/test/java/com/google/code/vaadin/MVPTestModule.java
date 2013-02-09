/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.junit.AbstractMVPApplicationTestModule;

/**
 * MVPTestModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 10.02.13
 */
public class MVPTestModule extends AbstractMVPApplicationTestModule {

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected Class<? extends ScopedUI> getTestUIClass() {
        return TestUI.class;
    }
}
