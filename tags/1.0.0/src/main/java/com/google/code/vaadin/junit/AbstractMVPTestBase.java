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

package com.google.code.vaadin.junit;

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.inject.Injector;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.util.CurrentInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;

/**
 * Base class for all JUnit-tests related to UI.
 *
 * @author Alexey Krylov
 * @since 09.02.2013
 */
@RunWith(MycilaJunitRunner.class)
public abstract class AbstractMVPTestBase {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    protected VaadinRequest mockedRequest;
    protected VaadinSession mockedSession;

    protected Logger logger;
    protected ScopedUIProvider uiProvider;
    protected Injector injector;

    protected UI ui;
    private boolean isFirstTest;

	/*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    void init(ScopedUIProvider uiProvider, Injector injector) {
        logger = LoggerFactory.getLogger(getClass());

        this.uiProvider = uiProvider;
        this.injector = injector;
        isFirstTest = true;
        mockedRequest = mock(VaadinRequest.class);
        mockedSession = mock(VaadinSession.class);

        ui = CurrentInstance.get(UI.class);
        ui.setSession(mockedSession);
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Before
    public void uiSetup() {
        // for the first test UI is initialized in the 'init' method above
        if (!isFirstTest) {
            logger.info("initialising test");

            ui = createTestUI(getTestUIClass());
            CurrentInstance.set(UI.class, ui);
            ui.doInit(mockedRequest, 1);
            ui.setSession(mockedSession);
        }
    }

    protected ScopedUI createTestUI(Class<? extends ScopedUI> uiClass) {
        return (ScopedUI) uiProvider.createInstance(uiClass);
    }

    @After
    public void cleanup() {
        ui.detach();

        if (isFirstTest) {
            isFirstTest = false;
        }
    }

    protected abstract Class<? extends ScopedUI> getTestUIClass();
}