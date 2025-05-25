# ⚙️ Инструкции по запуску проекта
- Java 17+ (Azul Zulu) и Postgres должны быть установлены
- В Postgres создайте базу данных `budget_planner_db` и пользователя `budget_planner_db_owner` с паролем `bppass` или переопределите параметры подключения к БД в файле `src/main/resources/application.properties`
- В директории с проектом исполнить следующие команды:
    ```bash
    ./gradlew build
    ./gradlew bootRun
    ```
---
# 📄 Документация проекта

## 📌 Содержание
- [Функциональные возможности](#-функциональные-возможности)
- [Сценарии взаимодействия пользователей с приложением](#-сценарии-взаимодействия-пользователей-с-приложением)
  - [Регистрация нового пользователя](#1-регистрация-нового-пользователя)
  - [Вход в систему](#2-вход-в-систему)
  - [Добавление транзакции](#3-добавление-транзакции)
  - [Просмотр списка транзакций](#4-просмотр-списка-транзакций)
  - [Просмотр баланса](#5-просмотр-баланса)
  - [Получение списка категорий](#6-получение-списка-категорий)
- [Структура базы данных](#-структура-базы-данных)

---

## 🔎 Функциональные возможности

Приложение представляет собой веб-сервис для учёта личных финансов, включающий:

- **Регистрацию и аутентификацию пользователей**  
  Пользователь может создать учётную запись и входить в систему с помощью логина и пароля.

- **Добавление транзакций**  
  Пользователь может добавить доход или расход с указанием суммы, категории и описания.

- **Просмотр всех транзакций**  
  Возможность получить список всех транзакций, совершённых пользователем.

- **Просмотр текущего баланса**  
  Отображение текущего баланса как сумма всех доходов минус сумма всех расходов.

- **Получение списка категорий**  
  Пользователь получает список предопределённых категорий (например: "Продукты", "Здоровье", "Зарплата").

---

## 🧪 Сценарии взаимодействия пользователей с приложением

### 1. Регистрация нового пользователя
Пользователь вводит E-mail, Имя и Пароль и в ответ получает JWT токен при успешной регистрации

**Пример запроса**

`POST /auth/sign-up`
```JSON
{
  "email": "user@mail.com",
  "name": "User",
  "password": "password123"
}
```
**Пример ответа**
```JSON
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQG1haWwuY29tIiwiaWF0IjoxNzQ4MTY4NDQ3LCJleHAiOjE3NDgxNzIwNDd9.ipDNbktE6BQCiI66FkbyfsqheLt64Z6W3e4HlBjKQNwt8zFVX2iJpu4Wntg5QwOkMeeJzdLXF69lvoGJyFTg9w"
}
```

### 2. Вход в систему
Пользователь вводит E-mail и Пароль и в ответ получает JWT токен при успешной аутентификации

**Пример запроса**

`POST /auth/login`
```JSON
{
  "email": "user@mail.com",
  "password": "password123"
}
```
**Пример ответа**
```JSON
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQG1haWwuY29tIiwiaWF0IjoxNzQ4MTY4NjQ2LCJleHAiOjE3NDgxNzIyNDZ9.pzh4frweaP8RxsFWf4wcJ27Sevs0s-0zWEjolJpAdC8OaZ3slqdH70Zs93Y5JWC7ECzItIZPd3AkmpqYTJm7lg"
}
```


---
> **ℹ️ Все дальнейшие запросы содержат заголовок `Authorization` со значением `Bearer <JWT>`, где `<JWT>` это токен, полученный при запросе на регистрацию или аутентификацию**
---


### 3. Добавление транзакции
Пользователь вводит категорию, сумму и выбирает, является ли транзакция доходом или расходом, а в ответ получает обновленный баланс

**Пример запроса**

`POST /api/transaction/add`
```JSON
{
  "category": "Salary",
  "value": 5000,
  "isIncome": true
}
```
**Пример ответа**
```JSON
{
  "newBalance": 5000.0
}
```

### 4. Просмотр списка транзакций
Данный метод API возвращает список всех транзакций пользователя

**Пример запроса**

`GET /api/transaction/list`

**Пример ответа**
```JSON
[
  {
    "category": "Clothing",
    "value": -300.0,
    "time": "2025-05-25T14:12:29.895376",
    "income": false
  },
  {
    "category": "Food",
    "value": -300.0,
    "time": "2025-05-25T14:12:25.41534",
    "income": false
  },
  {
    "category": "Salary",
    "value": 5000.0,
    "time": "2025-05-25T14:08:02.746181",
    "income": true
  }
]
```

### 5. Просмотр баланса
Данный метод возвращает баланс пользователя

**Пример запроса**

`GET /api/info/balance`

**Пример ответа**
```JSON
{
  "balance": 4400.0
}
```

### 6. Получение списка категорий
Данный метод возвращает список всех доступных категорий

**Пример запроса**

`GET /api/info/categories`

**Пример ответа**
```JSON
[
  {
    "id": 1,
    "name": "Salary",
    "icon": "http://localhost:8080/icons/sack-dollar.svg",
    "income": true
  },
  {
    "id": 2,
    "name": "Other",
    "icon": "http://localhost:8080/icons/question.svg",
    "income": true
  },
  {
    "id": 3,
    "name": "Adjust balance",
    "icon": "http://localhost:8080/icons/pen.svg",
    "income": true
  },
  {
    "id": 4,
    "name": "Other",
    "icon": "http://localhost:8080/icons/question.svg",
    "income": false
  },
  {
    "id": 5,
    "name": "Adjust balance",
    "icon": "http://localhost:8080/icons/pen.svg",
    "income": false
  },
  {
    "id": 6,
    "name": "Home",
    "icon": "http://localhost:8080/icons/house.svg",
    "income": false
  },
  {
    "id": 7,
    "name": "Bills",
    "icon": "http://localhost:8080/icons/file-invoice-dollar.svg",
    "income": false
  },
  {
    "id": 8,
    "name": "Health",
    "icon": "http://localhost:8080/icons/heart-pulse.svg",
    "income": false
  },
  {
    "id": 9,
    "name": "Entertainments",
    "icon": "http://localhost:8080/icons/gamepad-modern.svg",
    "income": false
  },
  {
    "id": 10,
    "name": "Gift",
    "icon": "http://localhost:8080/icons/gift.svg",
    "income": false
  },
  {
    "id": 11,
    "name": "Car",
    "icon": "http://localhost:8080/icons/car-side.svg",
    "income": false
  },
  {
    "id": 12,
    "name": "Education",
    "icon": "http://localhost:8080/icons/books.svg",
    "income": false
  },
  {
    "id": 13,
    "name": "Beauty",
    "icon": "http://localhost:8080/icons/sparkles.svg",
    "income": false
  },
  {
    "id": 14,
    "name": "Sport",
    "icon": "http://localhost:8080/icons/dumbbell.svg",
    "income": false
  },
  {
    "id": 15,
    "name": "Food",
    "icon": "http://localhost:8080/icons/apple-whole.svg",
    "income": false
  },
  {
    "id": 16,
    "name": "Clothing",
    "icon": "http://localhost:8080/icons/shirt.svg",
    "income": false
  }
]
```
---
## 🗄️ Структура базы данных
Все SQL скрипты для создания таблиц и добавление данных в БД находятся в директории `src/main/resources/db/migration`

**Диаграмма базы данных:**


---

    