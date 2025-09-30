# automacao-teste-software -- JMeter

O script para rodar os testes de carga contra o sistema
web [test-sofware-api](https://github.com/leonidesfernando/test-software-api)
é o _EntireFlow.jmx_.

Antes de rodar, você deve verificar:

* as credenciais no arquivo ``jmeter/confi/config.data.csv``
* a **url** e **porta** onde o sistema web estará rodando. Essas configurções estão no elemento de
  configuração ``HTTP Request Defaults``
* e claro as configurações de execução(users amount, ramp-up period, loop count) presentes no ``Thread Group``

## Requisitos

- JDK 17+
- Apache JMeter 5.6+

Este script foi criado usando os seguintes plugins do JMeter:

* Ultimate Thread Group
* Console Status Logger
* 3 Basic Graphics
* 5 Additional Graphics
* Apdex Score Calculator( when executed in background by the script: ```runAndGenerateReports.bat```)

[Para instalar o gerenciador de plugins](https://jmeter-plugins.org/wiki/PluginsManager/)

[Boas práticas com JMeter](https://jmeter.apache.org/usermanual/best-practices.html)

## Tipos de testes

Você pode usar este script para aplicar estes testes
*capacidade*, *tempo de resposta*, *estabilidade* e *stress*.
Para fazer isso você precisa apenas enviar uma quantidade de usuários e por quanto tempo o teste deve rodar.
Por exemplo: _100 usuários durante 900 segundos_

```
runAndGenerateReports.bat 100 900
```

---
[Inglês](README-JMETER.md)

