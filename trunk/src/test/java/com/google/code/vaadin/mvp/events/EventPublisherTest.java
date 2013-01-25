/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.events;

import com.google.code.vaadin.internal.event.EventPublisherModule;
import com.google.code.vaadin.mvp.EventPublisher;
import com.google.code.vaadin.mvp.EventPublisher;
import com.google.code.vaadin.mvp.Observes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.JUniceRunner;
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
@RunWith(JUniceRunner.class)
@GuiceModules(modules = EventPublisherModule.class)
public class EventPublisherTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private EventPublisher eventPublisher;


    private boolean eventNotified;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    public void when(SampleEvent viewEvent) {
        eventNotified = true;
    }

    @Test
    public void eventPublisherNotNull() {
        assertNotNull("Event publisher is null", eventPublisher);
    }

    @Test
    public void eventNotified() {
        eventPublisher.publish(new SampleEvent());
        assertTrue("Event was not received", eventNotified);
    }
}
