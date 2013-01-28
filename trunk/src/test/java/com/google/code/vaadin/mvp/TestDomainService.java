/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.events.DomainEvent;

import javax.inject.Inject;

/**
 * TestDomainService - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 28.01.13
 */
public class TestDomainService {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ModelEventPublisher modelEventPublisher;

	/*===========================================[ CLASS METHODS ]================*/

    public void doSomething() {
        modelEventPublisher.publish(new DomainEvent());
    }
}
