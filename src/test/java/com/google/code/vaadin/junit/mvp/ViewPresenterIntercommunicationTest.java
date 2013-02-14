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

import com.google.code.vaadin.internal.mapping.ViewPresenterMappingRegistry;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.*;
import com.google.code.vaadin.mvp.eventhandling.SharedModelEventPublisher;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * ViewPresenterIntercommunicationTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 28.01.13
 */
public class ViewPresenterIntercommunicationTest extends AbstractMVPTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private TestView view;

    @Inject
    private BasicView basicView;

    @Inject
    private SharedModelEventPublisher sharedModelEventPublisher;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testIntercommunication() {
        view.openView();

        ViewPresenterMappingRegistry mappingRegistry = injector.getInstance(ViewPresenterMappingRegistry.class);
        TestPresenter presenter = mappingRegistry.getPresenterForView(view);
        Assert.assertTrue("ViewOpenedEvent was not received", presenter.isViewOpened());

        view.openContact();

        Assert.assertTrue("ContactOpenedEvent was not received", presenter.isContactOpened());
        Assert.assertTrue("DomainEvent was not received", presenter.isDomainEventReceived());

        sharedModelEventPublisher.publish(new SharedModelEvent());
        Assert.assertTrue("SharedModelEvent was not received", presenter.isSharedModelEventReceived());
    }

    @Test
    public void testIntercommunicationWithViewWithoutInterface() {
        basicView.openView();

        ViewPresenterMappingRegistry mappingRegistry = injector.getInstance(ViewPresenterMappingRegistry.class);
        BasicPresenter presenter = mappingRegistry.getPresenterForView(basicView);
        Assert.assertTrue("ViewOpenedEvent was not received", presenter.isViewOpened());

        basicView.sampleButtonPressed();

        Assert.assertTrue("SampleEvent was not received", presenter.isEventReceived());
    }
}