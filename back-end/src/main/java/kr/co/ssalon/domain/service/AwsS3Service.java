package kr.co.ssalon.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket-static}")
    private String bucketStaticName;
    @Value("${spring.cloud.aws.s3.bucket-json}")
    private String bucketJsonName;

    public String getFileAsJsonString(String moimId) {
        // moimId를 바탕으로 S3에서 JSON 파일을 읽어와 String 형태로 반환합니다
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketJsonName)
                .key(moimId + "/" + moimId + ".json")
                .build();

        ResponseBytes<GetObjectResponse> s3Object = s3Client.getObjectAsBytes(getObjectRequest);
        String jsonStr = s3Object.asString(StandardCharsets.US_ASCII);

        return jsonStr;
    }

    public String uploadFileViaStream(Long moimId, String uploadFile) {
        try {
            // String -> Byte -> InputStream 변환
            InputStream inputStream = new ByteArrayInputStream(uploadFile.getBytes());

            // PutObjectRequest 설정
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketJsonName)
                    .contentType("application/json")
                    .contentLength((long) uploadFile.getBytes().length)
                    .key(moimId + "/" + moimId + ".json")
                    .build();

            // 업로드 실행
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, uploadFile.getBytes().length));
        } catch (Exception e) {
            // 임시 에러 로깅 처리
            e.printStackTrace();
            return "Failed to Upload JSON";
        }

        return "Success";
    }

    public List<String> uploadMultiFilesViaMultipart(List<MultipartFile> multipartFiles, Map<String, String> imageKeyMap) {
        List<String> fileUrlList = new ArrayList<>();

        multipartFiles.forEach(multipartFile -> {
            if (multipartFile.isEmpty()) {
                // log.info("image is null");
                fileUrlList.add("Failed to upload - null image: " + getFileName(multipartFile));
            }

            String oldfileName = getFileName(multipartFile);
            String newFileURI = "";
            for (String fileURI : imageKeyMap.keySet()) {
                String regexName = "\\S*" + oldfileName;

                if (fileURI.matches(regexName)) {
                    newFileURI = imageKeyMap.get(fileURI);
                    break;
                }
            }

            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketStaticName)
                        .contentType(multipartFile.getContentType())
                        .contentLength(multipartFile.getSize())
                        .key(newFileURI)
                        .build();
                RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
                s3Client.putObject(putObjectRequest, requestBody);
            } catch (IOException e) {
                // log.error("cannot upload image",e);
                throw new RuntimeException(e);
            }
            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                    .bucket(bucketStaticName)
                    .key(newFileURI)
                    .build();

            fileUrlList.add(s3Client.utilities().getUrl(getUrlRequest).toString());
        });

        return fileUrlList;
    }

    public String uploadSingleFileViaMultipart(Long moimId, MultipartFile multipartFile, Map<String, String> imageSrcMap) {
        String fileUrl = "";

        if (multipartFile.isEmpty()) {
            // log.info("image is null");
            return "Failed to upload - null image: " + getFileName(multipartFile);
        }

        String fileName = getFileName(multipartFile);
        String newFileName = imageSrcMap.get(fileName) + "." + fileName.substring(fileName.lastIndexOf(".") + 1);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketStaticName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .key("Thumbnails/" + moimId + "/" + newFileName)
                    .build();
            RequestBody requestBody = RequestBody.fromBytes(multipartFile.getBytes());
            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            // log.error("cannot upload image",e);
            throw new RuntimeException(e);
        }

        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketStaticName)
                .key("Thumbnails/" + moimId + "/" + newFileName)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    public List<String> copyFilesFromTemplate(Map<String, String> fileKeyMap) {
        List<String> resultCopy = new ArrayList<>();

        for (String key : fileKeyMap.keySet()) {
            CopyObjectRequest copyReq = CopyObjectRequest.builder()
                    .sourceBucket(bucketStaticName)
                    .sourceKey(key)
                    .destinationBucket(bucketStaticName)
                    .destinationKey(fileKeyMap.get(key))
                    .build();

            try {
                CopyObjectResponse copyRes = s3Client.copyObject(copyReq);
                resultCopy.add(copyRes.copyObjectResult().toString());

            } catch (S3Exception e) {
                System.err.println("Error on AWS S3 Service: " + e.awsErrorDetails().errorMessage());
            }
        }

        return resultCopy;
    }

    public String deleteFiles(Long moimId) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketStaticName)
                    .key(moimId + "")
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            System.err.println("Error on AWS S3 Service: " + e.awsErrorDetails().errorMessage());
        }

        return "OK";
    }

    private String getFileName(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) return "";
        return multipartFile.getOriginalFilename();
    }
}
