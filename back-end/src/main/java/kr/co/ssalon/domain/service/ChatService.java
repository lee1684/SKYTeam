package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.Message;
import kr.co.ssalon.domain.repository.MessageRepository;
import kr.co.ssalon.web.dto.MessageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final MemberMeetingService memberMeetingService;
    private final MessageRepository messageRepository;

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket-static}")
    private String bucketStaticName;

    @Transactional
    public MessageResponseDTO saveMessage(Member member, Meeting meeting, String message, String messageType, String imageUrl) throws BadRequestException {
        MemberMeeting memberMeeting = memberMeetingService.findByMemberAndMeeting(member, meeting);
        Message messageEntity = Message.createMessage(memberMeeting, message, messageType, imageUrl);
        messageEntity = messageRepository.save(messageEntity);

        return new MessageResponseDTO(messageEntity);
    }

    @Transactional
    public List<MessageResponseDTO> getMoimChatHistory(Long moimId) {
        return messageRepository.findAllByMeetingId(moimId).stream().filter(message -> "TALK".equals(message.getMessageType())).map(MessageResponseDTO::new).toList();
    }

    public List<MessageResponseDTO> getMyChatHistory(Long memberId) {
        return messageRepository.findAllByMemberId(memberId).stream().map(MessageResponseDTO::new).toList();
    }

    public String changeImageBytesToUrl(byte[] imageBytes, String fileName, Long roomId) {
        String imageUrl = "";

        String[] parts = fileName.split("\\.");
        String originalFileName = parts[0];
        String extension = parts[1];

        // Use builder to create PutObjectRequest
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketStaticName)
                .key("chat/" + roomId + "/" + originalFileName + "." + extension)
                .contentType("image/" + extension)
                .build();

        // Use RequestBody to create the request body from the byte array
        RequestBody requestBody = RequestBody.fromBytes(imageBytes);

        // Put the object in S3
        s3Client.putObject(putObjectRequest, requestBody);

        // Get the URL of the uploaded object
        imageUrl = s3Client.utilities().getUrl(builder -> builder.bucket(bucketStaticName).key("chat/" + originalFileName + "." + extension)).toString();

        return imageUrl;
    }
}
