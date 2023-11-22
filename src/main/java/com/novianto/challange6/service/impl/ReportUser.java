package com.novianto.challange6.service.impl;

//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.util.JRLoader;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//@Service
public class ReportUser {

//    @Autowired
//    private DataSource dataSource;
//
//    private Connection getConnection() {
//        try {
//            return dataSource.getConnection();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public JasperPrint jasperPrint() throws IOException, JRException {
//        InputStream inputStream = new ClassPathResource("reports/User.jasper").getInputStream();
//        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, getConnection());
//        return jasperPrint;
//    }
}
