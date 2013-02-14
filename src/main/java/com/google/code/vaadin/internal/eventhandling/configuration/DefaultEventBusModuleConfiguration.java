/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.configuration;

/**
 * DefaultEventBusModuleConfiguration - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 13.02.13
 */
class DefaultEventBusModuleConfiguration implements EventBusModuleConfiguration {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private boolean modelEventBusEnabled;
    private boolean sharedModelEventBusEnabled;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public boolean isModelEventBusRequired() {
        return modelEventBusEnabled;
    }

    @Override
    public boolean isSharedModelEventBusRequired() {
        return sharedModelEventBusEnabled;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    void setSharedModelEventBusEnabled(boolean sharedModelEventBusEnabled) {
        this.sharedModelEventBusEnabled = sharedModelEventBusEnabled;
    }

    public boolean isModelEventBusEnabled() {
        return modelEventBusEnabled;
    }

    void setModelEventBusEnabled(boolean modelEventBusEnabled) {
        this.modelEventBusEnabled = modelEventBusEnabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DefaultEventBusModuleConfiguration");
        sb.append("{modelEventBusEnabled=").append(modelEventBusEnabled);
        sb.append(", sharedModelEventBusEnabled=").append(sharedModelEventBusEnabled);
        sb.append('}');
        return sb.toString();
    }
}
