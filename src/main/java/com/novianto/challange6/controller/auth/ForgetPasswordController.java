package com.novianto.challange6.controller.auth;

import com.novianto.challange6.config.ConfigSecurity;
import com.novianto.challange6.dto.ResetPasswordDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserService;
import com.novianto.challange6.service.mail.EmailSender;
import com.novianto.challange6.util.EmailTemplate;
import com.novianto.challange6.util.Response;
import com.novianto.challange6.util.SimpleStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/forget-password/")
public class ForgetPasswordController {
    @Autowired
    private UserRepository userRepository;
    ConfigSecurity config = new ConfigSecurity();
    @Autowired
    public UserService serviceReq;
    @Value("${expired.token.password.minute:}")
    private int expiredToken;
    @Autowired
    public Response templateCRUD;
    @Autowired
    public EmailTemplate emailTemplate;
    @Autowired
    public EmailSender emailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/send")
    public Map sendEmailPassword(@RequestBody ResetPasswordDto user) {
        String message = "Thanks, please check your email";
        if (StringUtils.isEmpty(user.getEmail())) return templateCRUD.errorTemplateResponse("No email provided");
        User found = userRepository.findOneByUsername(user.getEmail());
        if (found == null) return templateCRUD.notFound("Email not found");
        String template = emailTemplate.getResetPassword();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtil.randomString(6, true);
                search = userRepository.findOneByOTP(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();
            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", otp);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? "" +
                    "@UserName" : "@" + found.getUsername()));
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername() == null ? "" +
                    "@UserName" : "@" + found.getUsername()));
            template = template.replaceAll("\\{\\{PASS_TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Chute - Forget Password", template);
        return templateCRUD.successResponse("success");
    }

    @PostMapping("/validate")
    public Map cheKTOkenValid(@RequestBody ResetPasswordDto model) {
        if (model.getOtp() == null) return templateCRUD.notFound("Token " + config.isRequired);
        User user = userRepository.findOneByOTP(model.getOtp());
        if (user == null) {
            return templateCRUD.errorTemplateResponse("Token not valid");
        }
        return templateCRUD.successResponse("Success");
    }

    @PostMapping("/change-password")
    public Map resetPassword(@RequestBody ResetPasswordDto model) {
        if (model.getOtp() == null) return templateCRUD.notFound("Token " + config.isRequired);
        if (model.getNewPassword() == null) return templateCRUD.notFound("New Password " + config.isRequired);
        User user = userRepository.findOneByOTP(model.getOtp());
        String success;
        if (user == null) return templateCRUD.notFound("Token not valid");
        user.setPassword(passwordEncoder.encode(model.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);
        try {
            userRepository.save(user);
            success = "success";
        } catch (Exception e) {
            return templateCRUD.errorTemplateResponse("Gagal simpan user");
        }
        return templateCRUD.successTemplateResponse(success);
    }
}