package com.example.Project.Management.bayport.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public static String removeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", "");
    }
}

