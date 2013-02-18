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

import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.eventhandling.ViewEventPublisher;
import com.google.code.vaadin.mvp.eventhandling.events.ViewEvent;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class ViewEventPublisherTest extends AbstractMVPTest{

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ViewEventPublisher viewEventPublisher;

    private boolean eventNotified;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes(EventType.VIEW)
    public void when(ViewEvent viewEvent) {
        eventNotified = true;
    }

    @Test
    public void eventPublisherNotNull() {
        assertNotNull("Event publisher is null", viewEventPublisher);
    }

    @Test
    public void eventNotified() {
        viewEventPublisher.publish(new ViewEvent());
        assertTrue("Event was not received", eventNotified);
    }
}
