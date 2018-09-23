package com.oofgz.fight;

import com.oofgz.fight.mail.VelocityEngineBean;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class MailSendCaseTests {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VelocityEngineBean velocityEngineBean;

    @Test
    public void sendSimpleMailTest() {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1158603417@qq.com");
        message.setTo("948913395@qq.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");

        mailSender.send(message);
    }

    @Test
    public void sendAttachmentsMailTest() throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("1158603417@qq.com");
        helper.setTo("948913395@qq.com");
        helper.setSubject("主题：有附件");
        helper.setText("有附件的邮件");

        FileSystemResource file = new FileSystemResource(new File("weixin.jpg"));
        helper.addAttachment("附件-1.jpg", file);
        helper.addAttachment("附件-2.jpg", file);

        mailSender.send(mimeMessage);
    }

    @Test
    public void sendInlineMailTest() throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("1158603417@qq.com");
        helper.setTo("948913395@qq.com");
        helper.setSubject("主题：嵌入静态资源");
        helper.setText("<html><body><img src=\"cid:weiXin\" ></body></html>", true);

        FileSystemResource file = new FileSystemResource(new File("src/assets/weiXin.jpg"));
        helper.addInline("weiXin", file);

        mailSender.send(mimeMessage);
    }

    @Test
    public void sendTemplateMailTest() throws Exception {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("1158603417@qq.com");
        helper.setTo("948913395@qq.com");
        helper.setSubject("主题：模板邮件");

        VelocityContext context = new VelocityContext();
        context.put("username", "didi_dada");
        StringWriter stringWriter = new StringWriter();
        velocityEngineBean.mergeTemplate("src/main/resources/templates/mailTemplate.vm", "UTF-8", context, stringWriter);
        String text = stringWriter.toString();
        helper.setText(text, true);

        mailSender.send(mimeMessage);
    }

}
