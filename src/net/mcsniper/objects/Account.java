package net.mcsniper.objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private String email, password;
    private boolean sniped, reserved;

    public Account(String email, String password, Boolean sniped, Boolean reserved){
        this.email = email;
        this.password = password;
        this.sniped = sniped;
        this.reserved = reserved;
    }

    public String getCombo(){
        return email + ":" + password;
    }

}
