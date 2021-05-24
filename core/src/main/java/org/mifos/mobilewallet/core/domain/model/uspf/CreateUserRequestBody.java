package org.mifos.mobilewallet.core.domain.model.uspf;

import java.util.ArrayList;
import java.util.List;

public class CreateUserRequestBody {

    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean sendPasswordToEmail = false;
    private int officeId = 1;
    private List<Integer> roles = new ArrayList<>();
    private String password;
    private String repeatPassword;
    private Boolean isSelfServiceUser;
    private List<Integer> clients = new ArrayList<>();

    public CreateUserRequestBody(String username, String firstname, String lastname, String email, Boolean sendPasswordToEmail, int officeId, List<Integer> roles, String password, String repeatPassword, Boolean isSelfServiceUser, List<Integer> clients) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.sendPasswordToEmail = sendPasswordToEmail;
        this.officeId = officeId;
        this.roles = roles;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.isSelfServiceUser = isSelfServiceUser;
        this.clients = clients;
    }
}
