/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp;

import com.google.code.vaadin.components.mapping.ViewPresenterMappingRegistry;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.PresenterWithSharedModel;
import com.google.code.vaadin.mvp.SimpleView;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
public class PresenterEventReceivingFromSharedModelTest extends AbstractMVPTest{

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private SimpleView view;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testSharedModelEventFromViewInvocation(){
        view.open();

        ViewPresenterMappingRegistry mappingRegistry = injector.getInstance(ViewPresenterMappingRegistry.class);
        PresenterWithSharedModel presenter = mappingRegistry.getPresenter(view);
        Assert.assertNotNull(presenter);

        view.doSomething();
        
        Assert.assertEquals(1, presenter.getReceivedSharedModelEventsCounter());
        Assert.assertEquals(1, presenter.getReceivedModelEventsCounter());
    }
}