package com.group15.TUKulinarium.payload.request;

import javax.validation.constraints.NotNull;

public class LogOutRequest {
    @NotNull
    private Long userId;

    public Long getUserId() {
        return this.userId;
    }
}