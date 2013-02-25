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

import com.google.code.vaadin.internal.localization.LocalizationModule;

import java.lang.annotation.*;
import java.util.ResourceBundle;

/**
 * {@link ResourceBundle} marked with this annotation will be injected automatically.
 * This is very useful for situations when {@link ResourceBundle#getBundle(String)} is not working propertly due to
 * bundle encoding specifics.
 *
 * @author Alexey Krylov
 * @see LocalizationModule
 * @since 07.02.13
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectBundle {
    /**
     * @return the base name of the resource bundle
     */
    String baseName();

    /**
     * @return encoding of the resource bundle
     */
    String encoding() default LocalizationConstants.DEFAULT_ENCODING;
}