# screen-awake

keeps the screen on. that's it.

useful when you need to watch something, read docs, or just don't want the screen locking during a long task and you're too lazy to change screen timeout settings every time.

## features

- set a timer (15 min / 30 min / 1h / custom)
- screen stays on until timer ends or you stop it
- small persistent notification while running
- no permissions needed beyond `WAKE_LOCK`

## screenshots

(no screenshots yet, apk is in releases)

## build

open in android studio, run on device/emulator. min sdk 26.

## why not just change screen timeout in settings

because then you forget to change it back and your phone drains at night. this is cleaner.

## stack

kotlin · android sdk · viewmodel · foreground service
