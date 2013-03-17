/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.code.vaadin.components.mapping.ViewPresenterMappingRegistry;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedEventBusSubscribersRegistry;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.PresenterWithSharedModel;
import com.google.code.vaadin.mvp.SimpleView;
import com.vaadin.util.CurrentInstance;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
public class SharedEventBusAfterUIDetachTest extends AbstractMVPTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private SimpleView view;

    @Inject
    private SharedEventBusSubscribersRegistry subscribersRegistry;

    private UIKey uiKey;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testAutoUnsubscription() {
        view.open();

        ViewPresenterMappingRegistry mappingRegistry = injector.getInstance(ViewPresenterMappingRegistry.class);
        PresenterWithSharedModel presenter = mappingRegistry.getPresenter(view);
        Assert.assertNotNull(presenter);

        uiKey = CurrentInstance.get(UIKey.class);
        Assert.assertEquals(1, subscribersRegistry.getSubscribers(uiKey).size());
    }

    @Override
    public void cleanup() {
        super.cleanup();
        Assert.assertTrue(subscribersRegistry.getSubscribers(uiKey).isEmpty());
    }
}