/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.logging;

import com.google.inject.AbstractModule;
import com.google.inject.internal.Slf4jLoggerProvider;
import org.slf4j.Logger;

/**
 * LoggerModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class LoggerModule extends AbstractModule {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(Logger.class).toProvider(new Slf4jLoggerProvider());
    }
}