/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.util;

import com.google.inject.Binding;
import com.google.inject.Scope;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.DefaultBindingScopingVisitor;

/**
 * GuiceHelper - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class GuiceHelper {

	/*===========================================[ CONSTRUCTORS ]=================*/

    private GuiceHelper() {
    }

	/*===========================================[ CLASS METHODS ]================*/

    public static boolean isScopedWith(Binding presenterBinding, final Scope... scopes) {
        final boolean[] validScoped = {false};
        presenterBinding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Object>() {
            @Override
            public Object visitScope(Scope scope) {
                for (Scope s : scopes) {
                    if (scope.equals(s)) {
                        validScoped[0] = true;
                        break;
                    }
                }

                return super.visitScope(scope);
            }
        });
        return validScoped[0];
    }
}
