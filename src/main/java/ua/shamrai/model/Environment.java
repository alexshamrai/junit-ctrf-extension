package ua.shamrai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Environment {
    private String reportName;
    private String appName;
    private String appVersion;
    private String buildName;
    private String buildNumber;
    private String buildUrl;
    private String repositoryName;
    private String repositoryUrl;
    private String commit;
    private String branchName;
    private String osPlatform;
    private String osRelease;
    private String osVersion;
    private String testEnvironment;
    private Extra extra;
}
