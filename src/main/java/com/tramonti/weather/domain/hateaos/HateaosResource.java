package com.tramonti.weather.domain.hateaos;

import org.springframework.hateoas.ResourceSupport;

public class HateaosResource<T> extends ResourceSupport {
    private T resource;

    public HateaosResource() {
    }

    public HateaosResource(T resource) {
        this.resource = resource;
    }

    public T getResource() {
        return resource;
    }

    public HateaosResource<T> setResource(T resource) {
        this.resource = resource;
        return this;
    }
}
