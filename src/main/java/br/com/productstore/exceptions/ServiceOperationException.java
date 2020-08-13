package br.com.productstore.exceptions;

import br.com.productstore.enums.MessageEnum;

public class ServiceOperationException extends RuntimeException{

    public ServiceOperationException(MessageEnum msg) {
        super(msg.getMsg());
    }

}
