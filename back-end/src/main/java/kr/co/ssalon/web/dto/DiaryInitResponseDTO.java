package kr.co.ssalon.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DiaryInitResponseDTO {

    private String resultCode;
    private String resultJsonUpload;
    private List<String> resultCopyFiles;
}
