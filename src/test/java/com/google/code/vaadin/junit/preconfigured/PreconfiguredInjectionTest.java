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

import com.google.code.vaadin.components.preconfigured.Preconfigured;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class PreconfiguredInjectionTest extends AbstractMVPTest {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @Preconfigured(nullSelectionAllowed = true, immediate = true)
    private Table table;

    @Inject
    @Preconfigured(caption = "Begin")
    private Button button;

    @Inject
    @Preconfigured(caption = "testLabel")
    private Label label;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testComponentParametersValidity() {
        Assert.assertTrue("Property 'nullSelectionAllowed' is not applied", table.isNullSelectionAllowed());
        Assert.assertTrue("Property 'immediate' is not applied", table.isImmediate());
        Assert.assertEquals("Property 'caption' is not applied", "Begin", button.getCaption());
        Assert.assertEquals("testLabel", label.getCaption());
    }

}
