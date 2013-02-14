/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.code.vaadin.MVPTestModule;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.eventhandling.SharedModelEventPublisher;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;
import com.google.inject.Stage;
import com.mycila.testing.plugin.guice.GuiceContext;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * SharedEventBusTest - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
@GuiceContext(value = {SharedEventBusReceiverModule.class, MVPTestModule.class}, stage = Stage.PRODUCTION)
public class SharedEventBusTest extends AbstractMVPTest{

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private SharedModelEventPublisher publisher;



	/*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testPublish() {
        publisher.publish(new SharedModelEvent());

        SharedEventBusReceiverService eventBusReceiverService = injector.getInstance(SharedEventBusReceiverService.class);
        int received = eventBusReceiverService.getSharedModelEventReceivedCount();
        Assert.assertEquals("Event was not received", 2, received);
    }
}
