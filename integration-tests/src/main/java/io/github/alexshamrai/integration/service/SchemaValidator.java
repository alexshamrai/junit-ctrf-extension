package io.github.alexshamrai.integration.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for JSON schema validation.
 */
public class SchemaValidator {
    private static final Logger log = LoggerFactory.getLogger(SchemaValidator.class);
    private final FileSteps fileSteps;
    private final ObjectMapper objectMapper;

    public SchemaValidator(FileSteps fileSteps) {
        this.fileSteps = fileSteps;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Validates a JSON object against a schema.
     *
     * @param jsonObject the JSON object to validate
     * @param schemaPath the path to the schema file
     * @return a set of validation messages, empty if validation is successful
     * @throws IOException if an I/O error occurs
     */
    public Set<ValidationMessage> validateAgainstSchema(JSONObject jsonObject, String schemaPath) throws IOException {
        // Load the JSON schema
        String schemaContent = fileSteps.readFileContent(schemaPath);
        
        // Create schema validator
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema schema = factory.getSchema(schemaContent);
        
        // Convert JSONObject to JsonNode
        JsonNode jsonNode = objectMapper.readTree(jsonObject.toString());
        
        // Validate the report against the schema
        Set<ValidationMessage> validationResult = schema.validate(jsonNode);
        
        if (!validationResult.isEmpty()) {
            String errors = validationResult.stream()
                    .map(ValidationMessage::getMessage)
                    .collect(Collectors.joining("\n"));
            log.info("Schema validation errors: {}", errors);
        }
        
        return validationResult;
    }
    
    /**
     * Checks if validation errors are related to missing new required fields.
     *
     * @param validationResult the set of validation messages
     * @param fieldNames the names of fields to check
     * @return true if errors are only related to the specified fields, false otherwise
     */
    public boolean isMissingRequiredFields(Set<ValidationMessage> validationResult, String... fieldNames) {
        if (validationResult.isEmpty()) {
            return false;
        }
        
        String errors = validationResult.stream()
                .map(ValidationMessage::getMessage)
                .collect(Collectors.joining());
        
        for (String fieldName : fieldNames) {
            if (errors.contains(fieldName)) {
                return true;
            }
        }
        
        return false;
    }
}