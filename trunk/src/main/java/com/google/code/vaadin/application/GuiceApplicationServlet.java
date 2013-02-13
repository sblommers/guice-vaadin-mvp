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

package com.google.code.vaadin.application;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.server.*;

/**
 * Constructs a new Application instance for each user.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
@Singleton
class GuiceApplicationServlet extends VaadinServlet implements SessionInitListener {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 1469564555655570905L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    /**
     * Cannot use constructor injection. Container expects servlet to have no-arg public constructor
     */
    @Inject
    private UIProvider basicProvider;

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void servletInitialized() {
        getService().addSessionInitListener(this);
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addUIProvider(basicProvider);
    }
}