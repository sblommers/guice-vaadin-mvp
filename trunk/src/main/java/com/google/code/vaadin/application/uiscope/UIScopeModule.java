/*
 * Copyright (C) 2013 David Sowerby
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.google.code.vaadin.application.uiscope;

import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.vaadin.server.UIProvider;

public class UIScopeModule extends AbstractModule {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private final UIScope uiScope;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public UIScopeModule() {
        uiScope = UIScope.getCurrent();
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void configure() {
        // tell Guice about the scope
        bindScope(UIScoped.class, uiScope);

        // make our scope instance injectable
        bind(UIScope.class).annotatedWith(Names.named("UIScope")).toInstance(uiScope);
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public UIScope getUiScope() {
        return uiScope;
    }
}