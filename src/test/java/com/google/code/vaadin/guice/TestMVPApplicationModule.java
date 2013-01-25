/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice;

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.internal.components.VaadinComponentPreconfigurationModule;
import com.google.code.vaadin.internal.event.EventPublisherModule;
import com.google.code.vaadin.internal.logging.LoggerModule;

/**
 * TestMVPApplicationModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class TestMVPApplicationModule extends AbstractMVPApplicationModule {

    /*===========================================[ CONSTRUCTORS ]=================*/

    public TestMVPApplicationModule() {
        super(null);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void bindComponents() {
        install(new LoggerModule());
        install(new EventPublisherModule());
        install(new VaadinComponentPreconfigurationModule());
    }

    @Override
    protected void bindTextBundle() {
        bind(TextBundle.class).toInstance(new TextBundle() {
            @Override
            public String getText(String key, Object... params) {
                return String.format(key, params);
            }
        });
    }
}
