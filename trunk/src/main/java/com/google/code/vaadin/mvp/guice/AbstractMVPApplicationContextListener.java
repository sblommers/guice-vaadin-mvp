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

package com.google.code.vaadin.mvp.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import com.vaadin.Application;

/**
 * AbstractMVPApplicationConfig - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public class AbstractMVPApplicationContextListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {

        ServletModule module = new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/*").with(GuiceApplicationServlet.class);

                bind(Application.class).to(MyApplication.class).in(ServletScopes.SESSION);
                bindConstant().annotatedWith(Names.named("welcome")).to("This is my first Vaadin/Guice Application");
            }
        };

        Injector injector = Guice.createInjector(module);
        // Поддержка жизненного цикла компонентов - @PostConstruct и т.п.
        lifecycleManager = injector.getInstance(LifecycleManager.class);
        lifecycleManager.start();
        lifecycleManager.close();

        LifecycleInjector.builder().withModules(modules).createInjector();


        return injector;
    }
}
