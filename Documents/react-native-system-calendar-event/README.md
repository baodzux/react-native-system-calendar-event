# react-native-system-calendar-event

Thư viện React Native dùng để thêm sự kiện vào lịch hệ thống trên Android/iOS.

## Cài đặt

```bash
npm install react-native-system-calendar-event
cd ios && pod install
```

## Sử dụng

```javascript
import { addEvent } from 'react-native-system-calendar-event';

addEvent({
  title: 'Họp nhóm React Native',
  startDate: '2025-09-25T09:00:00.000Z',
  endDate: '2025-09-25T10:00:00.000Z',
  location: 'Online',
  notes: 'Trao đổi về dự án mới'
}).then(eventId => {
  alert('Thêm thành công!');
});
```