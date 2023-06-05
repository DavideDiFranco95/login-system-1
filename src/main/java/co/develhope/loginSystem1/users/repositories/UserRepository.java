package co.develhope.loginSystem1.users.repositories;

import co.develhope.loginSystem1.auth.entities.SignupActivationDTO;
import co.develhope.loginSystem1.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    User findByPasswordResetCode(String resetPasswordCode);

    User findByActivationCode(SignupActivationDTO signupActivationDTO);
}
