# 📚 Student Task Reminder App

A powerful task reminder app designed especially for students — built with modern Android best practices!

Manage your tasks easily, stay notified about important deadlines, and focus better with a dedicated **Focus Mode**.

This project demonstrates the use of `Fragments`, `WorkManager`, `Foreground Service`, `Content Provider`, `BroadcastReceiver`, `ViewModel`, `LiveData`, and much more!

---

## ✨ Features

- ✅ Today, Upcoming, Completed task sections  
- ➕ Add,complete, and delete reminders  
- 🔔 Background notifications using WorkManager  
- 🎯 Manual focus mode using a Foreground Service  
- 🔄 Automatic rescheduling after reboot using BroadcastReceiver
- 🔓 Content Provider to expose reminder data to other apps  

---


## 📸 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/85303d29-30df-4913-a049-816cce301ecc" alt="Today Screen" width="300"/>
  &nbsp; &nbsp;
  <img src="https://github.com/user-attachments/assets/74f3602d-7b3e-4c13-aef3-a7b875c9f178" alt="Focus Mode" width="300"/>
</p>

---

## 🎥 Demo Video

Watch a short demo on [YouTube](https://youtube.com/shorts/-m_vbixvGyw?si=JHue8gw2EzneIP3W)!





---


## 📱 App Structure

| Concept             | Usage in App                                              |
|---------------------|------------------------------------------------------------|
| **Activity**         | Main container for hosting fragments via BottomNavigationView |
| **Fragments**        | UI sections: Today, Upcoming, Completed                   |
| **ViewModel**        | Store and manage UI-related reminder data                 |
| **LiveData**         | Observe reminders and automatically update UI             |
| **Room Database**    | Store reminders locally                                   |
| **WorkManager**      | Schedule and deliver reminder notifications               |
| **Foreground Service**| Focus Mode: study session with persistent notification    |
| **BroadcastReceiver**| Detect device boot and reschedule tasks                  |
| **Content Provider** | Share reminder data externally via `content://` URIs      |
| **Lifecycle Awareness**| Services, Activities respect the Android lifecycle     |

---

## 📦 Tech Stack

- **Language**: Kotlin  
- **Architecture**: MVVM (Model-View-ViewModel)  
- **UI**: XML layouts, Fragments, Bottom Navigation  
- **Persistence**: Room Database  
- **Asynchronous tasks**: WorkManager, Foreground Service  
- **Data sharing**: Content Provider  
- **System interaction**: BroadcastReceiver (BootCompleted)  
- **Notification**: Android Notification Channels

---

## 🧠 What I Learned

- Advanced Fragment usage with navigation and shared ViewModel  
- Background scheduling with WorkManager  
- Foreground services for critical tasks (Focus Mode)  
- Android Content Provider: creating, exposing, and querying app data  
- Handling device boot events using BroadcastReceiver  
- Best practices for lifecycle-aware components  
- Deep understanding of Android Notifications  
- Building production-ready Room databases  

---

## 🚀 How to Run

1. Clone the repository:

    ```bash
    git clone https://github.com/MuntasirNahid/Student-Task-Reminder-App.git
    ```

2. Open the project in **Android Studio**.

3. Build and run on a device or emulator.

4. Grant required permissions:
   - Notifications  
   - Foreground Service  
   - Boot Completed Receiver (for rescheduling)  

---

## 📋 Future Improvements
  
- 🌙 Add dark mode  
- 📆 Google Calendar integration  
- 📤 Export/Import reminders  
- ⏱️ Improve Focus Mode with custom duration options  

---

## 💡 Contributions

Feel free to fork the repo, make changes, and submit a pull request!

