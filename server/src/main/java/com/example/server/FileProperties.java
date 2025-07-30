package com.example.server;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="file")
public class FileProperties {
    private List<String> locations;

    public List<String> getLocations() { return locations; }
    public void setLocations(List<String> locations) { this.locations = locations; }
}
