package kr.co.ssalon.Controller;

import jakarta.validation.Valid;
import kr.co.ssalon.domain.entity.Diary;
import kr.co.ssalon.domain.entity.Ticket;
import kr.co.ssalon.dto.TicketDTO;
import kr.co.ssalon.dto.DiaryDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketConroller {

    @GetMapping("/tickets/{moimId}")
    public ResponseEntity<String> getTicketFrontById (@PathVariable("moimId") Long moimId) {
        return null;
    }

    @PostMapping("/tickets/{moimId}")
    public ResponseEntity<String> addTicketFrontById (@Valid @RequestBody TicketDTO ticketDTO)
    {
        return new ResponseEntity<>((MultiValueMap<String, String>) ticketDTO, HttpStatus.CREATED);
    }

    @PutMapping("/tickets/{moimId}")
    public ResponseEntity<String> updateTicketFrontById (@PathVariable("moimId") Long moimId, @Valid @RequestBody TicketDTO ticketDTO)
    {
        return new ResponseEntity<>((MultiValueMap<String, String>) ticketDTO, HttpStatus.OK);
    }

    @GetMapping("/tickets/{moimId}/{userId}")
    public ResponseEntity<String> getTicketBackById (@PathVariable("moimid") Long moimId, @PathVariable("userId") Long userId) {
        return null;
    }

    @PostMapping("/tickets/{moimId}/{userId}")
    public ResponseEntity<String> addTicketBackById (@Valid @RequestBody DiaryDTO diaryDTO)
    {
        return new ResponseEntity<>((MultiValueMap<String, String>) diaryDTO, HttpStatus.CREATED);
    }

    @PutMapping("/tickets/{moimId}/{userId}")
    public ResponseEntity<Ticket> updateTicketBackById (@PathVariable("moimId") Long moimId, @PathVariable("userId") Long userId, @Valid @RequestBody DiaryDTO diaryDTO)
    {
        return new ResponseEntity<>((MultiValueMap<String, String>) diaryDTO, HttpStatus.OK);
    }

//    @GetMapping("/users/{userId}/tickets")
//    public ResponseEntity<String> getTicketsById (@PathVariable("userId") Long userId) {
//        return null;
//    }
//
//    @GetMapping("/tickets/{moimId}/link")
//    public ResponseEntity<String> getLinkById (@PathVariable("moimId") Long moimId) {
//        return null;
//    }
//
//    @DeleteMapping("/tickets/{moimId}/link")
//    public ResponseEntity<HttpStatus> removeLinkById (@PathVariable("moimId") Long moimId) {
//        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping("/tickets/{moimId}/{userId}/link")
//    public  ResponseEntity<String> getQRLinkById (@PathVariable("moimid") Long moimId, @PathVariable("userId") Long userId) {
//        return null;
//    }
//
//    @PostMapping("/tickets/{moimId}/{userId}/link")
//    public  ResponseEntity<String> checkQRLinkById (@PathVariable("moimid") Long moimId, @PathVariable("userId") Long userId) {
//        return null;
//    }
}
