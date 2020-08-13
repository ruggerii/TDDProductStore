package br.com.productstore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public enum MessageEnum {
    NOT_FOUND("Not Found"),
    SERVICE_UNAVAILABLE("Service Unavailable"),
    PRODUCT_ALREADY_REGISTERED("Product already registered");

    @Getter
    private String msg;
}
