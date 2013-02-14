/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * SharedEventBusReceiverModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class SharedEventBusReceiverModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
       bind(SharedEventBusReceiverService.class).in(Scopes.SINGLETON);
    }
}
