/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.guice.event;

import com.google.code.vaadin.mvp.event.Observes;
import net.engio.mbassy.common.IPredicate;
import net.engio.mbassy.common.ReflectionUtils;
import net.engio.mbassy.listener.*;
import net.engio.mbassy.subscription.MessageEnvelope;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
* CompositeMetadataReader - TODO: description
*
* @author Alexey Krylov (AleX)
* @since 24.01.13
*/
class CompositeMetadataReader extends MetadataReader {
    private static final IPredicate<Method> AllMessageHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            return target.getAnnotation(Listener.class) != null || target.getAnnotation(Observes.class) != null;
        }
    };


    @Override
    public MessageHandlerMetadata getHandlerMetadata(Method messageHandler) {
        Listener listenerAnnotation = messageHandler.getAnnotation(Listener.class);
        if (listenerAnnotation == null) {
            Observes observesAnnotation = messageHandler.getAnnotation(Observes.class);
            if (observesAnnotation != null) {
                listenerAnnotation = new MappedListener();
            }
        }
        return new MessageHandlerMetadata(messageHandler, new IMessageFilter[0], listenerAnnotation);
    }

    @Override
    public List<MessageHandlerMetadata> getMessageHandlers(Class<?> target) {
        // get all handlers (this will include overridden handlers)
        List<Method> allMethods = ReflectionUtils.getMethods(AllMessageHandlers, target);
        List<MessageHandlerMetadata>  handlers = new LinkedList<MessageHandlerMetadata>();
        for(Method handler : allMethods){
            Method overriddenHandler = ReflectionUtils.getOverridingMethod(handler, target);
            if(overriddenHandler == null && isValidMessageHandler(handler)){
                // add the handler only if it has not been overridden because
                // either the override in the subclass deactivates the handler (by not specifying the @Listener)
                // or the handler defined in the subclass is part of the list and will be processed itself
                handlers.add(getHandlerMetadata(handler));
            }
        }
        return handlers;
    }

    private boolean isValidMessageHandler(Method handler) {
        if (handler.getParameterTypes().length != 1) {
            // a messageHandler only defines one parameter (the message)
            System.out.println("Found no or more than one parameter in messageHandler [" + handler.getName()
                    + "]. A messageHandler must define exactly one parameter");
            return false;
        }
        Enveloped envelope = handler.getAnnotation(Enveloped.class);
        if(envelope != null && !MessageEnvelope.class.isAssignableFrom(handler.getParameterTypes()[0])){
            System.out.println("Message envelope configured but no subclass of MessageEnvelope found as parameter");
            return false;
        }
        if(envelope != null && envelope.messages().length == 0){
            System.out.println("Message envelope configured but message types defined for handler");
            return false;
        }
        return true;
    }
}
