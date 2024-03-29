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

package com.google.code.vaadin.localization;

import com.google.code.vaadin.components.localization.EncodedControl;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Allows quick and conveniet access to project's Resources Bundles.
 * Bundles will be created with special UTF-8 characters support - {@link EncodedControl}.
 *
 * @author Alexey Krylov
 * @since 25.02.13
 */
public interface ResourceBundleProvider {

    /*===========================================[ INTERFACE METHODS ]==============*/

    /**
     * Provides instance of {@link ResourceBundle} for specified locale and optional encoding.
     * Bundle will be created with special UTF-8 characters support - {@link EncodedControl}.
     *
     * @param baseName bundle base name
     * @param locale   required bundle locale
     * @param encoding optional encoding. Default is {@link LocalizationConstants#DEFAULT_BUNDLE_ENCODING}.
     *
     * @throws IllegalArgumentException if {@code baseName} is null or empty.
     */
    ResourceBundle getBundle(@NotNull @Size(min = 1) String baseName, Locale locale, String... encoding);
}