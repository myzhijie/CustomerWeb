package com.jingsky.customer.util.util;

import java.io.Serializable;
import java.util.HashMap;

public class ResultMessage extends HashMap<Object, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ResultMessage() {
        this.put("status","success");
    }

}

