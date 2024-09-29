package baktulan.instagram.dto.followerDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowerRequest {
    private List<Long>subscribers;
    private List<Long >subscriptions;

}
