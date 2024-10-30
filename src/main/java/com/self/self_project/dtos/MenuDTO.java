package com.self.self_project.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuDTO {
    private String title;
    private List<SubmenuDTO> submenus;
}
