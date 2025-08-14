# SmartMeterApp

A local end-to-end prototype for a smart electricity metering service.  
It consists of:

- **Flask backend** (`server.py`) that:
  - Stores a simulated database of smart meters.
  - Accepts prepaid “token” submissions and credits meters.
  - Decrements each meter’s balance by 1 unit every 3 seconds.
- **Simulated meter box** (`simulated_meter.py`) — a small Tkinter GUI that polls the server and displays live meter status.
- **Android app** (Android Studio project) with four tabs:
  1. **Home** – shows Meter ID, current balance and last token; live-updates every 3 s.  
  2. **Top-Up** – enter a Meter ID and token, POST to the server, and display “+X units.”  
  3. **History** – lists all applied tokens and amounts.  
  4. **Appliances** – a simulated list of home appliances and their (static/demo) consumption.

---

## Prerequisites

- **Python 3.7+** on your PATH  
- **Java JDK 11+**  
- **Android Studio** with Android SDK (min API 21+)  
- A **Wi-Fi network** shared by your PC and Android device/emulator

---

## Installation & Setup

1. **Clone or unzip** this repository into `SmartMeterApp/`.

2. **Backend & Simulated Meter**  
   ```bash
   cd SmartMeterApp
   python -m venv venv
   # Windows
   venv\Scripts\activate  
   # macOS/Linux
   # source venv/bin/activate

   pip install flask requests tk


* **Windows**: double-click `run_all.bat`
* **macOS/Linux**:

  chmod +x run_all.sh
  ./run_all.sh
 

This opens two terminals:

* **Flask server** on `http://0.0.0.0:5000`
* **Simulated meter box** polling `/api/meter/123456`

3. **Android App**

   1. Open **Android Studio → Open** → select the `SmartMeterApp/app` folder.
   2. In `TopUpFragment.java` & `HomeFragment.java`, replace
      private static final String SERVER_URL = "http://192.168.1.10:5000";
      with your PC’s actual LAN IP.
   3. **Allow cleartext HTTP** (Android 9+):

      * Add `network_security_config.xml` under `res/xml/` (as shown in code).
      * In `AndroidManifest.xml`’s `<application>` tag:

        
        android:usesCleartextTraffic="true"
        android:networkSecurityConfig="@xml/network_security_config"
        
   4. Build & run on a device/emulator (same Wi-Fi).

---

## Usage

1. **Top-Up** tab: enter Meter ID (`123456`) and a valid token (e.g. `TOKEN-001` → +50 units).
2. **Home** tab & **Meter Box** window:

   * Balance jumps up, then decrements by 1 unit every 3 s.
   * Last-token and history update in real-time.
3. **History** tab: see all token events.
4. **Appliances** tab: view a sample list of appliances with their consumption.

---

## Project Structure


SmartMeterApp/
├─ server.py
├─ simulated_meter.py
├─ run_all.bat    (Windows)
├─ run_all.sh     (macOS/Linux)
└─ app/
   ├─ src/main/
   │  ├─ java/com/example/smartmeterapp/
   │  │  ├─ MainActivity.java
   │  │  ├─ HomeFragment.java
   │  │  ├─ TopUpFragment.java
   │  │  ├─ HistoryFragment.java
   │  │  ├─ AppliancesFragment.java
   │  │  └─ MeterData.java
   │  └─ res/
   │     ├─ layout/
   │     │  ├─ activity_main.xml
   │     │  ├─ fragment_home.xml
   │     │  ├─ fragment_topup.xml
   │     │  ├─ fragment_history.xml
   │     │  └─ fragment_appliances.xml
   │     ├─ menu/bottom_nav_menu.xml
   │     ├─ navigation/mobile_navigation.xml
   │     └─ xml/network_security_config.xml


