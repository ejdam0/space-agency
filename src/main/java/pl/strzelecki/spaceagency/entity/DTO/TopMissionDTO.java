package pl.strzelecki.spaceagency.entity.DTO;

import lombok.Data;

@Data
public class TopMissionDTO {
    private long missionId;
    private Long quantity;

    public TopMissionDTO(long missionId, Long quantity) {
        this.missionId = missionId;
        this.quantity = quantity;
    }
}
