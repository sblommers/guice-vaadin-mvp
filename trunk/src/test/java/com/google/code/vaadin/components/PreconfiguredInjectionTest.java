/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.components;

import com.google.code.vaadin.guice.TestMVPApplicationModule;
import com.vaadin.ui.Table;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.JUniceRunner;
import org.nnsoft.guice.junice.annotation.GuiceModules;

import javax.inject.Inject;

/**
 * PreconfiguredInjectionTest - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.01.13
 */
@RunWith(JUniceRunner.class)
@GuiceModules(modules = TestMVPApplicationModule.class)
public class PreconfiguredInjectionTest {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @Preconfigured(nullSelectionAllowed = true, immediate = true)
    public  Table table;

	/*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testComponentParametersValidity() {
        Assert.assertTrue("Property 'nullSelectionAllowed' is not applied", table.isNullSelectionAllowed());
        Assert.assertTrue("Property 'immediate' is not applied", table.isImmediate());
    }
}
