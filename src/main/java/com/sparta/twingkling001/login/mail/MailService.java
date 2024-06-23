package com.sparta.twingkling001.login.mail;

import com.sparta.twingkling001.member.dto.request.MemberReqDtoByMail;
import com.sparta.twingkling001.redis.RedisService;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    private MimeMessage createMessage(String token, String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);// 보내는 대상
        String verificationUrl = "http://localhost:8080/api/auth/mail/naver/verify?email=" + email + "&token=" + token;

        message.setSubject("Artify 회원가입 이메일 인증");// 제목

        String body = "<div>"
                + "<h1> 안녕하세요. 인증메일 입니다</h1>"
                + "<br>"
                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                + "<a href='"+ verificationUrl+ "'>인증 링크</a>"
                + "</div>";

        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("jjhhh6845@naver.com", "Artify_Admin"));// 보내는 사람
        return message;
    }

    public void sendMail(String token, String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = createMessage(token, email);
        javaMailSender.send(mimeMessage);
    }


//    public void checkUUID(String token, ){
//
//    }

//    @Transactional
//    public void add(MemberReqDtoByMail memberReqDtoByMail) throws Exception {
//        MemberRE.insert(member);
//        memberDao.updateToken(member);
//
//        String receiverMail = member.getEmail();
//        MimeMessage message = mailSender.createMimeMessage();
//
//        message.addRecipients(RecipientType.TO, receiverMail);// 보내는 대상
//        message.setSubject("Artify 회원가입 이메일 인증");// 제목
//
//        String body = "<div>"
//                + "<h1> 안녕하세요. Artify 입니다</h1>"
//                + "<br>"
//                + "<p>아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
//                + "<a href='http://localhost:3000/auth/verify?token=" + member.getToken() + "'>인증 링크</a>"
//                + "</div>";
//
//        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
//        // 보내는 사람의 이메일 주소, 보내는 사람 이름
//        message.setFrom(new InternetAddress("bitcamp1@naver.com", "Artify_Admin"));// 보내는 사람
//        mailSender.send(message); // 메일 전송
//    }
//
    public boolean checkToken(String email, String token) {
        String originToken = redisService.getValues(email);
        if(token.equals(originToken)){
            return true;
        }else{
            return false;
        }
    }

//    public long sendCertificationMail(String email) throws Exception {
//        if (userProvider.checkEmail(email) == 1) {
//            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);
//        }
//        try {
//            String code = UUID.randomUUID().toString().substring(0, 6);
//            sendMail(code, email);
//
//            return mailDao.createVerificationCode(code, email);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            throw new Exception(BaseResponseStatus.DATABASE_ERROR);
//        }
//    }
}
