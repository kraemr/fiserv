package com.example.server.files;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.server.GenericSuccessResponse;

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GenericSuccessResponse> deleteFile(@PathVariable Long id) {
        try {
            Optional<FileMetaData> optionalMeta = fileMetaDataRepository.findById(id);
            if (optionalMeta.isEmpty()) {
                return ResponseEntity.notFound().build();
            }        
            FileMetaData meta = optionalMeta.get(); 

            // Assuming files are stored under the first directory
            String dir = fileProperties.getLocations().get(0);
            File file = new File(dir, meta.getFilename());
            
            // file doesnt exist
            if (!file.exists()) {
                return GenericSuccessResponse.buildResponseEntity(404, false, "File doesnt exist");
            }        
            if(file.delete()) {
                fileMetaDataRepository.delete(meta);
                return GenericSuccessResponse.buildResponseEntity(200, true, "File successfully deleted");
            }
            // file could already be deleted but still be in the db or user running doesnt have permissions
            else
            {   
                return GenericSuccessResponse.buildResponseEntity(500, false, "File couldnt be deleted");
            }
        }
        catch (Exception e) {
            try {
                String err = "Deletion failed: " + e.getMessage();
                return GenericSuccessResponse.buildResponseEntity(500, false, err);
            }
            catch(Exception jsonException) {
                return ResponseEntity.internalServerError().build();
            }         
        }
    }

    @PostMapping("/upload/{folderId}")
    public ResponseEntity<GenericSuccessResponse> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Long folderId) {
        try {
            String uploadDir = fileProperties.getLocations().get(0); // Use first directory
            File targetFile = new File(uploadDir, file.getOriginalFilename());

            if(targetFile.exists()){
                return GenericSuccessResponse.buildResponseEntity(200, false, "already uploaded");
            }
            
            file.transferTo(targetFile);

            FileMetaData meta = new FileMetaData(file.getOriginalFilename(),file.getSize(),folderId);
            
            meta.setFilename(file.getOriginalFilename());
            meta.setSize(file.getSize());
            

            fileMetaDataRepository.save(meta);

            return GenericSuccessResponse.buildResponseEntity(200,true,"uploaded: " + targetFile.getAbsolutePath());
        } catch (Exception e) {
            try {
                String err = "Upload failed: " + e.getMessage();
                return GenericSuccessResponse.buildResponseEntity(500, false, err);
            }
            catch(Exception jsonException) {
                return ResponseEntity.internalServerError().build();
            }         
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
