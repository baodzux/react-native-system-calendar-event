Pod::Spec.new do |s|
    s.name         = "RNSystemCalendarEvent"
    s.version      = "0.1.1"
    s.summary      = "React Native Calendar Event Module"
    s.homepage     = "https://github.com/baodzux/react-native-system-calendar-event"
    s.license      = "MIT"
    s.author       = { "baodzux" => "your@email.com" }
    s.source       = { :git => "https://github.com/baodzux/react-native-system-calendar-event.git", :tag => s.version }
    s.platform     = :ios, "10.0"
    s.source_files  = "ios/*.{h,m,swift}"
    s.requires_arc = true
    s.dependency 'React-Core'
  end