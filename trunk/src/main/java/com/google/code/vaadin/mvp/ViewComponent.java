/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.application.AbstractMVPApplication;
import com.google.code.vaadin.mvp.application.RequestData;
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
public class ViewComponent extends CustomComponent {

    @Inject
    protected transient Logger logger;

    @Inject
    private EventPublisher eventPublisher;

    @com.google.inject.Inject(optional = true)
    private TextBundle textBundle;

    @Inject
    private RequestData requestData;


    protected String getText(String key, Object... params) {

        if (textBundle != null) {
            return textBundle.getText(key, params);
        } else {
            return "No TextBundle implementation found!";
        }
    }

    protected void fireViewEvent(Object event) {
        Preconditions.checkNotNull(event, "Published event can't be null");
        eventPublisher.publish(event);
    }

    /**
     * Returns the {@link Window} bound to the current request.
     *
     * @return
     */
    protected Window getContextWindow() {
        Window window = requestData.getWindow();
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
        return requestData.getApplication();
    }
}