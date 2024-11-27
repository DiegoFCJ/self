package com.scriptagher.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an automation bot with its name and file path.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Automation {
    private String name;
    private String filePath;
}