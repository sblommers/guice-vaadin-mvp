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

import com.google.code.vaadin.MVPTestModule;
import com.google.code.vaadin.TestUI;
import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.inject.Stage;
import com.mycila.testing.plugin.guice.GuiceContext;

/**
 * TestMVPTestBase - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 10.02.13
 */
@GuiceContext(value = MVPTestModule.class, stage = Stage.PRODUCTION)
public abstract class AbstractMVPTest extends AbstractMVPTestBase {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected Class<? extends ScopedUI> getTestUIClass() {
        return TestUI.class;
    }
}
