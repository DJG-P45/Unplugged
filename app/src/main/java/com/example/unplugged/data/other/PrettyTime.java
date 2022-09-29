package com.example.unplugged.data.other;

import java.time.Duration;

public class PrettyTime {

    public static String beautify(Duration duration) {
        return   duration.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
    }

}
