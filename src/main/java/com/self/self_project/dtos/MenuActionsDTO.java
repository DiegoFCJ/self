package com.self.self_project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuActionsDTO {
    private String title;
    private String Choice;
    private String action;
}