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

package com.google.code.vaadin.internal.util;

import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.mvp.AbstractMVPApplication;
import com.google.code.vaadin.mvp.MVPApplicationException;

import javax.servlet.ServletContext;

/**
 * //todo
 * Provides application implementation of {@link }MVPApplication
 *
 * @author Alexey Krylov
 * @since 25.01.13
 */
public class ApplicationClassProvider {

    /*===========================================[ CONSTRUCTORS ]=================*/

    private ApplicationClassProvider() {
    }

    /*===========================================[ CLASS METHODS ]================*/

    public static Class<? extends AbstractMVPApplication> getApplicationClass(ServletContext context) {
        try {
            return (Class<? extends AbstractMVPApplication>) Class.forName(context.getInitParameter(MVPApplicationInitParameters.P_APPLICATION));
        } catch (Exception e) {
            throw new MVPApplicationException(String.format("ERROR: Unable to instantiate class of [%s]. " +
                    "Please check your webapp deployment descriptor.", AbstractMVPApplication.class.getName()), e);
        }
    }
}