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

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.mvp.events.LocaleChangedEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Superclass for views and their subcomponents.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
public abstract class ViewComponent extends CustomComponent {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 1527463270559690859L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected transient Logger logger;
    private transient ViewEventPublisher viewEventPublisher;

    @com.google.inject.Inject(optional = true)
    private TextBundle textBundle;
    private RequestContext requestContext;
    private boolean initialized;

    /*===========================================[ CLASS METHODS ]================*/

    @Inject
    protected final void init(ViewEventPublisher viewEventPublisher, RequestContext requestContext) {
        logger = LoggerFactory.getLogger(getClass());
        this.viewEventPublisher = viewEventPublisher;
        this.requestContext = requestContext;
    }

    protected final void init() {
        initialized = false;
        initComponent();
        initialized = true;
    }

    protected void initComponent(){

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

    /**
     * Returns the {@link Window} bound to the current request.
     *
     * @return
     */
    protected Window getContextWindow() {
        Window window = requestContext.getWindow();
        if (window == null) {
            window = getContextApplication().getMainWindow();
        }
        return window;
    }

    /**
     * Returns the {@link AbstractMVPApplication} bound to the current request.
     *
     * @return
     */
    protected AbstractMVPApplication getContextApplication() {
        return requestContext.getApplication();
    }

    @Observes
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