package de.ait_tr.g_40_shop.service.Interfaces;

import de.ait_tr.g_40_shop.domain.entity.User;

public interface EmailService {

    void sendConfirmationEmail(User user);

}
