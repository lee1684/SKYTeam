package kr.co.ssalon.domain.service;

import kr.co.ssalon.web.dto.ImageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageUploadService {

    @Autowired
    private AwsS3Service awsS3Service;

    public ImageResponseDTO uploadImage(String username, List<MultipartFile> multipartFiles) {

        Map<String, String> imageSrcMap = new HashMap<>();

        int requestSize = multipartFiles.size();

        if (requestSize == 0) return ImageResponseDTO.builder()
                .resultCode("400 Bad Request")
                .numRequest(requestSize)
                .numResult(0)
                .mapURI(null)
                .build();

        for (MultipartFile multipartFile : multipartFiles) {
            String oldFileURI = multipartFile.getOriginalFilename();
            String extOldFile = oldFileURI.substring(oldFileURI.lastIndexOf('.') + 1);
            String newFileURI = "Images/" + usernameConverter(username) + "/" + generateRandomUUID() + "." + extOldFile;

            imageSrcMap.put(oldFileURI, newFileURI);
        }

        int resultSize = awsS3Service.uploadMultiFilesViaMultipart(multipartFiles, imageSrcMap);

        if (requestSize == resultSize) return ImageResponseDTO.builder()
                .resultCode("201 Created")
                .numRequest(requestSize)
                .numResult(resultSize)
                .mapURI(imageSrcMap)
                .build();
        else if (resultSize > 0) return ImageResponseDTO.builder()
                .resultCode("206 Partial Content")
                .numRequest(requestSize)
                .numResult(resultSize)
                .mapURI(imageSrcMap)
                .build();
        else return ImageResponseDTO.builder()
                    .resultCode("502 Bad Gateway")
                    .numRequest(requestSize)
                    .numResult(resultSize)
                    .mapURI(imageSrcMap)
                    .build();
    }

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private String usernameConverter(String originString) {
        return originString.replace(" ", "-");
    }
}
