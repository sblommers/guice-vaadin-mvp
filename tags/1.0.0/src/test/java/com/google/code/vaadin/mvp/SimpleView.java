/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.inject.ImplementedBy;

/**
 * @author Alexey Krylov (lexx)
 * @since 18.03.13
 */
@ImplementedBy(SimpleViewImpl.class)
public interface SimpleView extends View{

    /*===========================================[ INTERFACE METHODS ]============*/

    void doSomething();
}