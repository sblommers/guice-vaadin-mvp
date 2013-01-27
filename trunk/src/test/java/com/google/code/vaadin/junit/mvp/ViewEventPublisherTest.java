/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp;

import com.google.code.vaadin.MVPApplicationTestModule;
import com.google.code.vaadin.junit.MVPTestRunner;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.ViewEventPublisher;
import com.google.code.vaadin.mvp.events.SampleEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.annotation.GuiceModules;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * EventPublisherTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
@RunWith(MVPTestRunner.class)
@GuiceModules(modules = MVPApplicationTestModule.class)
public class ViewEventPublisherTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ViewEventPublisher viewEventPublisher;

    private boolean eventNotified;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    public void when(SampleEvent viewEvent) {
        eventNotified = true;
    }

    @Test
    public void eventPublisherNotNull() {
        assertNotNull("Event publisher is null", viewEventPublisher);
    }

    @Test
    public void eventNotified() {
        viewEventPublisher.publish(new SampleEvent());
        assertTrue("Event was not received", eventNotified);
    }
}
