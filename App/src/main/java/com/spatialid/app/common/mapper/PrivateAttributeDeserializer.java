// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.mapper;

import java.io.IOException;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PrivateAttributeDeserializer extends JsonDeserializer<Map<String, String>> {
    
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    @Override
    public Map<String, String> deserialize(JsonParser jp,
            DeserializationContext context) throws IOException {
        
        final JsonNode jnode = jp.getCodec().readTree(jp);
        
        if (jnode.isTextual() && !(StringUtils.hasLength(jnode.asText()))) {
            
            return null;
            
        }
        
        final String facilityAttribute = jnode.toPrettyString();
        
        return objectMapper.readValue(facilityAttribute, new TypeReference<Map<String, String>>() {});
        
    }
    
}
