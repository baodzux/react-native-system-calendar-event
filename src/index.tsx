import { NativeModules } from "react-native";

/**
 * Native module bridge
 */
const { SystemCalendarEvent } = NativeModules;

export type CalendarEventParams = {
  title: string;
  startDate: string; // ISO string, ví dụ '2025-09-25T09:00:00.000Z'
  endDate: string; // ISO string
  location?: string;
  notes?: string;
};

/**
 * Thêm sự kiện vào calendar hệ thống.
 * @returns Promise<string> - eventId nếu thành công
 */
export function addEvent(params: CalendarEventParams): Promise<string> {
  // Đảm bảo có các tham số cần thiết
  if (!params.title || !params.startDate || !params.endDate) {
    return Promise.reject(
      new Error("Missing required params: title, startDate, endDate")
    );
  }
  // Gọi native module
  return SystemCalendarEvent.addEvent(
    params.title,
    params.startDate,
    params.endDate,
    params.location ?? "",
    params.notes ?? ""
  );
}
