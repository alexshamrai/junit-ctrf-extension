package ua.shamrai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Results {
    private Tool tool;
    private Summary summary;
    private List<Test> tests;
    private Environment environment;
    private Extra extra;
}