package nl.hva.stb5.backend.rest.security;

import java.util.Date;

public class JWTokenInfo {

    public static final String KEY = "tokenInfo";

    private int id;
    private boolean admin;
    private Date issuedAt;
    private Date expiration;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "JWTokenInfo{" +
                "id='" + id + '\'' +
                ", admin=" + admin +
                ", issuedAt=" + issuedAt +
                ", expiration=" + expiration +
                '}';
    }
}
