/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.internal.event.EventPublisherModule;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.JUniceRunner;
import org.nnsoft.guice.junice.annotation.GuiceModules;

import javax.inject.Inject;

/**
 * ClassMateTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
@RunWith(JUniceRunner.class)
@GuiceModules(modules = EventPublisherModule.class)
public class InstantiationTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private TestPresenter testPresenter;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testClassMate() {
        Assert.assertNotNull("Presenter is null", testPresenter);
    }
}
