/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin;

import com.google.code.vaadin.guice.TestMVPApplicationModule;
import com.google.code.vaadin.mvp.TestPresenter;
import com.google.inject.Injector;
import com.google.inject.servlet.MVPTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.annotation.GuiceModules;

import javax.inject.Inject;

/**
 * ClassMateTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
//@RunWith(JUniceRunner.class)
@RunWith(MVPTestRunner.class)
@GuiceModules(modules = TestMVPApplicationModule.class)
public class InstantiationTest {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Injector injector;

	/*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testPresenterInjection() throws Exception {
        Assert.assertNotNull("Presenter is null", injector.getInstance(TestPresenter.class));
    }
}
