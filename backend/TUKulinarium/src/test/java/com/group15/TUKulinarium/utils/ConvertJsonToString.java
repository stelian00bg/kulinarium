package com.group15.TUKulinarium.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertJsonToString {
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
