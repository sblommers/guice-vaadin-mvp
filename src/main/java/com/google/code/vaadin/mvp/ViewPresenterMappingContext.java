/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

/**
 * IViewPresenterMappingContext - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 28.01.13
 */
public interface ViewPresenterMappingContext {

    /*===========================================[ INTERFACE METHODS ]==============*/

    <P extends AbstractPresenter<V>, V extends View> P getPresenterForView(V view);
}
