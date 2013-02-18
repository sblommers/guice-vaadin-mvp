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

import com.google.code.vaadin.MVPTestModuleWithTextBundle;
import com.google.code.vaadin.components.Preconfigured;
import com.google.code.vaadin.junit.AbstractMVPTest;
import com.google.code.vaadin.mvp.Lang;
import com.google.code.vaadin.mvp.TestView;
import com.google.code.vaadin.mvp.eventhandling.ViewEventPublisher;
import com.google.code.vaadin.mvp.eventhandling.events.LocaleChangedEvent;
import com.google.inject.Stage;
import com.mycila.testing.plugin.guice.GuiceContext;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Alexey Krylov
 * @since 28.01.13
 */
@GuiceContext(value = {MVPTestModuleWithTextBundle.class}, stage = Stage.PRODUCTION)
public class LocaleChangeTest extends AbstractMVPTest{

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @Preconfigured(labelValueKey = "label")
    private Label label;

    @Inject
    @Preconfigured(captionKey = "window")
    private Window window;

    @Inject
    private ViewEventPublisher viewEventPublisher;

    @Inject
    private TestView view;

    @Inject
    private Lang lang;

    /*===========================================[ CLASS METHODS ]================*/

    @Test
    public void testLocaleChange() {
        Assert.assertEquals("Invalid label value", "label-" + Lang.RU_RU.getLanguage(), label.getValue());
        Assert.assertEquals("Invalid window caption", "window-" + Lang.RU_RU.getLanguage(), window.getCaption());

        Assert.assertFalse("Received unexpected LocaleChangedEvent", view.isLocaleChangedEventReceived());
        lang.setLocale(Lang.EN_US);
        view.openView();
        viewEventPublisher.publish(new LocaleChangedEvent(Lang.EN_US));

        Assert.assertEquals("Invalid label value", "label-" + Lang.EN_US.getLanguage(), label.getValue());
        Assert.assertEquals("Invalid window caption", "window-" + Lang.EN_US.getLanguage(), window.getCaption());
        Assert.assertTrue("Not received unexpected LocaleChangedEvent", view.isLocaleChangedEventReceived());
    }
}