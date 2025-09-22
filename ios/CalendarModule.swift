import EventKit
import React

@objc(SystemCalendarEvent)
class SystemCalendarEvent: NSObject {
  @objc(addEvent:withStartDate:withEndDate:withLocation:withNotes:withResolver:withRejecter:)
  func addEvent(
    title: String,
    startDate: String,
    endDate: String,
    location: String,
    notes: String,
    resolve: @escaping RCTPromiseResolveBlock,
    reject: @escaping RCTPromiseRejectBlock
  ) {
    let eventStore = EKEventStore()
    eventStore.requestAccess(to: .event) { (granted, error) in
      if let err = error {
        reject("ERROR", "Không thể truy cập calendar: \(err.localizedDescription)", err)
        return
      }
      if !granted {
        reject("ERROR", "Không có quyền truy cập calendar", nil)
        return
      }

      // Parse startDate, endDate
      let formatter = ISO8601DateFormatter()
      guard let start = formatter.date(from: startDate), let end = formatter.date(from: endDate) else {
        reject("ERROR", "Định dạng ngày không hợp lệ", nil)
        return
      }

      let event = EKEvent(eventStore: eventStore)
      event.title = title
      event.startDate = start
      event.endDate = end
      event.location = location
      event.notes = notes
      event.calendar = eventStore.defaultCalendarForNewEvents

      do {
        try eventStore.save(event, span: .thisEvent)
        resolve(event.eventIdentifier ?? "")
      } catch let err {
        reject("ERROR", "Lỗi ghi event vào calendar: \(err.localizedDescription)", err)
      }
    }
  }
}