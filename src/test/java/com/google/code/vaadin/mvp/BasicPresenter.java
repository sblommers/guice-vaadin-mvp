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

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.events.ViewEvent;

/**
 * TestPresenter - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class BasicPresenter extends AbstractPresenter<BasicView> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 4440909913334550221L;

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private boolean eventReceived;
    private boolean viewOpened;

    /*===========================================[ CLASS METHODS ]================*/

    @Observes(EventType.VIEW)
    private void sampleButtonPressed(ViewEvent viewEvent) {
        eventReceived = true;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void initPresenter() {

    }

    @Override
    public void viewOpened() {
        viewOpened = true;
    }

    /*===========================================[ GETTER/SETTER ]================*/

    public boolean isEventReceived() {
        return eventReceived;
    }

    public boolean isViewOpened() {
        return viewOpened;
    }
}