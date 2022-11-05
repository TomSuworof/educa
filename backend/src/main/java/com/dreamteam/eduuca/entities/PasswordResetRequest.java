package com.dreamteam.eduuca.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Entity
@Getter
@Table(name = "t_password_reset_request")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @Id
    @NotNull
    @GeneratedValue
    private UUID id;

    @NotEmpty
    private String username;

    @PastOrPresent
    private Date created;

    public PasswordResetRequest(String username) {
        this.username = username;
        this.created = new Date();
    }

    public Boolean isValid() {
        Calendar now = new GregorianCalendar();
        Date dateCreated = this.created;
        Calendar dateExpired = new GregorianCalendar();
        dateExpired.setTime(dateCreated);
        dateExpired.add(Calendar.HOUR, 24);
        return now.before(dateExpired);
    }
}
