/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;

import javax.inject.Inject;

/**
 * SharedEventBusTest - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class SharedEventBusReceiverService {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private int sharedModelEventReceivedCount;
    private NonScopedServiceComponent component;

	/*===========================================[ CLASS METHODS ]================*/

    @Inject
    private void init(@EventBuses.SharedModelEventBus EventBus eventBus, NonScopedServiceComponent component) {
        eventBus.subscribe(this);
        this.component = component;
        eventBus.subscribe(component);
    }

    @Observes(EventType.SHARED_MODEL)
    public void sharedModelEventReceived(SharedModelEvent sharedModelEvent) {
        sharedModelEventReceivedCount++;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public int getSharedModelEventReceivedCount() {
        return sharedModelEventReceivedCount + component.getSharedModelEventReceivedCount();
    }
}
