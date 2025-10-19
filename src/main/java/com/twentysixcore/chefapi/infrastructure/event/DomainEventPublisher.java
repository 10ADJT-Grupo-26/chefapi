package com.twentysixcore.chefapi.infrastructure.event;

public interface DomainEventPublisher {
    void publish(Object event);
}
