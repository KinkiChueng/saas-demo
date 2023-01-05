package com.example.saasdemo.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

public class DatabaseChangeEvent extends ApplicationContextEvent {
    public DatabaseChangeEvent(ApplicationContext source) {
        super(source);
    }
}
