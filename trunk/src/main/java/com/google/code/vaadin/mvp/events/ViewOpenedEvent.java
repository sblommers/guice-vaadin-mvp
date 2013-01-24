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

package com.google.code.vaadin.mvp.events;

import com.google.code.vaadin.mvp.View;

/**
 * ViewOpenedEvent - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 24.01.13
 */
public class ViewOpenedEvent {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private final View view;
    private final Class<? extends View> viewInterface;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public ViewOpenedEvent(Class<? extends View> viewInterface, View view) {
        this.viewInterface = viewInterface;
        this.view = view;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public View getView() {
        return view;
    }

    public Class<? extends View> getViewInterface() {
        return viewInterface;
    }
}
