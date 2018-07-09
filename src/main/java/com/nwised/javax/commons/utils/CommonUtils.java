/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nwised.javax.commons.utils;


import com.google.common.hash.Hashing;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thilina_h
 */
public class CommonUtils {

    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int SHA256LENGHT = 32;
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final LocalDateTime SO_OLD = LocalDateTime.of(1900, Month.MARCH, 27, 0, 0);

    public static InputStream strToStream(String decrypt_input) {
        return IOUtils.toInputStream(decrypt_input, StandardCharsets.UTF_8);
    }

    public static String streamToStr(InputStream stream) throws IOException {
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

    public static enum DATE_TIME_FORMATS {
        DD_MM_YY("ddMMyy"), MM_DD_YY("MMddyy"), DD_MMM_YYYY_SPACE("dd MMM yyyy"), DD_MM_YYYY_SLASH("dd/MM/yyyy"),
        DD_MM_YYYY_DASH("dd-MM-yyyy"), yyyy_MM_dd_DASH("yyyy-MM-dd"), dd_MMM_yyyy_hh_mm_a("dd MMM yyyy hh:mm a"),
        DD_MM_YYYY_DASH$HH_mm_ss("dd-MM-yyyy HH:mm:ss"), ISO_FORMAT(DateTimeFormatter.ISO_DATE_TIME),
        h_mm_a_COLON("h:mm a"), MM_dd_yyyy_SLASH_SEP("MM/dd/yyyy"), yyyyMMdd("yyyyMMdd"), yyyyMMddHHmm("yyyyMMddHHmm"),
        yyyyMMddHHmmss("yyyyMMddHHmmss");

        private final DateTimeFormatter formatter;

        private DATE_TIME_FORMATS(String format) {
            this.formatter = DateTimeFormatter.ofPattern(format);
        }

        private DATE_TIME_FORMATS(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        public DateTimeFormatter getFormatter() {
            return formatter;
        }

        public String format(LocalDateTime date) {
            return formatter.format(date);
        }

        public String format(LocalDate date) {
            return formatter.format(date);
        }

        public String format(LocalTime date) {
            return formatter.format(date);
        }

        /**
         * @param date
         * @return returns null if param is null or empty.
         */
        public LocalDate parseDate(String date) {
            if (date != null && !date.isEmpty()) {
                return LocalDate.parse(date, formatter);
            }
            return null;
        }

        /**
         * @param date_time
         * @return returns null if param is null or empty.
         */
        public LocalDateTime parseDateTime(String date_time) {
            if (date_time != null && !date_time.isEmpty()) {
                return LocalDateTime.parse(date_time, formatter);
            }
            return null;
        }

    }

    public static Instant getCurrentTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
    }

    //public final static SimpleDateFormat clientInputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static BigDecimal setTwoDecimalPoints(BigDecimal number) {
        return number.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal strToFormattedBigDecimal(String number) {
        return setTwoDecimalPoints(new BigDecimal(number));
    }


    public static LocalDate parseLocalDate(String date, DATE_TIME_FORMATS format) {
        return LocalDate.parse(date, format.getFormatter());
    }


    /**
     * @param date
     * @param from-format of the input date
     * @param to          -required output format
     * @return
     */
    public static String changeDateFormat(String date, DATE_TIME_FORMATS from, DATE_TIME_FORMATS to) {
        return to.getFormatter().format(parseLocalDate(date, from));
    }

    public static String localDateToString(LocalDate date, DATE_TIME_FORMATS format) {
        return format.getFormatter().format(date);
    }

    public static LocalDate parseAnyClientDate(String date) {
        int indexOfSlash = date.indexOf("-");
        try {
            if (indexOfSlash > 2) {//yyyy-MM-dd
                return LocalDate.parse(date, DATE_TIME_FORMATS.yyyy_MM_dd_DASH.getFormatter());
            }
            return LocalDate.parse(date, DATE_TIME_FORMATS.DD_MM_YYYY_DASH.getFormatter());
        } catch (DateTimeParseException ex) {
            Logger.getGlobal().log(Level.WARNING, "error parsing date :" + date);
            return LocalDate.MIN;
        }
    }

    public static LocalDateTime parseDateTimeOrReturnCurntTimeOnFall(String date, DateTimeFormatter format) {
        try {
            return LocalDateTime.parse(date, format);
        } catch (DateTimeParseException ex) {
            Logger.getGlobal().log(Level.WARNING, "error parsing date :" + date + " to " + format.toString());
            return LocalDateTime.now();
        }
    }

    public static LocalDate _DateToLocalDate(java.util.Date date) {
        return LocalDate.from(date.toInstant());
    }

    public static LocalDateTime _DateToLocalDateTime(java.util.Date date) {
        return LocalDateTime.from(date.toInstant());
    }


    public static java.sql.Timestamp LocalDateToSQLTimeStamp(LocalDateTime dtime) {
        return java.sql.Timestamp.valueOf(dtime);
    }


    public static String readStringFromStream(InputStream inputStream) {
        //http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            //inputStream=new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int length;

            if (inputStream.markSupported()) {
                System.out.println("mark supported");
                inputStream.mark(inputStream.available() + 1);
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                inputStream.reset();
            } else {
                System.out.println("mark not supported");
                BufferedInputStream buf = IOUtils.buffer(inputStream);

                while ((length = buf.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }

                //}
            }
            return result.toString("UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(CommonUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String sha256Hex(String input) {
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }

    public static int generateRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);

    }

    public static String encodeImage(Object value) {
        if (value != null && value instanceof Blob) {
            try {
                Blob img = (Blob) value;
                byte[] img_bytes = img.getBytes(1, (int) img.length());
                String img_b64 = new String(Base64.getEncoder().encode(img_bytes));
                img.free();
                return img_b64;
            } catch (SQLException ex) {
                Logger.getGlobal().log(Level.WARNING, "ERROR fetching user image", ex);
            }
        }
        return null;
    }

    public static long unitsBetween(Temporal temp_inclusive, Temporal temp_exclusive, ChronoUnit unit) {
        return unit.between(temp_inclusive, temp_exclusive);
    }

    public static <T> T getArrayValue(T[] array, int position, T def) {
        try {
            return array[position];
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            StringBuilder build = new StringBuilder();
            for (T element : array) {
                build.append(element).append(",");
            }
            build.deleteCharAt(build.length() - 1);//removes last ','
            Logger.getGlobal().log(Level.WARNING, "Error accesing array index:" + position + System.lineSeparator() + build.toString(), ex);

            return def;
        }
    }

    public static String maskMobile(final @Size(min = 9) String mobile) {
        final int start = 4;
        final int end = mobile.length() - 3;
        final String overlay = StringUtils.repeat("#", end - start);
        return StringUtils.overlay(mobile, overlay, start, end);
    }

    public static String maskEmail(final @NotNull String email) {
        return email.replaceAll("(?<=.{3}).(?=[^@]*?@)", "*");
//        String[] split = email.split("@");
//        String left_most = split[0];
//        final int start = 4;
//        final int end = email.length() - 3;
//        final String overlay = StringUtils.repeat("#", end - start);
//        return StringUtils.overlay(email, overlay, start, end);
    }

    public static byte[] generateSalt() {

        byte bytes[] = new byte[20];
        RANDOM.nextBytes(bytes);
        return bytes;
    }
    public static String generateSaltStr() {

        byte bytes[] = new byte[20];
        RANDOM.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

}
