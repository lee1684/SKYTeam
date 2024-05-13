package kr.co.ssalon.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiaryFetchResponseDTO {

    private String resultCode;
    private String resultJSON;
}
