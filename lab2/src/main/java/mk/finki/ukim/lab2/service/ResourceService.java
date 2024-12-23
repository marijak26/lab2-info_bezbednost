package mk.finki.ukim.lab2.service;

import lombok.Getter;
import mk.finki.ukim.lab2.model.AccessRecord;
import mk.finki.ukim.lab2.model.AccessRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Service
public class ResourceService {
    private final List<AccessRecord> activeAccess = new ArrayList<>();

    public void requestAccess(AccessRequest request) {
        AccessRecord record = new AccessRecord();
        record.setUserId(request.getUserId());
        record.setResourceId(request.getResourceId());
        record.setExpiryTime(LocalDateTime.now().plusMinutes(30));
        activeAccess.add(record);
    }

    public void revokeAccess(String userId, String resourceId) {
        Iterator<AccessRecord> iterator = activeAccess.iterator();
        while (iterator.hasNext()) {
            AccessRecord record = iterator.next();
            if (record.getUserId().equals(userId) && record.getResourceId().equals(resourceId)) {
                iterator.remove();
                break;
            }
        }
    }

}
