package com.scriptagher.backend.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Automation {
    private String name;
    private String language;
    private String command;
    private String filePath;
    private Map<String, String> parameters;
}
