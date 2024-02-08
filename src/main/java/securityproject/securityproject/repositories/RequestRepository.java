package securityproject.securityproject.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import securityproject.securityproject.models.Request;
import securityproject.securityproject.models.User;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findBySenderAndAcceptedFalse(User sender);
    List<Request> findAllBySender(User sender);
    Request findByAcceptedFalse();
    
}
