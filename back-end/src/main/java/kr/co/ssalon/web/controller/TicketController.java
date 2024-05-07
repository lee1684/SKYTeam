package kr.co.ssalon.web.controller;

import kr.co.ssalon.web.dto.TicketEditResponseDTO;
import kr.co.ssalon.domain.service.AwsS3Service;
import kr.co.ssalon.domain.service.TicketService;
import kr.co.ssalon.web.dto.TicketImageResponseDTO;
import kr.co.ssalon.web.dto.TicketInitResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// TicketController : 증표 관련 API
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final AwsS3Service awsS3Service;
    private final TicketService ticketService;

    @GetMapping("/{moimId}") // 모임 증표 데이터 다운로드
    public ResponseEntity<String> getFile(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(ticketService.loadTicket(moimId));
    }

    @PutMapping("/{moimId}") // 모임 증표 편집에 의한 신규 파일 업로드
    public ResponseEntity<TicketEditResponseDTO> uploadJSON(@PathVariable("moimId") Long moimId, @RequestPart String json) {
        return ResponseEntity.ok(ticketService.editTicketJSON(moimId, json));
    }

    @DeleteMapping("/{moimId}") // 모임 삭제에 의한 S3 등록 데이터 삭제
    public ResponseEntity<String> deleteFiles(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(awsS3Service.deleteFiles(moimId));
    }

    @PostMapping("/{moimId}") // 모임 생성 요청에 의한 템플릿 기반 신규 증표 생성
    public ResponseEntity<TicketInitResponseDTO> postFile(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(ticketService.initTicket(moimId));
    }

    @PostMapping(value = "/{moimId}/image") // 모임 증표 편집 시 이미지 업로드
    public ResponseEntity<TicketImageResponseDTO> uploadFiles(@PathVariable("moimId") Long moimId, @RequestPart List<MultipartFile> files) {

        TicketImageResponseDTO ticketImageResponseDTO = ticketService.uploadImages(moimId, files);

        return switch (ticketImageResponseDTO.getResultCode()) {
            case "201 Created" -> ResponseEntity.status(HttpStatus.CREATED).body(ticketImageResponseDTO);
            case "206 Partial Content" -> ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(ticketImageResponseDTO);
            case "502 Bad Gateway" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ticketImageResponseDTO);
            default -> ResponseEntity.badRequest().body(ticketImageResponseDTO); // 400 Bad Request
        };
    }
}