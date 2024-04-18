package kr.co.ssalon.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingController {

    @GetMapping("/moims")
    public ResponseEntity<String> getMoims() {
        return null;
    }

    @GetMapping("/moims/{moimId}")
    public ResponseEntity<String> getMoimById(@PathVariable("moimId") Long moimId) {
        return null;
    }
}
