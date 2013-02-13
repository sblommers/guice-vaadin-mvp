/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.View;

/**
 * ViewProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public interface ViewProvider {

	/*===========================================[ CLASS METHODS ]================*/

    <V extends View> V getView(AbstractPresenter<V> presenter);
}
