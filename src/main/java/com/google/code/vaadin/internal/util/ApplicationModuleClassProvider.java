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

import com.google.code.vaadin.guice.AbstractMVPApplicationModule;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.mvp.MVPApplicationException;

import javax.servlet.ServletContext;

/**
 * ApplicationModuleClassProvider - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class ApplicationModuleClassProvider {

    /*===========================================[ CONSTRUCTORS ]=================*/

    private ApplicationModuleClassProvider() {
    }

    /*===========================================[ CLASS METHODS ]================*/

    public static Class<? extends AbstractMVPApplicationModule> getApplicationModuleClass(ServletContext context) {
        try {
            return (Class<? extends AbstractMVPApplicationModule>) Class.forName(context.getInitParameter(MVPApplicationInitParameters.P_APPLICATION_MODULE));
        } catch (Exception e) {
            throw new MVPApplicationException(String.format("ERROR: Unable to instantiate class of [%s]. " +
                    "Please check your webapp deployment descriptor.", AbstractMVPApplicationModule.class.getName()), e);
        }
    }
}