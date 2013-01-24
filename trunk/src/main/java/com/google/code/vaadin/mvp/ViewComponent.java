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
import com.google.code.vaadin.application.AbstractMVPApplication;
import com.google.code.vaadin.application.RequestContext;
import com.google.code.vaadin.mvp.event.EventPublisher;
import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Window;

import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Superclass for views and their subcomponents.
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public abstract class ViewComponent extends CustomComponent {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 1527463270559690859L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected transient Logger logger;
    private EventPublisher eventPublisher;
    private TextBundle textBundle;
    private RequestContext requestContext;

    /*===========================================[ CLASS METHODS ]================*/

    @Inject
    protected void init(Logger logger, EventPublisher eventPublisher, TextBundle textBundle, RequestContext requestContext) {
        this.logger = logger;
        this.eventPublisher = eventPublisher;
        this.textBundle = textBundle;
        this.requestContext = requestContext;
    }

    protected String getText(String key, Object... params) {
        return textBundle.getText(key, params);
    }

    protected void fireViewEvent(Object event) {
        Preconditions.checkArgument(event != null, "Event can't be null");
        eventPublisher.publish(event);
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
}