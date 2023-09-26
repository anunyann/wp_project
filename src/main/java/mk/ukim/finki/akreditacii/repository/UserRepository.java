package mk.ukim.finki.akreditacii.repository;

import mk.ukim.finki.akreditacii.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaSpecificationRepository<User, String> {

}
