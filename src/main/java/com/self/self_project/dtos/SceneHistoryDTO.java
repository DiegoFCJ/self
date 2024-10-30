package com.self.self_project.dtos;

import java.util.Stack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SceneHistoryDTO {
    private Stack<SceneDTO> sceneHistory;
}
