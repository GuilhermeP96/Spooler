package com.spooler.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    /**
     * Retorna o carimbo de data e hora atual formatado.
     * @return Carimbo de data e hora como uma String.
     */
    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
