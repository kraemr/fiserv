package com.example.server;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;


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

}
