package baktulan.instagram.dto.userDTO;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaginationResponse {

  private   List<UserResponse> allUsers;

  private int currentPage;
  private int pageSize;

}
