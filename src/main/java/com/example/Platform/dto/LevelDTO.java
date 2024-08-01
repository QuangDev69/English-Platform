package com.example.Platform.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LevelDTO {
    @NotEmpty(message = "Level name Can not be empty")
    private String levelName;
}
