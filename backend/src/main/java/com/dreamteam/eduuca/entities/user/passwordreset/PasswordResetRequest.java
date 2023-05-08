package com.dreamteam.eduuca.entities.user.passwordreset;

import com.dreamteam.eduuca.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Log4j2
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

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @PastOrPresent
    private Date created;

    public PasswordResetRequest(User user) {
        this.user = user;
        this.created = new Date();
    }

    public Boolean isValid() {
        log.debug("isValid() called.");
        Calendar now = new GregorianCalendar();
        Date dateCreated = this.created;
        Calendar dateExpired = new GregorianCalendar();
        dateExpired.setTime(dateCreated);
        dateExpired.add(Calendar.HOUR, 24);
        log.trace("isValid(). Now: {}, date expired: {}", now, dateExpired);
        boolean result = now.before(dateExpired);
        log.trace("isValid(). Request is valid: {}", result);
        return result;
    }
}
