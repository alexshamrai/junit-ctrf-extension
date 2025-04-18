package io.github.alexshamrai.ctrf.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing the root structure of the Common Test Reporting Format (CTRF) JSON.
 *
 * <p>This class serves as the entry point for the CTRF JSON structure, containing test execution
 * results and related metadata. It's designed to be serialized to/deserialized from JSON using
 * Jackson.</p>
 *
 * <p>Should be aligned with official schema <a href="https://ctrf.io/docs/specification/overview">...</a></p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CtrfJson {
    /**
     * The format of the report.
     * This is a required field and must always be "CTRF".
     */
    @Builder.Default
    private String reportFormat = "CTRF";

    /**
     * The version of the specification.
     * This is a required field and must be a valid semver string.
     * Current version is "0.0.0".
     */
    @Builder.Default
    private String specVersion = "0.0.0";

    /**
     * Container for test execution results and related data.
     * This field holds all test execution information including test statuses, 
     * execution times, and any associated metadata defined in the CTRF specification.
     * When serialized to JSON, this becomes the "results" object in the CTRF structure.
     */
    private Results results;
}
