/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;

/**
 * NonScopedServiceComponent - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class NonScopedServiceComponent {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private int sharedModelEventReceivedCount;

	/*===========================================[ CLASS METHODS ]================*/

    @Observes(EventType.SHARED_MODEL)
    public void sharedModelEventReceived(SharedModelEvent sharedModelEvent) {
        sharedModelEventReceivedCount++;
    }

    public int getSharedModelEventReceivedCount() {
        return sharedModelEventReceivedCount;
    }
}
