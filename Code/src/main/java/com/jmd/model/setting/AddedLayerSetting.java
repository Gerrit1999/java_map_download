package com.jmd.model.setting;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AddedLayerSetting implements Serializable {

    @Serial
    private static final long serialVersionUID = -2871907579117630271L;

    private String name;
    private String url;
    private String type;
    private boolean proxy;
}
