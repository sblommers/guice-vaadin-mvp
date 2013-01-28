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

package com.google.code.vaadin.junit.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;

/**
 * ServletTestUtils - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 27.01.13
 */
public class ServletTestUtils {

    /*===========================================[ CONSTRUCTORS ]=================*/

    private ServletTestUtils() {
    }

    /*===========================================[ CLASS METHODS ]================*/

    /**
     * Returns a FilterChain that does nothing.
     */
    public static FilterChain newNoOpFilterChain() {
        return new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) {
            }
        };
    }

    /**
     * Returns a fake, HttpServletRequest which stores attributes in a HashMap.
     */
    public static HttpServletRequest newFakeHttpServletRequest() {
        HttpServletRequest delegate = (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class[]{HttpServletRequest.class}, new ThrowingInvocationHandler());

        return new HttpServletRequestWrapper(delegate) {
            final Map<String, Object> attributes = Maps.newHashMap();
            final HttpSession session = newFakeHttpSession();

            @Override
            public String getMethod() {
                return "GET";
            }

            @Override
            public Object getAttribute(String name) {
                return attributes.get(name);
            }

            @Override
            public void setAttribute(String name, Object value) {
                attributes.put(name, value);
            }

            @Override
            public Map getParameterMap() {
                return ImmutableMap.of();
            }

            @Override
            public String getRequestURI() {
                return "/";
            }

            @Override
            public String getContextPath() {
                return "";
            }

            @Override
            public String getHeader(String name) {
                return "";
            }

            @Override
            public String getParameter(String name) {
                return name;
            }

            @Override
            public String getServletPath() {
                return "/";
            }

            @Override
            public HttpSession getSession() {
                return session;
            }

            @Override
            public HttpSession getSession(boolean create) {
                return session;
            }
        };
    }

    /**
     * Returns a fake, serializable HttpSession which stores attributes in a HashMap.
     */
    public static HttpSession newFakeHttpSession() {
        return (HttpSession) Proxy.newProxyInstance(HttpSession.class.getClassLoader(),
                new Class[]{HttpSession.class}, new FakeHttpSessionHandler());
    }

    /**
     * Returns a fake, HttpServletResponse which throws an exception if any of its
     * methods are called.
     */
    public static HttpServletResponse newFakeHttpServletResponse() {
        return (HttpServletResponse) Proxy.newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class[]{HttpServletResponse.class}, new ThrowingInvocationHandler());
    }

    /*===========================================[ INNER CLASSES ]================*/

    private static class ThrowingInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            throw new UnsupportedOperationException("No methods are supported on this object");
        }
    }

    private static class FakeHttpSessionHandler implements InvocationHandler, Serializable {
        private static final long serialVersionUID = 5193608683408142848L;
        private final Map<String, Object> attributes = Maps.newHashMap();
        private String id;

        private FakeHttpSessionHandler() {
            id = UUID.randomUUID().toString();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if ("setAttribute".equals(name)) {
                attributes.put((String) args[0], args[1]);
                return null;
            } else if ("getAttribute".equals(name)) {
                return attributes.get(args[0]);
            } else if ("getId".equals(name)) {
                return id;
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }
}
