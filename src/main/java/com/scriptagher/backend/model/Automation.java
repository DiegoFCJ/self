package com.scriptagher.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Automation {
    private String botName;
    private String startCommand;
    private String description;
    private String sourcePath;
    private String language;
}