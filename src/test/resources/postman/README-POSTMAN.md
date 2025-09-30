# automacao-teste-software -- Postman

Over here we have a Postman environment and collection to check
all CRUD endpoints from the web system [teste-sofware](https://github.com/leonidesfernando/teste-software).
The collection is `Teste Software.postman_collection.json` where we have mapped the
endpoints, and the environment is `TEST_SOFTWARE_DEV.postman_environment.json`
where we have added and create dynamically some variables to be used
in the collection.

## How to use

You must import the collection and environment in you _Postman_, so
you must clone the [teste-sofware](https://github.com/leonidesfernando/teste-software)
and run it to be able to execute Postman's scripts.

## What do you will found?

In this collection all endpoints have *tests* and someone have
*pre-request script* where we made use and create some variables
dynamically, such as _price_, _date_, _description_ and os forth.

## Requirements

1. To have [teste-sofware](https://github.com/leonidesfernando/teste-software) running
2. Execute the tests in this order, because they have dependencies.
   The order:

    - Login
    - Create a new entry
    - Search entry by description
    - List entries from first page
    - Prepare to create a new entry
    - Prepare to edit
    - Remove a specific entry

[Portuguese](README-POSTMAN.pt_br.md)