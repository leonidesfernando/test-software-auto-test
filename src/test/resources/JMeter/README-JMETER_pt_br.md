# JMeter Performance Testing: `automacao-teste-software`
Este repositório fornece o script **JMeter**, **`_EntireFlow.jmx_`**, desenvolvido para testes de  **carga**, **capacidade**, **stress**, **estabilidade**, and **tempo de resposta** abrangentes no sistema web [`test-sofware-api`](https://github.com/leonidesfernando/test-software-api). O script simula uma jornada de usuário autenticado realista, começando com a verificação do usuário, para medir o desempenho, estabilidade e capacidade do sistema.

---

## 🚀 A simulação do Fluxo de Usuário
O script modela um fluxo de transação de usuário, garantindo cobertura profunda dos endpoints de API críticos, incluindo a **fase de verificação/configuração de usuário pré-teste**:

| Passo                         | Método | **Endpoint**                                                         | Descrição                                                                                                               |
|:------------------------------|:-------|:---------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------|
| **1. Login**                  | POST   | `/api/auth/signin`                                                   | Etapa de autenticação.                                                                                                  |
| **2. Busca Usuário/Verifica** | POST   | `/api/auth/findUserByAdmin`                                          | Crucial - passo iniial: Verifica a existência do usuário (lê do arquivo `config.data.csv`).                             |
| **3. Cria usuário**           | POST   | `/api/auth/signup`                                                   | Crucial - passo inicial: Adiciona um usuário (lê do arquivo `config.data.csv`), caso não encontrado no passo anterior . |
| **4. Adiciona lançamento**    | POST   | `/api/entries/add`                                                   | Adiciona um lançamento.                                                                                                 |
| **5. Busca**                  | POST   | `/api/entries/search`                                                | Busca lançamentos.                                                                                                      |
| **6. Get um lançamento**      | GET    | `/api/entries/get/${id}`                                             | Obtém um lançamento pelo ID.                                                                                            |
| **7. Update**                 | PUT    | `/api/entries/update`                                                | Edita lançamento existente.                                                                                             |
| **8. Deleta um lançamento**   | DELETE | `/api/entries/remove/${id}`                                          | Remove um lançamento pelo ID.                                                                                           |
| **9. Logout**                 | POST   | `/api/auth/signout`                                                  | Finaliza a sessão do usuário.                                                                                           |

> **Distribuição de Fluxo:** Cada usuário simulado executa este fluxo: login, adiciona o primeiro lançamento, e há um fluxo de distribuição para simular uma navegação real.

---

## 🛠️ Configuração e Requisitos

### Requisitos de Sistema

* **JDK 17+**
* **Apache JMeter 5.6+**

### Verificações Pré-Execução
Antes de executar o teste, configure estes três elementos críticos:

-  **Localização do Servidor:** Defina o **URL** e a **porta** corretos para a aplicação alvo dentro do arquivo CSV: `jmeter/config/config.data.csv`.

### Plugins Utilizados

Os seguintes plugins são necessários para executar o script e gerar relatórios avançados:

* **Ultimate Thread Group**: Para agendamento de carga flexível.
* **Console Status Logger**
* **3 Basic Graphics** e **5 Additional Graphics**: Para relatórios visuais abrangentes.
* **Apex Score Calculator**: Usado para quantificar a satisfação do usuário durante a execução em segundo plano.

[Para instalar o gerenciador de plugins](https://jmeter-plugins.org/wiki/PluginsManager/)

[Boas práticas com JMeter](https://jmeter.apache.org/usermanual/best-practices.html)

---

## 📈 Execução e Tipos de Teste

Este script pode ser usado para realizar testes de **capacidade**, **tempo de resposta**, **estabilidade** e **estresse**.

Para executar o script e gerar relatórios, execute o arquivo *batch* com a contagem de usuários e a duração desejadas:

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

[Inglês](README-JMETER.md)
