package edu.hw5;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Task5 {

    private final static String NULL_ID_MESSAGE = "Id can't be null.";
    private final static String EMPTY_ID_MESSAGE = "Vehicle id can't be empty.";

    private Task5() {

    }

    public static boolean isValidVehicleId(String vehicleId) {
        if (vehicleId == null) {
            throw new IllegalArgumentException(NULL_ID_MESSAGE);
        }
        if (vehicleId.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_ID_MESSAGE);
        }
        String allowedWords = "АВЕКМНОРСТУХ";
        Pattern validationPattern =
            Pattern.compile("^[" + allowedWords + "]\\d{3}[" + allowedWords + "]{2}(0[1-9]|[1-9]\\d{1,2})$");
        Matcher matcher = validationPattern.matcher(vehicleId);
        return matcher.find();
    }

}
