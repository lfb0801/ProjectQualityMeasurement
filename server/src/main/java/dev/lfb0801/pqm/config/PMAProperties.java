package dev.lfb0801.pqm.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pma.config")
public record PMAProperties(String path, List<String> blockedAuthors) {}
