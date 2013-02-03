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

package com.google.code.vaadin.internal.logging;

import com.google.inject.AbstractModule;
import com.google.inject.internal.Slf4jLoggerProvider;
import org.slf4j.Logger;

import javax.inject.Inject;

/**
 * Adds support of Slf4j logger injection via {@link com.google.inject.Inject} and {@link Inject} annotations.
 *
 * @author Alexey Krylov
 * @since 25.01.13
 */
public class LoggerModule extends AbstractModule {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(Logger.class).toProvider(new Slf4jLoggerProvider());
    }
}