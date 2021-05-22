package org.mifos.mobilewallet.core.domain.model;

public class LoginRequestBody {

    private String username;
    private String password;

    public LoginRequestBody(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
