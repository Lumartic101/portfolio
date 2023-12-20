package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    OPEN("OPEN"),
    CLOSED("CLOSED"),
    EXPIRED("EXPIRED"),
    ARCHIVED("ARCHIVED"),
    CONCEPT("CONCEPT");

    private final String value;

    Status(String value){
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString(){
        return value;
    }
}
