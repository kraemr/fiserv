package com.example.server.files;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.List;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class FileMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private Long size;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private List<FileTag> tags;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "folder_id")
    private Long folderId;

    FileMetaData(String filename, long size, List<FileTag> tags, long folderId) {
        this.filename = filename;
        this.size = size;
        this.tags = tags;
        this.folderId = folderId;
    }

    FileMetaData(String filename, long size, long folderId) {
        this.filename = filename;
        this.size = size;
        this.tags = null;
        this.folderId = folderId;
    }

    public void setFolderId(Long folderId){
        this.folderId = folderId;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setSize(Long size) {
        this.size = size;        
    }

    public Long getSize(){
        return this.size;
    }

    public String getFilename() {
        return this.filename;
    }

    public Long getId(){
        return this.id;
    }

    public Long getFolderId() {
        return this.folderId;
    }

}
