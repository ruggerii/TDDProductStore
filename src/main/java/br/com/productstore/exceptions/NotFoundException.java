package br.com.productstore.exceptions;

import br.com.productstore.enums.MessageEnum;

public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super(MessageEnum.NOT_FOUND.getMsg());
    }

}
