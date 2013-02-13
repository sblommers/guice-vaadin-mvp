/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.configuration;

/**
 * EventBusModuleConfiguration - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 13.02.13
 */
public interface EventBusModuleConfiguration {

	/*===========================================[ INTERFACE METHODS ]============*/

    boolean isModelEventBusRequired();

    boolean isSharedModelEventBusRequired();
}
