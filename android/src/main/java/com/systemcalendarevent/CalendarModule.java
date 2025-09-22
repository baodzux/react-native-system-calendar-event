package com.systemcalendarevent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class CalendarModule extends ReactContextBaseJavaModule {
    public CalendarModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SystemCalendarEvent";
    }

    @ReactMethod
    public void addEvent(String title, String startDate, String endDate, String location, String notes, Promise promise) {
        try {
            Context context = getReactApplicationContext();

            // Chuyển định dạng ISO sang milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            long startMillis = sdf.parse(startDate).getTime();
            long endMillis = sdf.parse(endDate).getTime();

            // Lấy calendar mặc định (có thể tuỳ chỉnh)
            String[] projection = new String[]{"_id", "calendar_displayName"};
            Cursor calCursor = context.getContentResolver().query(
                    CalendarContract.Calendars.CONTENT_URI,
                    projection,
                    CalendarContract.Calendars.VISIBLE + " = 1",
                    null,
                    CalendarContract.Calendars._ID + " ASC"
            );
            long calendarId = -1;
            if (calCursor != null && calCursor.moveToFirst()) {
                calendarId = calCursor.getLong(0); // lấy calendar đầu tiên
                calCursor.close();
            }
            if (calendarId == -1) {
                promise.reject("ERROR", "Không tìm thấy calendar hệ thống");
                return;
            }

            // Tạo event
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, notes);
            values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            values.put(CalendarContract.Events.EVENT_LOCATION, location);

            Uri uri = context.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                String eventId = uri.getLastPathSegment();
                promise.resolve(eventId);
            } else {
                promise.reject("ERROR", "Lỗi ghi event vào calendar");
            }
        } catch (Exception e) {
            promise.reject("ERROR", "Lỗi ghi event vào calendar: " + e.getMessage());
        }
    }
}