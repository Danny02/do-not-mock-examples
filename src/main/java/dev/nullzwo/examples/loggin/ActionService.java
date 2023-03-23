package dev.nullzwo.examples.loggin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionService {
    private static final Logger log = LoggerFactory.getLogger(ActionService.class);

    public void invokeAction() {
        log.info("action was invoked for {}");
    }

    public void invokeAction(String param) {
        log.info("action was invoked for {}", param);
    }
}
