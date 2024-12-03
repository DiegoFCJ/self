package com.scriptagher.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for representing a local bot.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalBot {
    private String botName;
    private String path;
}