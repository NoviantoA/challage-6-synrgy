package com.novianto.challange6.controller;

import com.novianto.challange6.dto.UserDto;
import com.novianto.challange6.entity.User;
import com.novianto.challange6.repository.UserRepository;
import com.novianto.challange6.service.UserService;
import com.novianto.challange6.service.impl.ReportUser;
import com.novianto.challange6.util.ConfigValidation;
import com.novianto.challange6.util.Response;
import com.novianto.challange6.util.SimpleStringUtil;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpleStringUtil simpleStringUtil;
    @Autowired
    private Response response;
    @Autowired
    private HttpServletResponse responseHttp;
    @Autowired
    private ReportUser reportUser;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> saveUser(@RequestBody UserDto request) {
        try {
            return new ResponseEntity<Map>(userService.saveUser(request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = {"/update/{userId}", "/update/{userId}/"})
    public ResponseEntity<Map> updateUser(@RequestBody UserDto request, @PathVariable("userId") UUID userId) {
        try {
            if (userId == null) {
                return new ResponseEntity<Map>(response.routeNotFound(ConfigValidation.ID_USER_REQUIRED), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Map>(userService.updateUser(userId, request), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = {"/delete/{userId}", "/delete/{userId}/"})
    public ResponseEntity<Map> deleteUser(@PathVariable("userId") UUID userId) {
        try {
            return new ResponseEntity<Map>(userService.deleteUser(userId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @GetMapping(value = {"/get/{userId}", "/get/{userId}/"})
    public ResponseEntity<Map> getUserById(@PathVariable("userId") UUID userId) {
        try {
            return new ResponseEntity<Map>(response.successResponse(userRepository.getByIdUser(userId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Map>(response.routeNotFound(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    @GetMapping("/all-users")
    public Page<User> getAllUser(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUser(pageable);
    }

    @GetMapping(value = {"/list-spec", "/list-spec/"})
    public ResponseEntity<Map> listUserHeaderSpec(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtil.getShort(orderby, ordertype, page, size);
        Specification<User> spec =
                ((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (username != null && !username.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
                    }
                    if (email != null && !email.isEmpty()) {
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                });
        Page<User> list = userRepository.findAll(spec, show_data);
        return new ResponseEntity<Map>(response.successResponse(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/reports")
    public void getUserReport() throws JRException, IOException {
        responseHttp.setContentType("application/pdf");
        responseHttp.setHeader("Content-Disposition", "attachment; filename=\"user_list.pdf\"");
        JasperPrint jasperPrint = reportUser.jasperPrint();
        JasperExportManager.exportReportToPdfStream(jasperPrint, responseHttp.getOutputStream());
    }

}
