package io.github.alexshamrai.integration.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonSchemaValidator {
    private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidator.class);
    private final ObjectMapper objectMapper;
    private final JsonSchemaFactory schemaFactory;

    public JsonSchemaValidator() {
        this.objectMapper = new ObjectMapper();
        this.schemaFactory = JsonSchemaFactory.byDefault();
    }

    public ValidationResult validateAgainstSchema(File jsonFile, String schemaPath) throws IOException {
        try {
            String schemaContent = FileUtility.readFileContent(schemaPath);
            JsonNode schemaNode = objectMapper.readTree(schemaContent);
            JsonSchema schema = schemaFactory.getJsonSchema(schemaNode);

            JsonNode jsonNode = objectMapper.readTree(jsonFile);

            ProcessingReport report = schema.validate(jsonNode);
            
            ValidationResult result = new ValidationResult(report.isSuccess());
            
            if (!report.isSuccess()) {
                report.forEach(processingMessage -> 
                    result.addError(processingMessage.getMessage())
                );
            }
            
            return result;
        } catch (ProcessingException e) {
            log.error("Error validating JSON against schema", e);
            throw new IOException("Error validating JSON against schema", e);
        }
    }

    public class ValidationResult {
        private final boolean success;
        private final List<String> errors = new ArrayList<>();
        
        public ValidationResult(boolean success) {
            this.success = success;
        }
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public List<String> getErrors() {
            return errors;
        }
    }
}