package pl.strzelecki.spaceagency.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopMissionDTO {
    private long missionId;
    private Long quantity;

    public TopMissionDTO(long missionId, Long quantity) {
        this.missionId = missionId;
        this.quantity = quantity;
    }
}
