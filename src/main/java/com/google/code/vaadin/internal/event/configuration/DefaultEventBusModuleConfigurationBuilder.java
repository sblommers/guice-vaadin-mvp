/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.configuration;

/**
 * DefaultEventBusModuleConfigurationBuilder - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 13.02.13
 */
public class DefaultEventBusModuleConfigurationBuilder implements EventBusModuleConfigurationBuilder {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private boolean modelEventBusEnabled;
    private boolean sharedModelEventBusEnabled;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBusModuleConfigurationBuilder withModelEventBus() {
        modelEventBusEnabled = true;
        return this;
    }

    @Override
    public EventBusModuleConfigurationBuilder withSharedModelEventBus() {
        sharedModelEventBusEnabled = true;
        return this;
    }

    @Override
    public EventBusModuleConfiguration build() {
        DefaultEventBusModuleConfiguration configuration = new DefaultEventBusModuleConfiguration();
        configuration.setModelEventBusEnabled(modelEventBusEnabled);
        configuration.setSharedModelEventBusEnabled(sharedModelEventBusEnabled);
        return configuration;
    }
}