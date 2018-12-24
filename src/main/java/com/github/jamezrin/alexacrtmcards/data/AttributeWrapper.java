package com.github.jamezrin.alexacrtmcards.data;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;

public class AttributeWrapper {
    private final ObjectMapper objectMapper;
    private final String wrapperRootKey;

    public AttributeWrapper(String wrapperRootKey, ObjectMapper objectMapper) {
        this.wrapperRootKey = wrapperRootKey;
        this.objectMapper = objectMapper;
    }

    public AttributeWrapper(String wrapperRootKey) {
        this(wrapperRootKey, new ObjectMapper()
                .registerModule(new JavaTimeModule()));
    }

    /**
     * Loads the persistent user attributes date tree
     * returning an {@link UserAttributes} instance
     *
     * @param handlerInput the request handler input
     * @return the attributes instance
     */
    public UserAttributes load(HandlerInput handlerInput) {
        AttributesManager attributesManager = handlerInput.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getPersistentAttributes();
        Map<?, ?> wrappedMap = (Map) attributes.get(wrapperRootKey);
        if (wrappedMap == null) return new UserAttributes();
        return objectMapper.convertValue(wrappedMap, UserAttributes.class);
    }

    /**
     * Saves an {@link UserAttributes} instance to
     * the users persistent attributes data tree
     *
     * @param handlerInput the request handler input
     * @param userAttributes the user attributes to save
     * @return whether the user had attributes previously
     */
    public boolean save(HandlerInput handlerInput, UserAttributes userAttributes) {
        AttributesManager attributesManager = handlerInput.getAttributesManager();
        Map<String, Object> attributes = attributesManager.getPersistentAttributes();
        Object previous = attributes.put(wrapperRootKey,
                objectMapper.convertValue(userAttributes, Map.class));
        attributesManager.setPersistentAttributes(attributes);
        attributesManager.savePersistentAttributes();
        return previous != null;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String getWrapperRootKey() {
        return wrapperRootKey;
    }
}
