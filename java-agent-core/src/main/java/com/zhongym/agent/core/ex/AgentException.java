package com.zhongym.agent.core.ex;

/**
 * @author zhongym
 */
public class AgentException extends RuntimeException {
    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
