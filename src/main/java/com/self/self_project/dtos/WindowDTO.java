package com.self.self_project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WindowDTO {
    
    private double width;
    private double height;
    
    // Variabili per lo spostamento della finestra
    private double initialX;
    private double initialY;
    private double dragOffsetX;
    private double dragOffsetY;

    public WindowDTO(double width, double height) {
        this.width = width;
        this.height = height;
    }
}
