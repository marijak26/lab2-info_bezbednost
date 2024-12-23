package mk.finki.ukim.lab2.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessRequest {
    private String userId;
    private String resourceId;

}