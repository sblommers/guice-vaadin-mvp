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

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.View;

/**
 * Provides {@link View} instance for specified {@link AbstractPresenter}.
 *
 * @author Alexey Krylov
 * @see AbstractPresenter#init(ViewProvider)
 * @since 14.02.13
 */
public interface ViewProvider {

	/*===========================================[ CLASS METHODS ]================*/

    <V extends View> V getView(AbstractPresenter<V> presenter);
}
