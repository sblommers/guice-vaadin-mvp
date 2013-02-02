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

package com.google.code.vaadin.junit.preconfigured;

import com.google.code.vaadin.MVPApplicationTestModule;
import com.google.code.vaadin.components.Preconfigured;
import com.google.code.vaadin.junit.MVPTestRunner;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nnsoft.guice.junice.annotation.GuiceModules;

import javax.inject.Inject;

/**
 * PreconfiguredInjectionTest - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
@RunWith(MVPTestRunner.class)
@GuiceModules(modules = MVPApplicationTestModule.class)
public class PreconfiguredInjectionTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @Preconfigured(nullSelectionAllowed = true, immediate = true)
    private Table table;

    @Inject
    @Preconfigured(caption = "Begin")
    private Button button;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testComponentParametersValidity() {
        Assert.assertTrue("Property 'nullSelectionAllowed' is not applied", table.isNullSelectionAllowed());
        Assert.assertTrue("Property 'immediate' is not applied", table.isImmediate());
        Assert.assertEquals("Property 'caption' is not applied", "Begin", button.getCaption());
    }
}
