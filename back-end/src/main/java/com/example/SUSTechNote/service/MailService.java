package com.example.SUSTechNote.service;

import java.io.File;
import java.util.List;

public interface MailService {

    String sendSimpleMail(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content);

    void sendMailWithAttachments(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content,
                                 List<File> files);

    void sendMailWithImage(String mailFrom, String mailFromNick, String mailTo, String cc, String subject, String content,
                           String[] imagePaths, String[] imageId);
}
