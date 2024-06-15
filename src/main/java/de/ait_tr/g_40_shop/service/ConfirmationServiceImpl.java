package de.ait_tr.g_40_shop.service;

import de.ait_tr.g_40_shop.domain.entity.ConfirmationCode;
import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.repository.ConfirmationCodeRepository;
import de.ait_tr.g_40_shop.repository.UserRepository;
import de.ait_tr.g_40_shop.service.Interfaces.ConfirmationService;
import de.ait_tr.g_40_shop.service.Interfaces.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationCodeRepository repository;
    private final UserRepository userRepository;
    public ConfirmationServiceImpl(ConfirmationCodeRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public String generateConfirmationCode(User user) {

        String code = UUID.randomUUID().toString();
        LocalDateTime expired = LocalDateTime.now().plusMinutes(2);
        ConfirmationCode confirmationCode = new ConfirmationCode(code, expired, user);
        repository.save(confirmationCode);

        return code;
    }

    @Override
    public void confirmAccount(String code) {

        ConfirmationCode entity = repository.findByCode(code).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (entity!= null && now.isBefore(entity.getExpired())){
            User user = userRepository.findById(entity.getUser().getId()).orElse(null);
            if (user!=null){
                user.setActive(true);
                userRepository.save(user);
            }
        }
    }
}
