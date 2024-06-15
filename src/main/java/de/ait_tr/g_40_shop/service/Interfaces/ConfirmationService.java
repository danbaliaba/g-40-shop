package de.ait_tr.g_40_shop.service.Interfaces;

import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.exception_handling.Response;

public interface ConfirmationService {

    String generateConfirmationCode(User user);

    void confirmAccount(String code);


}
