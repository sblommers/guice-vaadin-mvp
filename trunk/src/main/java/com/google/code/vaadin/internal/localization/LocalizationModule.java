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

package com.google.code.vaadin.internal.localization;

import com.google.code.vaadin.localization.InjectBundle;
import com.google.code.vaadin.localization.ResourceBundleProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;

import java.util.ResourceBundle;

/**
 * Support module for {@link ResourceBundle} injections via {@link InjectBundle} annotation.
 *
 * @author Alexey Krylov
 * @since 07.02.13
 */
public class LocalizationModule extends AbstractModule {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(ResourceBundleProvider.class).to(DefaultResourceBundleProvider.class).in(Scopes.SINGLETON);
        bindListener(Matchers.any(), new ResourceBundleTypeListener());
    }
}