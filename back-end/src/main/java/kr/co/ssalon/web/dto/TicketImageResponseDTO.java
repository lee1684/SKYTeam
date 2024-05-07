package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketImageResponseDTO {

    private String resultCode;
    private int numRequest;
    private int numResult;
    private Map<String, String> mapURI;
}
