package kr.co.igns.framework.utils.file;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class UploadParameters {
    private File file;
    private MultipartFile multipartFile;
    private String targetType;
    private String targetId;
    private String sort;
    private String desc;
    //
    private boolean deleteIfExist;
    private boolean thumbnail = true;
    private int thumbnailWidth = 640;
    private int thumbnailHeight = 640;
}
