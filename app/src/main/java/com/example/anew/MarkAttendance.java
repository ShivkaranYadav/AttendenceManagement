package com.example.anew;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarkAttendance {
    public String sname, enrollment, semester, subject, datetime;

    public MarkAttendance(String name, String enrollment_no, String sem, String sub, String dnt){
        sname = name;
        enrollment = enrollment_no;
        semester = sem;
        subject = sub;
        datetime = dnt;
    }

}