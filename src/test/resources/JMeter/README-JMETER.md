# JMeter Performance Testing: `automacao-teste-software`

This repository provides the **JMeter** script, **`_EntireFlow.jmx_`**, designed for comprehensive **load testing**, **capacity**, **stress**, **statbility**, and **response time** of the [`test-sofware-api`](https://github.com/leonidesfernando/test-software-api) web system. The script simulates a realistic, end-to-end authenticated user journey, starting with user verification, to measure the system's performance, stability, and capacity.

---

## 🚀 The Simulated User Flow

The script models a user transaction flow, ensuring deep coverage of critical API endpoints, including the **pre-test user setup/verification phase**:

| Step                    | Method | **Precise Endpoint**                                                  | Description                                                                                                                                |
|:------------------------|:-------|:----------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------|
| **1. Login**            | POST   | `/api/auth/signin`                                                    | Core authentication step.                                                                                                                  |
| **2. Find User/Verify** | POST   | `/api/auth/findUserByAdmin`                                           | Crucial initial step: Checks for the existence of the user (read from `config.data.csv`). Logic suggests the user is created if not found. |
| **3. Create User**      | POST   | `/api/auth/signup`                                                    | Crucial initial step: Add a user (read from `config.data.csv`), if not found in the find setp.                                             |
| **4. Add entry**        | POST   | `/api/entries/add`                                                    | Add a new entry.                                                                                                                           |
| **5. Search**           | POST   | `/api/entries/search`                                                 | Search entries.                                                                                                                            |
| **6. Get an enty**      | GET    | `/api/entries/get/${id}`                                              | Get an entry by ID.                                                                                                                        |
| **7. Update**           | PUT    | `/api/entries/update`                                                 | Edit an existing entry.                                                                                                                    |
| **8. Delete and entry** | DELETE | `/api/entries/remove/${id}`                                           | Remove an entry by ID.                                                                                                                     |
| **9. Logout**           | POST   | `/api/auth/signout`                                                   | Ends the user session cleanly.                                                                                                             |

> **Flow Distribution:** Every simulated user executes this flow: login, add the first entry, and there are a distribution flow to simulate a real navigation.

---

## 🛠️ Configuration and Requirements

### System Requirements

* **JDK 17+**
* **Apache JMeter 5.6+**

### Pre-Run Checks

Before running the test, configure these three critical elements:

1.  **Credentials** , **Server Location** and **Load Profile**, are present in the CSV file: `jmeter/config/config.data.csv`.

### Plugins Used

The following plugins are required to run the script and generate advanced reports:

* **Ultimate Thread Group**: For flexible load scheduling.
* **Console Status Logger**
* **3 Basic Graphics** and **5 Additional Graphics**: For comprehensive visual reporting.
* **Apdex Score Calculator**: Used for quantifying user satisfaction during background execution.

[To install the plugins manager ](https://jmeter-plugins.org/wiki/PluginsManager/)

[JMeter's best practices](https://jmeter.apache.org/usermanual/best-practices.html)

---

## 📈 Execution and Testing Types

This script can be used to perform **capacity**, **response time**, **stability**, and **stress** tests.

To execute the script and generate reports, run the batch file with the desired user count and duration:

```bash
# Syntax: runAndGenerateReports.bat -help
- Capacity: runPerformanceTest capacity 500 600       (500 users for 10 minutes)
- Response Time: runPerformanceTest response 80 300   (80 users for 5 minutes)
- Load: runPerformanceTest load 150 900               (150 users for 15 minutes)
- Stress: runPerformanceTest stress 1000 120          (1000 users for 2 minutes)
- Stability: runPerformanceTest stability 50 14400    (50 users for 4 hours)

# Syntax: runAndGenerateReports.bat <USERS_AMOUNT> <DURATION_SECONDS>

runAndGenerateReports.bat 100 900
# Example: A load test example, with 100 users executing the entire flow for 900 seconds (15 minutes).
```

[Portuguese](README-JMETER.pt_br.md)