package com.novianto.challange6.util;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class Response {
    public <T> Map<String, Object> successResponse(T data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("status", 200);
        response.put("message", "Success");
        return response;
    }

    public <T> Map<String, Object> errorResponse(T message, int status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        return response;
    }

    public boolean isValidName(String name) {
        String nameRegex = "^[a-zA-Z\\s]*$";
        Pattern pattern = Pattern.compile(nameRegex);
        return pattern.matcher(name).matches();
    }

    public boolean isValidPrice(Double price) {
        if (price == null) {
            return false;
        }
        String priceString = Double.toString(price);
        String priceRegex = "^[0-9]+(\\.[0-9]+)?$";
        Pattern pattern = Pattern.compile(priceRegex);
        return pattern.matcher(priceString).matches();
    }

//    public boolean isValidDate(Object dateObj) {
//        if (dateObj == null) {
//            return false;
//        }
//        String dateStr = String.valueOf(dateObj);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setLenient(false);
//        try {
//            dateFormat.parse(dateStr);
//            return true;
//        } catch (ParseException e) {
//            return false;
//        }
//    }

    public boolean isValidDate(Date date) {
        if (date == null) {
            return false;
        }
        return true;
    }


    public boolean isValidQuantity(Integer quantity) {
        if (quantity == null) {
            return false;
        }
        if (quantity < 0) {
            return false;
        }
        return true;
    }

    public String defaultIfNull(String obj) {
        return obj != null ? obj : "";
    }

    public double defaultIfNull(Double obj) {
        return obj != null ? obj : 0.0;
    }

    public int defaultIfNull(Integer obj) {
        return obj != null ? obj : 0;
    }

    public boolean defaultIfNull(Boolean obj) {
        return obj != null ? obj : false;
    }

    public boolean isRequired(Object obj) {
        return obj != null;
    }

    public boolean isRequired(String obj) {
        return obj != null;
    }

    public Map<String, Object> successTemplateResponse(Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("message", "Success");
        response.put("status", 200);
        return response;
    }

    public Map<String, Object> routeNotFound(Object message) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", 404);
        return response;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
