package com.twentysixcore.chefapi.application.ports.outbound;

public interface DomainEventPublisher {
    void publish(Object event);
}
