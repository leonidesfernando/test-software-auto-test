# automacao-teste-software -- JMeter

The script to run the loading tests against the web
system [test-sofware-api](https://github.com/leonidesfernando/test-software-api) is  _EntireFlow.jmx_.
Before run you have to check:

* the credentials inside the file ``jmeter/confi/config.data.csv``
* the **url** and **port** where the web system will be running. These configurations are on the config
  element ``HTTP Request Defaults``
* and of course the thread properties(users amount, ramp-up period, loop count) inside the ``Thread Group``

## Requirements

- JDK 17+
- Apache JMeter 5.6+

This script was created using the following JMeter's plugins:

* Ultimate Thread Group
* Console Status Logger
* 3 Basic Graphics
* 5 Additional Graphics
* Apdex Score Calculator( when executed in background by the script: ```runAndGenerateReports.bat```)

[To install the plugins manager ](https://jmeter-plugins.org/wiki/PluginsManager/)

[JMeter's best practices](https://jmeter.apache.org/usermanual/best-practices.html)

## Kinds of tests

You can use this script to apply *capacity*, *response time*, *stability* and *stress*
To do that you just need to send the amount of users and for how long
time it must be run.
For example: _100 users during 900 seconds_

```
runAndGenerateReports.bat 100 900
```

---
[Portuguese](README-JMETER.pt_br.md)
