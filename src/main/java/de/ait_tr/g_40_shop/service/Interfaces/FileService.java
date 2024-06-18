package de.ait_tr.g_40_shop.service.Interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String upload(MultipartFile file, String productTitle);
}
