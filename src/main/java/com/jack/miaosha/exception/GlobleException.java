package com.jack.miaosha.exception;

import com.jack.miaosha.result.CodeMsg;
import lombok.Data;

@Data
public class GlobleException extends RuntimeException {
    private static final long serialVsersionUID = 1L;
    private CodeMsg cm;
    public GlobleException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

}
