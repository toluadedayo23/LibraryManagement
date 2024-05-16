package com.test.librarymanagement.logging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("librarymanagement.logging")
@Data
public class LoggingProperties {
    private boolean enabled;
    private List<String> excludedPaths;

}
