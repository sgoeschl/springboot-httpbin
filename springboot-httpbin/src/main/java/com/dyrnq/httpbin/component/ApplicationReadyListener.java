package com.dyrnq.httpbin.component;

import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;

import java.util.List;

@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    Logger logger = LoggerFactory.getLogger(ApplicationReadyListener.class);

    @Autowired
    ServletContext servletContext;
    @Autowired
    private Environment environment;
    @Autowired
    private String schema;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        String serverPort = environment.getProperty("server.port");
        String serverAddress = environment.getProperty("server.address");
        String springMVCServletPath = environment.getProperty("spring.mvc.servlet.path");
        List<NetworkIF> networkInterfaces = hardware.getNetworkIFs(true);


        for (NetworkIF networkInterface : networkInterfaces) {
//            System.out.println("Name: " + networkInterface.getName());
//            System.out.println("Display name: " + networkInterface.getDisplayName());
//            System.out.println("MAC address: " + networkInterface.getMacaddr());
//            System.out.println("MTU: " + networkInterface.getMTU());
//            System.out.println("Speed: " + networkInterface.getSpeed() + " bps");
//            System.out.println("IPv4 addresses: " + networkInterface.getIPv4addr());
//            System.out.println("IPv6 addresses: " + networkInterface.getIPv6addr());
//            System.out.println("---");

            String[] ipv4Addresses = networkInterface.getIPv4addr();

            if (ipv4Addresses.length > 0) {
                for (String ipv4 : ipv4Addresses) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(schema);
                    sb.append("://");
                    sb.append(ipv4);
                    sb.append(StringUtils.equalsIgnoreCase("80", serverPort) ? "" : (":" + serverPort));
                    sb.append(servletContext.getContextPath());
                    if (springMVCServletPath != null && !"".equalsIgnoreCase(springMVCServletPath) && !"/".equalsIgnoreCase(springMVCServletPath)) {
                        sb.append(springMVCServletPath);
                        if (!springMVCServletPath.endsWith("/")) {
                            sb.append("/");
                        }
                    }
                    System.out.print(sb);
                }
                System.out.println();
            }
        }
    }
}
