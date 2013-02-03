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

import com.google.code.vaadin.TextBundle;
import com.google.inject.servlet.SessionScoped;

import java.util.Locale;

/**
 * Lang - TODO: description
 *
 * @author Alexey Krylov
 * @since 31.01.13
 */
@SessionScoped
public class Lang implements TextBundle {

	/*===========================================[ STATIC VARIABLES ]=============*/

    public static final Locale EN_US = new Locale("en", "US");
    public static final Locale RU_RU = new Locale("ru", "RU");

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Locale locale = RU_RU;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public String getText(String key, Object... params) {
        return key + "-" + locale.getLanguage();
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}