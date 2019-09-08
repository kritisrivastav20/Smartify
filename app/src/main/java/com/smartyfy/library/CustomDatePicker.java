package com.smartyfy.library;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.smartyfy.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tecno Waev on 02-04-2016.
 */
public class CustomDatePicker {
    public CustomDatePicker() {}

    public static void dateListener(final EditText lv_editText, final Context lv_context, final boolean isPast, final boolean isFuture, final boolean isToday) {
        final DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(lv_editText, myCalendar, isPast, isFuture, isToday, lv_context);
            }
        };

        final SimpleDateFormat sdf_parse = new SimpleDateFormat("d-M-y", Locale.ENGLISH);
        lv_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalendar = Calendar.getInstance();
                if (lv_editText.getText().length() > 0) {
                    try {
                        myCalendar.setTime(sdf_parse.parse(lv_editText.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                InputMethodManager imm = (InputMethodManager) lv_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                DatePickerDialog lv_dialog = new DatePickerDialog(lv_context, dob, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if (isFuture)
                    lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                if (isPast)
                    lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                if (isToday) {
                    lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                lv_dialog.show();
            }
        });

        lv_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar myCalendar = Calendar.getInstance();
                    if (lv_editText.getText().length() > 0) {
                        try {
                            myCalendar.setTime(sdf_parse.parse(lv_editText.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) lv_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    DatePickerDialog lv_dialog = new DatePickerDialog(lv_context, dob, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    if (isFuture)
                        lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    if (isPast)
                        lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                    if (isToday) {
                        lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    }
                    lv_dialog.show();
                }
            }
        });
    }

    public static void timeListener(final EditText lv_editText, final Context lv_context) {
        //Time Setting
        final TimePickerDialog.OnTimeSetListener follow_time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTimeLabel(lv_editText, myCalendar);

            }
        };

        lv_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar lv_frm_cal = Calendar.getInstance();
                try {
                    lv_frm_cal.setTime(new SimpleDateFormat("HH:mm").parse(lv_editText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new TimePickerDialog(lv_context, AlertDialog.THEME_DEVICE_DEFAULT_DARK, follow_time, lv_frm_cal.get(Calendar.HOUR_OF_DAY), lv_frm_cal.get(Calendar.MINUTE), false).show();
            }
        });
        lv_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar lv_frm_cal = Calendar.getInstance();
                    try {
                        lv_frm_cal.setTime(new SimpleDateFormat("HH:mm").parse(lv_editText.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new TimePickerDialog(lv_context, AlertDialog.THEME_DEVICE_DEFAULT_DARK, follow_time, lv_frm_cal.get(Calendar.HOUR_OF_DAY), lv_frm_cal.get(Calendar.MINUTE), false).show();
                }
            }
        });
    }

    public static void updateTimeLabel(EditText label, Calendar myCalendar) {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        label.setText(sdf.format(myCalendar.getTime()));
    }

    public static void updateDateLabel(EditText label, Calendar myCalendar, boolean isPast, boolean isFuture, boolean isToday, Context lv_context) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        if (isPast || isFuture || isToday) {
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
                Date lv_frm_date;
                try {
                    lv_frm_date = sdf.parse(sdf.format(myCalendar.getTime()));
                    Date lv_curr_date = sdf.parse(sdf.format(new Date()));
                    boolean condition = false;
                    String lv_error_msg = "";
                    if (isPast) {
                        condition = lv_frm_date.compareTo(lv_curr_date) >= 0;
                        lv_error_msg = "Date should not be greater than current date";
                    }
                    if (isFuture) {
                        condition = lv_frm_date.compareTo(lv_curr_date) <= 0;
                        lv_error_msg = "Date should not be less than current date";
                    }
                    if (isToday) {
                        condition = lv_frm_date.compareTo(lv_curr_date) == 0;
                        lv_error_msg = "Date should be today date";
                    }
                    if (condition) {
                        label.setText(sdf.format(new Date()));
                        Toast.makeText(lv_context, lv_error_msg, Toast.LENGTH_SHORT).show();
                    } else label.setText(sdf.format(lv_frm_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else label.setText(sdf.format(myCalendar.getTime()));
        } else
            label.setText(sdf.format(myCalendar.getTime()));
    }

    public static void updateDateLabel(EditText source_editText, EditText target_editText, Calendar myCalendar, boolean isPast, boolean isFuture, boolean isToday, Context lv_context, int duration, String period) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        if (isPast || isFuture || isToday) {
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
                Date lv_frm_date;
                try {
                    lv_frm_date = sdf.parse(sdf.format(myCalendar.getTime()));
                    Date lv_curr_date = sdf.parse(sdf.format(new Date()));
                    boolean condition = false;
                    String lv_error_msg = "";
                    if (isPast) {
                        condition = lv_frm_date.compareTo(lv_curr_date) >= 0;
                        lv_error_msg = "Date should not be greater than current date";
                    }
                    if (isFuture) {
                        condition = lv_frm_date.compareTo(lv_curr_date) <= 0;
                        lv_error_msg = "Date should not be less than current date";
                    }
                    if (isToday) {
                        condition = lv_frm_date.compareTo(lv_curr_date) == 0;
                        lv_error_msg = "Date should be today date";
                    }
                    if (condition) {
                        source_editText.setText(sdf.format(new Date()));
                        Toast.makeText(lv_context, lv_error_msg, Toast.LENGTH_SHORT).show();
                    } else source_editText.setText(sdf.format(lv_frm_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else source_editText.setText(sdf.format(myCalendar.getTime()));
        } else
            source_editText.setText(sdf.format(myCalendar.getTime()));
        update_target_date(source_editText, target_editText, duration, period);
    }

    public static void updateTomorrowDate(EditText label) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        label.setText(sdf.format(calendar.getTime()));
    }


    public static void dateListener(final AutoCompleteTextView lv_editText, final Context lv_context, final boolean isPast, final boolean isFuture, final boolean isToday) {
        final DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(lv_editText, myCalendar, isPast, isFuture, isToday, lv_context);
            }
        };

        final SimpleDateFormat sdf_parse = new SimpleDateFormat("d-M-y", Locale.ENGLISH);
        lv_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalendar = Calendar.getInstance();
                if (lv_editText.getText().length() > 0) {
                    try {
                        myCalendar.setTime(sdf_parse.parse(lv_editText.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                InputMethodManager imm = (InputMethodManager) lv_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                DatePickerDialog lv_dialog = new DatePickerDialog(lv_context, dob, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if (isFuture)
                    lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                if (isPast)
                    lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                if (isToday) {
                    lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                lv_dialog.show();
            }
        });

        lv_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar myCalendar = Calendar.getInstance();
                    if (lv_editText.getText().length() > 0) {
                        try {
                            myCalendar.setTime(sdf_parse.parse(lv_editText.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) lv_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    DatePickerDialog lv_dialog = new DatePickerDialog(lv_context, dob, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    if (isFuture)
                        lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    if (isPast)
                        lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                    if (isToday) {
                        lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    }
                    lv_dialog.show();
                }
            }
        });
    }

    public static void timeListener(final AutoCompleteTextView lv_editText, final Context lv_context) {
        //Time Setting
        final TimePickerDialog.OnTimeSetListener follow_time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTimeLabel(lv_editText, myCalendar);

            }
        };

        lv_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar lv_frm_cal = Calendar.getInstance();
                try {
                    lv_frm_cal.setTime(new SimpleDateFormat("HH:mm").parse(lv_editText.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new TimePickerDialog(lv_context, follow_time, lv_frm_cal.get(Calendar.HOUR_OF_DAY), lv_frm_cal.get(Calendar.MINUTE), false).show();
            }
        });
        lv_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar lv_frm_cal = Calendar.getInstance();
                    try {
                        lv_frm_cal.setTime(new SimpleDateFormat("HH:mm").parse(lv_editText.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new TimePickerDialog(lv_context, follow_time, lv_frm_cal.get(Calendar.HOUR_OF_DAY), lv_frm_cal.get(Calendar.MINUTE), false).show();
                }
            }
        });
    }

    public static void updateTimeLabel(AutoCompleteTextView label, Calendar myCalendar) {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        label.setText(sdf.format(myCalendar.getTime()));
    }

    public static void updateDateLabel(AutoCompleteTextView label, Calendar myCalendar, boolean isPast, boolean isFuture, boolean isToday, Context lv_context) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        if (isPast || isFuture || isToday) {
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
                Date lv_frm_date;
                try {
                    lv_frm_date = sdf.parse(sdf.format(myCalendar.getTime()));
                    Date lv_curr_date = sdf.parse(sdf.format(new Date()));
                    boolean condition = false;
                    String lv_error_msg = "";
                    if (isPast) {
                        condition = lv_frm_date.compareTo(lv_curr_date) >= 0;
                        lv_error_msg = "Date should not be greater than current date";
                    }
                    if (isFuture) {
                        condition = lv_frm_date.compareTo(lv_curr_date) <= 0;
                        lv_error_msg = "Date should not be less than current date";
                    }
                    if (isToday) {
                        condition = lv_frm_date.compareTo(lv_curr_date) == 0;
                        lv_error_msg = "Date should be today date";
                    }
                    if (condition) {
                        label.setText(sdf.format(new Date()));
                        Toast.makeText(lv_context, lv_error_msg, Toast.LENGTH_SHORT).show();
                    } else label.setText(sdf.format(lv_frm_date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else label.setText(sdf.format(myCalendar.getTime()));
        } else
            label.setText(sdf.format(myCalendar.getTime()));
    }

    public static void dateListener(final EditText source_editText, final EditText target_editText, final Context lv_context, final boolean isPast, final boolean isFuture, final boolean isToday, final int duration, final String period) {
        final DatePickerDialog.OnDateSetListener dob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel(source_editText, target_editText, myCalendar, isPast, isFuture, isToday, lv_context, duration, period);
            }
        };

        final SimpleDateFormat sdf_parse = new SimpleDateFormat("d-M-y", Locale.ENGLISH);
        source_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar myCalendar = Calendar.getInstance();
                if (source_editText.getText().length() > 0) {
                    try {
                        myCalendar.setTime(sdf_parse.parse(source_editText.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                InputMethodManager imm = (InputMethodManager) lv_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                DatePickerDialog lv_dialog = new DatePickerDialog(lv_context, dob, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                if (isFuture)
                    lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                if (isPast)
                    lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                if (isToday) {
                    lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                lv_dialog.show();
            }
        });

        source_editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar myCalendar = Calendar.getInstance();
                    if (source_editText.getText().length() > 0) {
                        try {
                            myCalendar.setTime(sdf_parse.parse(source_editText.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) lv_context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    DatePickerDialog lv_dialog = new DatePickerDialog(lv_context, dob, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    if (isFuture)
                        lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    if (isPast)
                        lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
                    if (isToday) {
                        lv_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        lv_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    }
                    lv_dialog.show();
                }
            }
        });
    }

    private static void update_target_date(EditText source_editText, EditText target_editText, int duration, String period) {
        String fdate = source_editText.getText().toString();
        if(!fdate.equals("")) {
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date from_date;
            Calendar calendar = Calendar.getInstance();
            try {
                from_date = df.parse(fdate);
                calendar.setTime(from_date);
                switch (period) {
                    case "day":
                    case "day(s)":
                        calendar.add(Calendar.DATE, duration - 1);
                        break;
                    case "month":
                    case "month(s)":
                        calendar.add(Calendar.MONTH, duration);
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case "week":
                    case "week(s)":
                        calendar.add(Calendar.WEEK_OF_MONTH, duration);
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case "year":
                    case "year(s)":
                        calendar.add(Calendar.YEAR, duration);
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                }
                String to_date_string = df.format(calendar.getTime());
                target_editText.setText(to_date_string);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateTomorrowDate(AutoCompleteTextView label) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        label.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Help to parse the date from string
     * @param date the date present in string format
     * @param Format the format in which date is coming Eg: d-M-y
     * @param locale the locale to be used
     * @return Date in Date format
     * @throws ParseException
     */
    public static Date parseDate(String date, String Format, Locale locale) throws ParseException {
        return new SimpleDateFormat(Format, locale).parse(date);
    }

    /**
     * Help to format the date from Date -> String
     * @param date the date present in Date format
     * @param Format the format in which date is coming Eg: d-M-y
     * @param locale the locale to be used
     * @return Date in String format
     */
    public static String formatDate(Date date, String Format, Locale locale) {
        return new SimpleDateFormat(Format, locale).format(date);
    }

    /**
     * Helps in converting string date to string date of different format
     * @param date the date present in string format
     * @param actual_format the current format of the date passed
     * @param expected_format the expected format of date
     * @param locale the locale to be used
     * @return null if any error occurs or date in expected string format :)
     */
    public static String convertDateString(String date, String actual_format, String expected_format, Locale locale) {
        try {
            return formatDate(parseDate(date, actual_format, locale), expected_format, locale);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
