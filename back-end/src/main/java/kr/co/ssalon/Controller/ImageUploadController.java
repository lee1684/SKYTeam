package kr.co.ssalon.Controller;

import kr.co.ssalon.Service.AwsS3Service;
import kr.co.ssalon.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// ImageUploadController : 현재는 ImageUpload만 담당하지만, 추후 DB 구축 시 AssetController로 수정 필요
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class ImageUploadController {

    private final AwsS3Service awsS3Service;
    private final TicketService ticketService;

    @PutMapping("/{moimId}") // 모임 증표 편집에 의한 신규 파일 업로드
    public ResponseEntity<List<String>> uploadFiles(@PathVariable("moimId") Long moimId, List<MultipartFile> files) {
        return ResponseEntity.ok(awsS3Service.uploadFilesViaMultipart(moimId, files));
    }

    @DeleteMapping("/{moimId}") // 모임 삭제에 의한 S3 등록 데이터 삭제
    public ResponseEntity<String> deleteFiles(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(awsS3Service.deleteFiles(moimId));
    }

    @PostMapping("/{moimId}") // 모임 생성 요청에 의한 템플릿 기반 신규 증표 생성
    public ResponseEntity<String> getFile(@PathVariable("moimId") Long moimId) {
        return ResponseEntity.ok(ticketService.initTicket(moimId));
    }
}