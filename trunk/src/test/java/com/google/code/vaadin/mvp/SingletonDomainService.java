/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.eventhandling.SharedModelEventPublisher;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
@Singleton
public class SingletonDomainService {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private SharedModelEventPublisher modelEventPublisher;

    /*===========================================[ CLASS METHODS ]================*/

    public void doSomething() {
        modelEventPublisher.publish(new SharedModelEvent());
    }
}
