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

import com.google.code.vaadin.MVPApplicationTestModule;
import com.google.code.vaadin.junit.MVPTestRunner;
import com.google.code.vaadin.mvp.GlobalModelEventPublisher;
import com.google.code.vaadin.mvp.ModelEventPublisher;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.ViewEventPublisher;
import com.google.code.vaadin.mvp.events.GlobalModelEvent;
import com.google.code.vaadin.mvp.events.ModelEvent;
import com.google.code.vaadin.mvp.events.ViewEvent;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.annotation.GuiceModules;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * EventPublisherTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
@RunWith(MVPTestRunner.class)
@GuiceModules(modules = MVPApplicationTestModule.class)
public class EventBusComplexTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ViewEventPublisher viewEventPublisher;

    @Inject
    private ModelEventPublisher modelEventPublisher;

    @Inject
    private GlobalModelEventPublisher globalModelEventPublisher;

    @Inject
    private Injector injector;

    private int viewEventReceivedCount;
    private int modelEventReceivedCount;
    private int globalModelEventReceivedCount;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes
    public void viewEventReceived(ViewEvent viewEvent) {
        viewEventReceivedCount++;
    }

    @Observes
    public void modelEventReceived(ModelEvent modelEvent) {
        modelEventReceivedCount++;
    }

    @Observes
    public void globalModelEventReceived(GlobalModelEvent globalModelEvent) {
        globalModelEventReceivedCount++;
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
    public void globalModelEventPublisherNotNull() {
        assertNotNull("GlobalModelEventPublisher is null", globalModelEventPublisher);
        assertEquals("Different GlobalModelEventPublisher instances", injector.getInstance(GlobalModelEventPublisher.class), globalModelEventPublisher);
    }

    @Test
    public void globalModelEventPublisherIsSingleton() {
        Assert.assertTrue("GlobalModelEventPublisher is not singleton", Scopes.isSingleton(injector.getBinding(GlobalModelEventPublisher.class)));
    }

    @Test
    public void globalModelEventNotified() {
        globalModelEventPublisher.publish(new GlobalModelEvent());
        assertEquals("Event was not received", 1, globalModelEventReceivedCount);
    }

    @Test
    public void modelEventNotified() {
        modelEventPublisher.publish(new ModelEvent());
        assertEquals("Event was not received", 1,  modelEventReceivedCount);
    }

    @Test
    public void viewEventNotified() {
        viewEventPublisher.publish(new ViewEvent());
        assertEquals("Event was not received", 1, viewEventReceivedCount);
    }
}
