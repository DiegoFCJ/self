package com.scriptagher.backend.service.remote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.scriptagher.shared.constants.APIS;
import com.scriptagher.shared.constants.LOGS;
import com.scriptagher.shared.logger.CustomLogger;

@Service
public class BotUploadService {

    public String uploadBot(MultipartFile file) throws IOException {
        File uploadFile = new File(APIS.BOT_DIR_UPLOAD + file.getOriginalFilename());

        try (OutputStream outputStream = new FileOutputStream(uploadFile)) {
            outputStream.write(file.getBytes());
        }

        String message = String.format(LOGS.BOT_UPLOADED, uploadFile.getAbsolutePath());
        CustomLogger.info(LOGS.BOT_CONTROLLER, message);
        return message;
    }
}
