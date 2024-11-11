package com.rafo.apps.task_retrofit.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGeneric {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    private Object data;

}
