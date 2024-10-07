# Project TDoH

TDoH is a personal project i did to manage basically my life, to help me (and others in the future) to be more organize and stop procrastinanting

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Contact](#contact)

## Installation

Instructions on how to install and set up the project.

```bash
git clone https://github.com/Miharu669/TDoh-PersonalProject-F5-Back.git
```
- server localhost:8080
- db_tdoh 1234 :3306
- add to .env a valid API_JWT_KEY

## Usage

```bash
mvn spring-boot:run to run app
```

## UML Diagram

![uml](https://i.gyazo.com/e82c92559936ff06d6d50c96dd6e1f7a.png)

## Endpoints
api-endpoint= /api/v1

### Login
- <p>POST localhost:8080/api/v1/auth/register</p>
- <p>POST localhost:8080/api/v1/auth/authenticate</p>
- <p>POST localhost:8080/api/v1/auth/refresh-token</p>

### Notes
- <p>GET localhost:8080/api/v1/notes</p>
- <p>POST localhost:8080/api/v1/notes</p>
- <p>PUT localhost:8080/api/v1/notes/{id}</p>
- <p>DELETE localhost:8080/api/api/v1/notes/{id}</p>

### Events
- <p>GET localhost:8080/api/v1/events</p>
- <p>GET localhost:8080/api/v1/events/{id}</p>(get event by Id)
- <p>GET localhost:8080/api/v1/events/date/{date}</p>(get events by date)
- <p>POST localhost:8080/api/v1/events</p>
- <p>DELETE localhost:8080/api/api/v1/events/{id}</p>
- <p>DELETE localhost:8080/api/api/v1/events/date/{date}</p>(delente all events for one date)

### Tasks
- <p>GET localhost:8080/api/v1/tasks</p>
- <p>GET localhost:8080/api/v1/tasks/{id}</p>(get task by Id)
- <p>POST localhost:8080/api/v1/tasks</p>
- <p>PUT localhost:8080/api/v1/tasks/{id}</p>
- <p>PATCH localhost:8080/api/v1/tasks/{id}/status</p>(mark a task as done)
- <p>DELETE localhost:8080/api/api/v1/tasks/{id}</p>

### Tasks
- <p>GET localhost:8080/api/v1/subtasks</p>
- <p>GET localhost:8080/api/v1/subtask/tasks/{taskId}</p>(get subtask by taskId)
- <p>GET localhost:8080/api/v1/subtasks/{id}</p>(get subtask by id)
- <p>POST localhost:8080/api/v1/subtasks</p>(dont forget to add taskId)
- <p>PUT localhost:8080/api/v1/subtastasks/{id}</p>
- <p>PATCH localhost:8080/api/v1/subtasks/{id}/status</p>(mark a subtask as done)
- <p>DELETE localhost:8080/api/api/v1/subtasks/{id}</p>

## FrontEnd
- Front Repo: [TDoH-FrontEnd](https://github.com/Miharu669/TDoh-PersonalProject-F5-Front)

## Contact

- Name: Verónica Doel
- Email: veronicadoelfuentes@gmail.com
- GitHub: [Miharu669](https://github.com/Miharu669)