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
 * ViewComponent - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public abstract class ViewComponent extends CustomComponent {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 1527463270559690859L;

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    protected transient Logger logger;

    @Inject
    private EventPublisher eventPublisher;

    @com.google.inject.Inject(optional = true)
    private TextBundle textBundle;

    @Inject
    private RequestContext requestContext;

	/*===========================================[ CLASS METHODS ]================*/

    protected String getText(String key, Object... params) {

        if (textBundle != null) {
            return textBundle.getText(key, params);
        } else {
            return "No TextBundle implementation found!";
        }
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