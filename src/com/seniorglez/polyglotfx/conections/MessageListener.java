package com.seniorglez.polyglotfx.conections;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class MessageListener extends ScheduledService<String> {

    @Override
    protected Task<String> createTask() {

        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                /*
                 * You can make any type of connection in this method without having to handle
                 * any type of exception, if this method throws an exception the service will
                 * call it again. I'm just going to pretend that messages are arriving.
                 */
                Thread.sleep(10000);
                return "{\"author\": \"Diego\",\"body\": \"Hi m8s\"}";
            }
        };
    }
}