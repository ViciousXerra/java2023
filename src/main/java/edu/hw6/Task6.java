package edu.hw6;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public final class Task6 {

    private final static int USABLE_PORT_LIMIT = 49152;
    private final static int TWO_DIGITS_NUM = 10;
    private final static int THREE_DIGITS_NUM = 100;
    private final static int FOUR_DIGITS_NUM = 1000;
    private final static int FIVE_DIGITS_NUM = 10000;

    private final static int FTP_PORT = 21;
    private final static int SSH_PORT = 22;
    private final static int SMTP_PORT = 25;
    private final static int DNS_PORT = 53;
    private final static int HTTP_PORT = 80;
    private final static int HTTPS_PORT = 443;
    private final static int ORACLE_DB_PORT = 1521;
    private final static int MYSQL_DB_PORT = 3306;
    private final static int RDP_PORT = 3389;
    private final static int POSTGRESQL_DB_PORT = 5432;
    private final static int MONGO_DB_PORT = 27017;


    private final static Map<Integer, String> COMMON_PORTS = new HashMap<>() {
        {
            //Well-Known Ports
            put(FTP_PORT, "FTP (File Transfer Protocol)");
            put(SSH_PORT, "SSH (Secure Shell)");
            put(SMTP_PORT, "SMTP (Simple Mail Transfer Protocol)");
            put(DNS_PORT, "DNS (Domain Name System)");
            put(HTTP_PORT, "HTTP (HyperText Transfer Protocol)");
            put(HTTPS_PORT, "HTTPS (HyperText Transfer Protocol Secure)");
            //Registered Ports
            put(ORACLE_DB_PORT, "Oracle Database");
            put(MYSQL_DB_PORT, "MySQL Database");
            put(RDP_PORT, "Remote Desktop Protocol (RDP)");
            put(POSTGRESQL_DB_PORT, "PostgreSQL Database");
            put(MONGO_DB_PORT, "MongoDB Database");
        }
    };

    private Task6() {

    }

    public static String getPortsInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Protocol  Port   Possible service");
        builder.append(System.lineSeparator());
        for (int i = 0; i < USABLE_PORT_LIMIT; i++) {
            /*
            Что писать, если делать с try with resources, на пустой блок try
            ругается чекстайл
             */
            try (ServerSocket socket = new ServerSocket(i)) {
                continue;
            } catch (IOException e) {
                appendOccupiedPortInfo("TCP", i, builder);
            }
            try (DatagramSocket socket = new DatagramSocket(i)) {
                continue;
            } catch (IOException e) {
                appendOccupiedPortInfo("UDP", i, builder);
            }
        }
        return builder.toString();
    }

    private static void appendOccupiedPortInfo(String portType, int port, StringBuilder builder) {
        builder.append(portType).append("       ").append(port);
        if (port < TWO_DIGITS_NUM) {
            builder.append("      ");
        } else if (port < THREE_DIGITS_NUM) {
            builder.append("     ");
        } else if (port < FOUR_DIGITS_NUM) {
            builder.append("    ");
        } else if (port < FIVE_DIGITS_NUM) {
            builder.append("   ");
        } else {
            builder.append("  ");
        }
        builder.append(COMMON_PORTS.getOrDefault(port, "N/A")).append(System.lineSeparator());
    }

}
