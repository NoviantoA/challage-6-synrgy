package com.novianto.challange6.controller.auth;

import com.novianto.challange6.config.ConfigSecurity;
import com.novianto.challange6.dto.RegisterDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserAuthService;
import com.novianto.challange6.service.mail.EmailSender;
import com.novianto.challange6.util.EmailTemplate;
import com.novianto.challange6.util.Response;
import com.novianto.challange6.util.SimpleStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user-register/")
public class RegisterController {
    @Autowired
    private UserRepository userRepository;
    ConfigSecurity config = new ConfigSecurity();
    @Autowired
    public UserAuthService serviceReq;
    @Autowired
    public Response templateCRUD;
    @Autowired
    public EmailSender emailSender;
    @Autowired
    public EmailTemplate emailTemplate;
    @Value("${expired.token.password.minute:}")
    private int expiredToken;
    @Value("${BASEURL:}")
    private String BASEURL;
    @Autowired
    private Response response;

    @PostMapping("/register")
    public ResponseEntity<Map> saveRegisterManual(@Valid @RequestBody RegisterDto registerDto) throws RuntimeException {
        Map map = new HashMap();
        User user = userRepository.checkExistingEmail(registerDto.getUsername());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.errorTemplateResponse("Username atau email sudah ada"), HttpStatus.OK);
        }
        map = serviceReq.registerManual(registerDto);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping("/register-google")
    public ResponseEntity<Map> saveRegisterByGoogle(@Valid @RequestBody RegisterDto objModel) throws RuntimeException {
        Map map = new HashMap();

        User user = userRepository.checkExistingEmail(objModel.getUsername());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.errorTemplateResponse("Username sudah ada"), HttpStatus.OK);
        }
        map = serviceReq.registerByGoogle(objModel);
        Map mapRegister = sendEmailegister(objModel);
        return new ResponseEntity<Map>(mapRegister, HttpStatus.OK);

    }

    @PostMapping("/send-otp")
    public Map sendEmailegister(@RequestBody RegisterDto user) {
        String message = "Thanks, please check your email for activation.";
        if (user.getUsername() == null) return templateCRUD.successResponse("No email provided");
        User found = userRepository.findOneByUsername(user.getUsername());
        if (found == null) return templateCRUD.errorTemplateResponse("Email not found");

        String template = emailTemplate.getRegisterTemplate();
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
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        return templateCRUD.successResponse(message);
    }

    @GetMapping("/register-confirm-otp/{token}")
    public ResponseEntity<Map> saveRegisterManual(@PathVariable(value = "token") String tokenOtp) throws RuntimeException {
        User user = userRepository.findOneByOTP(tokenOtp);
        if (null == user) {
            return new ResponseEntity<Map>(templateCRUD.errorTemplateResponse("OTP tidak ditemukan"), HttpStatus.OK);
        }
        if (user.isEnabled()) {
            return new ResponseEntity<Map>(templateCRUD.successResponse("Akun Anda sudah aktif, Silahkan melakukan login"), HttpStatus.OK);
        }
        String today = config.convertDateToString(new Date());
        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if (Long.parseLong(today) > Long.parseLong(dateToken)) {
            return new ResponseEntity<Map>(templateCRUD.errorTemplateResponse("Your token is expired. Please Get token again."), HttpStatus.OK);
        }
        user.setEnabled(true);
        userRepository.save(user);
        return new ResponseEntity<Map>(templateCRUD.successResponse("Sukses, Silahkan Melakukan Login"), HttpStatus.OK);
    }

    @PostMapping("/register-google-tymeleaf")
    public ResponseEntity<Map> saveRegisterByGoogleTyemeleaf(@Valid @RequestBody RegisterDto objModel) throws RuntimeException {
        Map map = new HashMap();
        User user = userRepository.checkExistingEmail(objModel.getUsername());
        if (null != user) {
            return new ResponseEntity<Map>(templateCRUD.errorTemplateResponse("Username sudah ada"), HttpStatus.OK);
        }
        map = serviceReq.registerByGoogle(objModel);
        Map mapRegister = sendEmailegisterTymeleaf(objModel);
        return new ResponseEntity<Map>(mapRegister, HttpStatus.OK);
    }

    @PostMapping("/send-otp-tymeleaf")
    public Map sendEmailegisterTymeleaf(@RequestBody RegisterDto user) {
        String message = "Thanks, please check your email for activation.";
        if (user.getUsername() == null) return templateCRUD.errorTemplateResponse("No email provided");
        User found = userRepository.findOneByUsername(user.getUsername());
        if (found == null) return templateCRUD.errorTemplateResponse("Email not found");
        String template = emailTemplate.getRegisterTemplate();
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
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername().toLowerCase()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "/v1/user-register/web/index/" + otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername().toLowerCase()));
            template = template.replaceAll("\\{\\{VERIFY_TOKEN}}", BASEURL + "/v1/user-register/web/index/" + found.getOtp());
        }
        emailSender.sendAsync(found.getUsername(), "Register", template);
        return templateCRUD.successResponse(message);
    }

//    @PostMapping("")
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Map> saveRegisterManualByGoogle(@Valid @RequestBody RegisterDto objModel) throws RuntimeException {
//        Map map = new HashMap();
//        User user = userRepository.checkExistingEmail(objModel.getEmailAddress());
//        if (null != user) {
//            return new ResponseEntity<Map>(response.errorTemplateResponse("Email is Registered, try another email or click Forget Password"), HttpStatus.OK);
//        }
//        map = serviceReq.registerManual(objModel);
//        return new ResponseEntity<Map>(map, HttpStatus.OK);
//    }
}
