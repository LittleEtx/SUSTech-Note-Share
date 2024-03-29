package com.example.SUSTechNote.service.Impl;

import com.example.SUSTechNote.service.MailService;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Service("mailService")
public class MailServiceImpl implements MailService {

    /**
     * JavaMailSender是Spring Boot在MailSenderPropertiesConfiguration 类中配直好的，该类在 Mail
     * 自动配置类 MailSenderAutoConfiguration 中导入 因此这里注入 JavaMailSender 就可以使用了
     */
    @Resource
    private JavaMailSender mailSender;


    /**
     * 1、发送普通文本邮件
     *
     * @param mailFrom     发件人邮箱
     * @param mailFromNick 发件人昵称
     * @param mailTo       收件人邮箱
     * @param cc           抄送人邮箱(可为空，方法内部处理)
     * @param subject      主题
     * @param content      内容
     */
    @Override
    public String sendSimpleMail(String mailFrom, String mailFromNick, String mailTo, String cc,
                               String subject, String content) {
        try {
            // 多个收件人之间用英文逗号分隔
            String[] mailToArr = mailTo.split(",");
            for (String address : mailToArr) {
                // 简单邮件信息类
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                // HTML邮件信息类
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                // 昵称
                mimeMessageHelper.setFrom(new InternetAddress(mailFromNick + " <" + mailFrom + ">"));
                mimeMessageHelper.setTo(address);
                if (!StringUtils.isEmpty(cc)) {
                    mimeMessageHelper.setCc(cc);
                }
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content);

                mailSender.send(mimeMessage);
            }
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    /**
     * 2、发送带附件的邮件
     *
     * @param mailFrom     发件人
     * @param mailFromNick 发件人昵称
     * @param mailTo       收件人
     * @param cc           抄送人
     * @param subject
     * @param content
     * @param files
     */
    @Override
    public void sendMailWithAttachments(String mailFrom, String mailFromNick, String mailTo, String cc,
                                        String subject, String content, List<File> files) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            /*
            第二个参数true表示构造一个multipart message类型的邮件，multipart message类型的邮件包含多个正文、附件以及内嵌资源，
            邮件的表现形式更丰富
             */
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(new InternetAddress(mailFromNick + " <" + mailFrom + ">"));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);

            // 设置多个收件人
            String[] toAddress = mailTo.split(",");
            mimeMessageHelper.setTo(toAddress);
            if (!StringUtils.isEmpty(cc)) {
                mimeMessageHelper.setCc(cc);
            }
            // 添加附件
            if (null != files) {
                for (File file : files) {
                    // 通过addAttachment方法添加附件
                    mimeMessageHelper.addAttachment(file.getName(), file);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        //发送邮件
        mailSender.send(mimeMessage);
    }

    /**
     * 3、正文内容带图片
     *
     * @param mailFrom
     * @param mailFromNick
     * @param mailTo
     * @param cc
     * @param subject
     * @param content
     * @param imagePaths
     * @param imageId
     */
    @Override
    public void sendMailWithImage(String mailFrom, String mailFromNick, String mailTo, String cc, String subject,
                                  String content, String[] imagePaths, String[] imageId) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(new InternetAddress(mailFromNick + " <" + mailFrom + ">"));
            // 设置多个收件人
            String[] toAddress = mailTo.split(",");
            mimeMessageHelper.setTo(toAddress);
            if (!StringUtils.isEmpty(cc)) {
                mimeMessageHelper.setCc(cc);
            }
            mimeMessageHelper.setSubject(subject);
            // 第二个参数为true表示邮件正文是html格式的，默认是false
            mimeMessageHelper.setText(content, true);
            // 添加图片
            if (imagePaths != null && imagePaths.length != 0) {
                for (int i = 0; i < imagePaths.length; i++) {
                    // 通过FileSystemResource构造静态资源(获取文件)
                    // ==默认从文件系统的当前路径加载book.xml资源-FileSystemResource fr = new FileSystemResource("book.xml");==
                    FileSystemResource fileSystemResource = new FileSystemResource(imagePaths[i]);
                    // 调用addInline方法将资源加入邮件对象中
                    mimeMessageHelper.addInline(imageId[i], fileSystemResource);
                }
            }
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
}
