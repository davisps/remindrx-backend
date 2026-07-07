# RemindRx (prototype)

This repo contains a starter Android app implementing the **core RemindRx requirements** from the project proposal + SRS:

- Secure user accounts (local register/login)
- Prescription management (add/edit/delete medications)
- Smart reminders (daily notification via `AlarmManager`)
- Refill tracking (remaining dose count + “refill soon” status)
- Adherence tracking (log “Taken” events + history screen)

## Open & run

1. Open this folder in **Android Studio**.
2. Let Gradle sync/download finish.
3. Run the `app` configuration on an emulator or device (Android 10+).

or

1. Download the APK file on to a compatable android device.
2. Run the file and follow the prompts.

## Notes

- This is a **local-only prototype**, set up with a local backend so wont run without a local server set up on your computer. Passwords are stored as **salted SHA-256 hashes**.
- Medication + adherence data is stored in a local Room database (`remindrx.db`).
- Reminders are **one daily time per medication** (`HH:mm`) in this first UI pass.

