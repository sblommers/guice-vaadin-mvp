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

package com.google.code.vaadin.junit.mvp;

import com.google.code.vaadin.internal.util.ScopesResolver;
import com.google.code.vaadin.junit.MVPTestRunner;
import com.google.code.vaadin.mvp.Lang;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.sun.xml.internal.ws.client.RequestContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * ViewPresenterIntercommunicationTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 28.01.13
 */
@RunWith(MVPTestRunner.class)
public class ScopesResolverTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Injector injector;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testScopesHelper() {
        //todo detect notscoped components. what if nonscoped component will be injected into the singleton
        Binding<Lang> langBinding = injector.getBinding(Lang.class);
        Assert.assertFalse("Lang is RequestScoped", ScopesResolver.isRequestScoped(langBinding));
        Assert.assertTrue("Lang is not UIScoped", ScopesResolver.isUIScoped(langBinding));

        RequestContext instance = injector.getInstance(RequestContext.class);
        instance.toString();
    }
}