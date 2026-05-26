
# Music Explorer App

An Android application built as the final coursework for the Mobile Application Development module at the University of York Europe Campus (CITY College).

## Overview

Music Explorer is anative Android application that allows users to discover new music, explore global top charts, and manage a personal collection of favorite artists. It seamlessly integrates the **Last.fm API** for music metadata, all wrapped in a modern **Material Design 3** interface.

## Features

* **Global Top Charts & Search:** Browse worldwide trending artists or search for specific artists by name.
* **Rich Artist Profiles:** View comprehensive artist details including a biography summary, playcounts, top tracks, and similar artists.
* **Offline Favorites Collection:** Save your favorite artists to a local database and manage your personal collection, available even offline.
* **Native Sharing:** Share your favorite artists with friends using Android's native share sheet.
* **Network State Handling:** Elegant UI feedback for loading states, successful data fetches, and network errors (with offline detection).

## Tech Stack & Architecture

* **Language:** Kotlin
* **UI toolkit:** Standard Android Views wit h**Material Design 3** components.
* **Networking:** `Retrofit2` for REST API consumption.
* **Local Storage:** `Room Database` for persistent, offlinw-capable storage of favorite artists.
* **Concurrency:** `Kotlin Coroutines` (`lifecycleScope`, `Dispatchers.IO`) for safe background threading.
* **Architecture:** Activity-based architecture with RecyclerViews and custom Adapters.

## Setup & Installation
1. Clone this repository to your local machine.
2. Open the project in **Android Studio**.
3. Allow Gradle to sync and download all dependencies.
4. Build and run the application on an Android Emulator or a physical device (Minimum SDK: 24, Target SDK: 36).

## Authors
**[Grigorios Karakitsos]** - **[Michai-Viorel Popescu]**
