package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.ConfirmationCode;
import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.repository.ConfirmationCodeRepository;
import de.ait_tr.g_40_shop.repository.UserRepository;
import de.ait_tr.g_40_shop.service.Interfaces.EmailService;
import de.ait_tr.g_40_shop.service.Interfaces.RoleService;
import de.ait_tr.g_40_shop.service.Interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final ConfirmationCodeRepository confirmationCodeRepository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository repository, ConfirmationCodeRepository confirmationCodeRepository, BCryptPasswordEncoder encoder, RoleService roleService, EmailService emailService) {
        this.repository = repository;
        this.confirmationCodeRepository = confirmationCodeRepository;
        this.encoder = encoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        return repository.findByUsername(username).orElseThrow(
//                () -> new UsernameNotFoundException(
//                        String.format("User %s not found", username)));

        // Альтернативный вариант
        User user = repository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("User %s not found", username));
        }

        return user;
    }

    @Override
    public void register(User user) {

            user.setId(null);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setActive(false);
            user.setRoles(Set.of(roleService.getRoleUser()));

            repository.save(user);
            emailService.sendConfirmationEmail(user);

    }

    @Override
    @Transactional
    public void confirmAccount(String code) {

        ConfirmationCode entity = confirmationCodeRepository.findByCode(code).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (entity!= null && now.isBefore(entity.getExpired())){
            User user = repository.findById(entity.getUser().getId()).orElse(null);
            user.setActive(true);
        }
    }
}
