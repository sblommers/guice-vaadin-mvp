/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit;

import com.google.code.vaadin.MVPTestModule;
import com.google.code.vaadin.TestUI;
import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.inject.Stage;
import com.mycila.testing.plugin.guice.GuiceContext;

/**
 * TestMVPTestBase - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 10.02.13
 */
@GuiceContext(value = MVPTestModule.class, stage = Stage.PRODUCTION)
public abstract class AbstractMVPTest extends AbstractMVPTestBase {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected Class<? extends ScopedUI> getTestUIClass() {
        return TestUI.class;
    }
}
