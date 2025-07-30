package com.example.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileProperties fileProperties;
    private final FileMetaDataRepository fileMetaDataRepository;

    public FileController(FileProperties fileProperties, FileMetaDataRepository repo) {
        this.fileProperties = fileProperties;
        this.fileMetaDataRepository = repo;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String uploadDir = fileProperties.getLocations().get(0); // Use first directory
            File targetFile = new File(uploadDir, file.getOriginalFilename());
            file.transferTo(targetFile);

            FileMetaData meta = new FileMetaData();
            
            meta.setFilename(file.getOriginalFilename());
            meta.setSize(file.getSize());

            fileMetaDataRepository.save(meta);

            return ResponseEntity.ok("File uploaded: " + targetFile.getAbsolutePath());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }


   @GetMapping("/download/{id}")
public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
    Optional<FileMetaData> optionalMeta = fileMetaDataRepository.findById(id);

    if (optionalMeta.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    FileMetaData meta = optionalMeta.get();

    // Assuming files are stored under the first directory
    String dir = fileProperties.getLocations().get(0);
    File file = new File(dir, meta.getFilename());

    if (!file.exists()) {
        return ResponseEntity.notFound().build();
    }

    try {
        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data);
    } catch (IOException e) {
        return ResponseEntity.internalServerError().build();
    }

}

@GetMapping("/meta")
public List<FileMetaData> listMetadata() {
    return fileMetaDataRepository.findAll();
}

}
