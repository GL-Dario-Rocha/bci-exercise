
# User Registration and Login (BCI Exercise)




## Run Locally

Clone the project

```bash
  git clone https://github.com/GL-Dario-Rocha/bci-exercise.git
```

Go to the root directory of the project and run

```bash
  gradlew bootRun
```

by default it runs on **localhost:8080**



## Run Tests
Go to the root directory of the project and run
```
    gradlew test
```

## API Reference

#### User Sign-up

```http
  POST /users/sign-up
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | Your API key |
| `email` | `string` | **Required**. Your API key |
| `password` | `string` | **Required**. Your API key |
| `phones` | `List of Phone` | Your API key |

Phone

| Parameter     | Type      | Description               |
| :--------     | :-------  | :-------------------------|
| `number`      | `string`  |  Your API key             |
| `citycode`    | `string`  |  Your API key             |
| `contrycode`  | `string`  |  Your API key             |

Request:
```json
curl --location --request POST 'localhost:8080/users/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Juan Perez",
    "email": "juan.perez@gmail.cam",
    "password": "a2asfGfdfdf4",
    "phones": [
        {
        "number": 32165487,
        "citycode": 54,
        "countrycode": "ARG"
        }
    ]
}'
```
Response 201 CREATED:
```json
{
    "id": "8f7d7366-55d6-4134-8a0f-7f0b77b649e2",
    "created": "Jul 16, 2022 7:27:40 PM",
    "lastLogin": "Jul 16, 2022 7:27:40 PM",
    "token": "eyJhbGciO...",
    "name": "Juan Perez",
    "email": "juan.perez@gmail.cam",
    "password": "zh9Rl2inivXolinjpy9wBwoATe2IRNXOg2l16HG8gwg=",
    "phones": [
        {
            "number": 32165487,
            "citycode": 54,
            "countrycode": "ARG"
        }
    ],
    "active": true
}
```

#### Get item

```http
  GET /users/login
```
Must add Authorization values(token obtained in sign-up endpoint) to header
Request:

```
curl --location --request GET 'localhost:8080/users/login' \
--header 'Authorization: Bearer eyJhbGciOi...'
```

Response 200 OK:
```json
{
    "id": "0890b4a5-1235-49b2-b47d-63da7bc5071d",
    "created": "Jul 16, 2022 7:33:28 PM",
    "lastLogin": "Jul 16, 2022 7:33:28 PM",
    "token": "eyJhbGciOi...",
    "name": "Juan Perez",
    "email": "juan.perez@gmail.cam",
    "password": "+nhFeBYAYe2BYoXXQpf2ewHWc+QCIb1EZHwni/aB5RQ=",
    "phones": [
        {
            "number": 32165487,
            "citycode": 54,
            "countrycode": "ARG"
        }
    ],
    "active": true
}
```




## Error Codes

| Code | Meaning     |
| :-------- | :------------------------- |
| `1` | Related to email format. |
| `2` | Related to password format. |
| `3` | When a resource already exist. |
| `4` | Related to encryption. |
| `5` | Related to generation or obtaining of token.  |
| `6` | Related to the non-existence of a resource. |
| `7` | Related to decryption. |
| `999` | Generic error. |


## Documentation

The diagrams are in the diagrams folder
