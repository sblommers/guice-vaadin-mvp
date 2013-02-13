/*
 * Copyright (C) 2013 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.vaadin.junit.mvp;

import com.google.code.vaadin.internal.util.ScopesResolver;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.eventhandling.*;
import com.google.code.vaadin.mvp.events.SharedModelEvent;
import com.google.code.vaadin.mvp.events.ModelEvent;
import com.google.code.vaadin.mvp.events.ViewEvent;
import com.google.inject.Scopes;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * EventPublisherTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class EventBusComplexTest extends AbstractMVPTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ViewEventPublisher viewEventPublisher;

    @Inject
    private ModelEventPublisher modelEventPublisher;

    @Inject
    private SharedModelEventPublisher sharedModelEventPublisher;

    private int viewEventReceivedCount;
    private int modelEventReceivedCount;
    private int sharedModelEventReceivedCount;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    public void viewEventReceived(ViewEvent viewEvent) {
        viewEventReceivedCount++;
    }

    @Observes(EventType.MODEL)
    public void modelEventReceived(ModelEvent modelEvent) {
        modelEventReceivedCount++;
    }

    @Observes(EventType.SHARED_MODEL)
    public void sharedModelEventReceived(SharedModelEvent sharedModelEvent) {
        sharedModelEventReceivedCount++;
    }

    @Test
    public void viewEventPublisherNotNull() {
        assertNotNull("ViewEventPublisher is null", viewEventPublisher);
    }

    @Test
    public void modelEventPublisherNotNull() {
        assertNotNull("ModelEventPublisher is null", modelEventPublisher);
    }

    @Test
    public void viewEventPublisherIsUIScoped() {
        assertNotNull("ViewEventPublisher is not UIScoped", ScopesResolver.isUIScoped(injector.getBinding(ViewEventPublisher.class)));
    }

    @Test
    public void modelEventPublisherIsUIScoped() {
        assertNotNull("ModelEventPublisher is not UIScoped", ScopesResolver.isUIScoped(injector.getBinding(ModelEventPublisher.class)));
    }

    @Test
    public void modelEventNotified() {
        modelEventPublisher.publish(new ModelEvent());
        assertEquals("Event was not received", 1, modelEventReceivedCount);
    }

    @Test
    public void viewEventNotified() {
        viewEventPublisher.publish(new ViewEvent());
        assertEquals("Event was not received", 1, viewEventReceivedCount);
    }

    @Test
    public void sharedModelEventPublisherNotNull() {
        assertNotNull("SharedModelEventPublisher is null", sharedModelEventPublisher);
    }

    @Test
    public void sharedModelEventPublisherEquals() {
        assertEquals("Different SharedModelEventPublisher instances", injector.getInstance(SharedModelEventPublisher.class), sharedModelEventPublisher);
    }

    @Test
    public void sharedModelEventPublisherIsSingleton() {
        Assert.assertTrue("SharedModelEventPublisher is not singleton", Scopes.isSingleton(injector.getBinding(SharedModelEventPublisher.class)));
    }

    @Test
    public void sharedModelEventNotified() {
        sharedModelEventPublisher.publish(new SharedModelEvent());
        assertEquals("Event was not received", 1, sharedModelEventReceivedCount);
    }
}
