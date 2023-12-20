package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString(){
        return value;
    }
}
