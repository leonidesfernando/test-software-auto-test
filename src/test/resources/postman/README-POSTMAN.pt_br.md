# automacao-teste-software -- Postman

Aqui nós temos um ambiente e uma coleção do _Postman_ para verificar
todos os CRUD endpoints do sistema web [teste-sofware](https://github.com/leonidesfernando/teste-software).
A coleção é `Teste Software.postman_collection.json` onde nós mapeamos os endpoints e
o ambiente é `TEST_SOFTWARE_DEV.postman_environment.json` onde nós adicionameos e criamos
dinamicamente algumas variáveis para serem usadas na coleção.

## Como usar

Você deve importar a coleção e o ambiente em seu _Postman_ então você deve clonar o
[teste-sofware](https://github.com/leonidesfernando/teste-software) e executá-lo para
ser capaz de executar os scripts do _Postman_.

## O que você vai encontrar?

Nesta coleção todos os endpoints possuem *tests* e alguns possuem
*pre-request script* onde nós usamos para criar alguma variáveis
dinamicamente, como _preço_, _data_, _descrição_ e assim por diante.

## Requisitos

1. Ter [teste-sofware](https://github.com/leonidesfernando/teste-software) rodando
2. Executar os testes nesta ordem, porque eles possuem dependencias
   A ordem:

    - Login
    - Create a new entry
    - Search entry by description
    - List entries from first page
    - Prepare to create a new entry
    - Prepare to edit
    - Remove a specific entry

[English](README-POSTMAN.md)