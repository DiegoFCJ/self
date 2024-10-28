package com.self.self_project.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubmenuDTO {
    private String titleString;
    private List<MenuDTO> menuActions;
}