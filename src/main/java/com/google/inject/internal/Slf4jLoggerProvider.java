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

package com.google.inject.internal;


import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.logging.Level;

/**
 * Provider for {@link Logger}. Injection will be perfomed with default @Inject annotations.
 *
 * @author Alexey Krylov)
 * @since 25.01.13
 */
public class Slf4jLoggerProvider implements Provider<Logger> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Injector injector;
    private java.util.logging.Logger logger;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    public void init(Injector injector, java.util.logging.Logger logger) {
        this.injector = injector;
        this.logger = logger;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public Logger get() {
        try {
            return ((InjectorImpl) injector).callInContext(new ContextualCallable<Logger>() {
                @Override
                public Logger call(InternalContext context) {
                    InjectionPoint injectionPoint = context.getDependency().getInjectionPoint();
                    Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
                    return LoggerFactory.getLogger(declaringClass);
                }
            });
        } catch (ErrorsException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    }
}