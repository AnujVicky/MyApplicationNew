package com.example.myapplicationnew;
import android.widget.EditText;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Users {
    private String name;
    private String phone_number;
    private @ServerTimestamp Date timestamp;
    private String alternate_phone_number;
    private String user_id;

    public Users(String name, String phone_number,Date timestamp,String alternate_phone_number, String user_id) {
        this.name = name;
        this.phone_number = phone_number;
        this.timestamp = timestamp;
        this.alternate_phone_number = alternate_phone_number;
        this.user_id = user_id;
    }

    public Users(String name, String alternate_number) {
        this.name = name;
        this.alternate_phone_number =alternate_number;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("alternate_phone_number", alternate_phone_number);
        return result;
    }


    public Users() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAlternate_phone_number() {
        return alternate_phone_number;
    }

    public void setAlternate_phone_number(String alternate_phone_number) {
        this.alternate_phone_number = alternate_phone_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
