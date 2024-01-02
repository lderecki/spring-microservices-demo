package pl.lderecki.oauth2acs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lderecki.oauth2acs.entity.UserAuthority;

@Repository
public interface UserAuthorityRepo extends JpaRepository<UserAuthority, Long> {
}
