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

package com.google.code.vaadin.internal.localization;

import com.google.code.vaadin.components.localization.EncodedControl;
import com.google.code.vaadin.localization.LocalizationConstants;
import com.google.code.vaadin.localization.ResourceBundleProvider;
import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Default implementation of {@link ResourceBundleProvider}.
 *
 * @author Alexey Krylov
 * @since 25.02.13
 */
class DefaultResourceBundleProvider implements ResourceBundleProvider {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public ResourceBundle getBundle(@NotNull @Size(min = 1) String baseName, Locale locale, String... encoding) {
        Preconditions.checkArgument(baseName != null && !baseName.isEmpty(), "Specified baseName is null or empty");

        String requestedEncoding = LocalizationConstants.DEFAULT_BUNDLE_ENCODING;
        if (encoding != null && encoding.length > 0 && !encoding[0].isEmpty()) {
            requestedEncoding = encoding[0];
        }

        return ResourceBundle.getBundle(baseName, locale, new EncodedControl(requestedEncoding));
    }
}