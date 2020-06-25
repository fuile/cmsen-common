/**
 * +---------------------------------------------------------
 * | Author Jared.Yan<yanhuaiwen@163.com>
 * +---------------------------------------------------------
 * | Copyright (c) http://cmsen.com All rights reserved.
 * +---------------------------------------------------------
 */
package com.cmsen.common.event;

import java.util.HashMap;
import java.util.Map;

public class EventManager {
    private Map<String, EventListener> eventObject;

    public void addEventListener(String eventName, String listener) {
        try {
            addEventListener(eventName, (EventListener) Class.forName(listener).newInstance());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void addEventListener(String eventName, Class<? extends EventListener> listener) {
        try {
            addEventListener(eventName, listener.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void addEventListener(String eventName, EventListener listener) {
        if (null == this.eventObject) {
            this.eventObject = new HashMap<>();
        }
        this.eventObject.put(eventName, listener);
    }

    public void removeEventListener(String eventName) {
        if (null != this.eventObject) {
            this.eventObject.remove(eventName);
        }
    }

    public void clearEventListener() {
        if (null != this.eventObject) {
            this.eventObject.clear();
        }
    }

    public void notifyEvent(String eventName) {
        notifyEvent(eventName, null);
    }

    public void notifyEvent(String eventName, Object o) {
        if (null != this.eventObject) {
            this.eventObject.get(eventName).event(o, eventName);
        }
    }

    public void notifyEventAll() {
        notifyEventAll(null);
    }

    public void notifyEventAll(Object o) {
        for (Map.Entry<String, EventListener> eventObject : this.eventObject.entrySet()) {
            if (null != eventObject.getValue()) {
                eventObject.getValue().event(o, eventObject.getKey());
            }
        }
    }
}
