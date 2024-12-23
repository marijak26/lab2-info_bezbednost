package mk.finki.ukim.lab2.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AccessRecord {
    private String userId;
    private String resourceId;
    private LocalDateTime expiryTime;

}
