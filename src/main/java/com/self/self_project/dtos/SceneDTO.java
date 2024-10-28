package com.self.self_project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SceneDTO {
    private boolean showBackButton;
    private boolean showHomeButton;
    private String menuType;
    private double width;
    private double height;
}
