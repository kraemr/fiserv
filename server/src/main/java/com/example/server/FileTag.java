package com.example.server;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "file_tag")
public class FileTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String byUser;

    // Constructors
    public FileTag() {}

    public FileTag(String name, Date created, String byUser) {
        this.name = name;
        this.created = created;
        this.byUser = byUser;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }
}
