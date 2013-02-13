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

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.localization.TextBundle;
import com.google.code.vaadin.mvp.events.LocaleChangedEvent;
import com.vaadin.ui.CustomComponent;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Base class for views and their subcomponents.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
public abstract class ViewComponent extends CustomComponent {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 1527463270559690859L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    protected transient Logger logger;

    @Inject
    private transient ViewEventPublisher viewEventPublisher;

    @com.google.inject.Inject(optional = true)
    private TextBundle textBundle;
    private boolean initialized;

    /*===========================================[ CLASS METHODS ]================*/

    public void init() {
        initialized = false;
        initComponent();
        initialized = true;
    }

    protected void initComponent() {

    }

    protected String getText(String key, Object... params) {
        if (textBundle != null) {
            return textBundle.getText(key, params);
        } else {
            return String.format("%s: No TextBundle implementation found!", key);
        }
    }

    protected void fireViewEvent(@NotNull Object event) {
        viewEventPublisher.publish(event);
    }

    @Observes(EventType.VIEW)
    void localeChanged(LocaleChangedEvent localeChangedEvent) {
        if (initialized) {
            localize();
        }
    }

    /**
     * Override to localize the view. Firing a {@link LocaleChangedEvent} event will eventually invoke this method
     */
    protected void localize() {
    }

    protected boolean isInitialized() {
        return initialized;
    }

    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}