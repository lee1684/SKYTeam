package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.web.dto.MeetingListSearchDTO;
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
@Tag(name = "티켓")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final AwsS3Service awsS3Service;
    private final TicketService ticketService;

    @Operation(summary = "모임 증표 데이터 다운로드")
    @ApiResponse(responseCode = "200", description = "모임 증표 데이터 다운로드 성공", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @GetMapping("/{moimId}") // 모임 증표 데이터 다운로드
    public ResponseEntity<String> getFile(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(ticketService.loadTicket(moimId));
    }

    @Operation(summary = "모임 증표 편집에 의한 JSON 업로드")
    @ApiResponse(responseCode = "200", description = "모임 증표 편집에 의한 JSON 업로드 성공", content = {
            @Content(schema = @Schema(implementation = TicketEditResponseDTO.class))
    })
    @PutMapping("/{moimId}") // 모임 증표 편집에 의한 JSON 업로드
    public ResponseEntity<TicketEditResponseDTO> uploadJSON(@PathVariable("moimId") Long moimId, @RequestPart String json, @RequestPart List<MultipartFile> files) {

        TicketEditResponseDTO ticketEditResponseDTO = ticketService.editTicketJSON(moimId, json, files);
        if (ticketEditResponseDTO.getResultJson().equals("200 OK")) return ResponseEntity.ok(ticketEditResponseDTO);
        else return ResponseEntity.badRequest().body(ticketEditResponseDTO);
    }

    @Operation(summary = "모임 삭제에 의한 S3 등록 데이터 삭제")
    @ApiResponse(responseCode = "200", description = "모임 삭제에 의한 S3 등록 데이터 삭제 성공", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @DeleteMapping("/{moimId}") // 모임 삭제에 의한 S3 등록 데이터 삭제
    public ResponseEntity<String> deleteFiles(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(awsS3Service.deleteFiles(moimId));
    }

    @Operation(summary = "모임 생성 요청에 의한 템플릿 기반 신규 증표 생성")
    @ApiResponse(responseCode = "200", description = "모임 생성 요청에 의한 템플릿 기반 신규 증표 생성 성공", content = {
            @Content(schema = @Schema(implementation = TicketInitResponseDTO.class))
    })
    @PostMapping("/{moimId}") // 모임 생성 요청에 의한 템플릿 기반 신규 증표 생성
    public ResponseEntity<TicketInitResponseDTO> postFile(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(ticketService.initTicket(moimId));
    }

    @Operation(summary = "모임 증표 편집 시 이미지 업로드")
    @ApiResponse(responseCode = "200", description = "모임 증표 편집 시 이미지 업로드 성공", content = {
            @Content(schema = @Schema(implementation = TicketImageResponseDTO.class))
    })
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