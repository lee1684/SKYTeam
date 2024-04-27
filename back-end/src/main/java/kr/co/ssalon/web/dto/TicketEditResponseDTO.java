package kr.co.ssalon.web.dto;

import java.util.List;

public record TicketEditResponseDTO(String resultJson, String jsonElement, List<String> resultSrc) {
}
