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
