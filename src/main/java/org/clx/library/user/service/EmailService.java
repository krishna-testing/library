package org.clx.library.user.service;

import org.clx.library.user.dto.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
