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

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.code.vaadin.MVPTestModule;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.eventhandling.*;
import com.google.code.vaadin.mvp.eventhandling.events.ModelEvent;
import com.google.inject.Stage;
import com.mycila.testing.plugin.guice.GuiceContext;
import net.engio.mbassy.IMessageBus;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Alexey Krylov
 * @since 14.02.13
 */
@GuiceContext(value = {MVPTestModule.class}, stage = Stage.PRODUCTION)
public class ModelEventBusTest extends AbstractMVPTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ModelEventPublisher publisher;

    @Inject
    @EventBusType(EventBusTypes.MODEL)
    private EventBus eventBus;

    @Inject
    @EventBusType(EventBusTypes.MODEL)
    private IMessageBus messageBus;


    @Inject
    @EventBusType(EventBusTypes.MODEL)
    private EventPublisher eventPublisher;
    private int eventCounter;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testComponentsInjection() {
        Assert.assertNotNull("ModelEventPublisher not null", publisher);
        Assert.assertNotNull("Model MessageBus is null", messageBus);
        Assert.assertNotNull("Model EventBus is null", eventBus);
        Assert.assertNotNull("Model EventPublisher is null", eventPublisher);
    }

    @Observes(EventType.MODEL)
    protected void on(ModelEvent event) {
        eventCounter++;
    }

    @Test
    public void testFireEvent() {
        publisher.publish(new ModelEvent());
        eventBus.publish(new ModelEvent());
        messageBus.post(new ModelEvent()).now();
        eventPublisher.publish(new ModelEvent());

        Assert.assertEquals("Invalid received event count", 4, eventCounter);
    }
}