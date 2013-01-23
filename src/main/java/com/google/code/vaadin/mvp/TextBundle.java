/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

/**
 * Interface for a bundle implementation used for obtaining (localized) texts.
 * If implemented, can be used by CDI Utils.
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public interface TextBundle {

    /*===========================================[ INTERFACE METHODS ]==============*/

    String getText(String key, Object... params);
}
