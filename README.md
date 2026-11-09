# Employee one-to-one API

The application uses PostgreSQL configured in `src/main/resources/application.properties` and listens on port 8082. An employee can have one optional profile. Deleting a profile keeps the employee; deleting an employee also deletes its profile.

| Method | Path | Behavior |
| --- | --- | --- |
| POST | /api/v1/employees | Create employee with optional profile (201) |
| GET | /api/v1/employees | List employees (200) |
| GET | /api/v1/employees/{id} | Get employee (200, or 404) |
| PUT | /api/v1/employees/{id}/profile | Create or replace profile fields (200, or 404) |
| DELETE | /api/v1/employees/{id}/profile | Remove profile (204, or 404 for missing employee) |
| DELETE | /api/v1/employees/{id} | Delete employee and profile (204, or 404) |

POST example:

```json
{
  "name": "Alex",
  "email": "alex@example.com",
  "department": "Engineering",
  "employeeProfileDto": {
    "phoneNumber": "1234567890",
    "address": "Hyderabad",
    "dateOfBirth": "1995-04-12",
    "emergencyContact": "9876543210"
  }
}
```

PUT accepts just the nested profile object. The old request spellings `phoneNumebr` and `dataOfBirth` remain accepted. Responses include employee IDs and profile details under `employeeProfileResponse`. Duplicate email creation returns 409.

Run `mvn test` for database-backed lifecycle tests using an isolated H2 database; PostgreSQL is not required for tests. Run `mvn spring-boot:run` to start the application against configured PostgreSQL.

## Test with Postman

1. Start PostgreSQL and ensure the configured `emp` database exists. Check the connection settings in `src/main/resources/application.properties`.
2. Set the `DB_PASSWORD` environment variable to your PostgreSQL password, then start the application with `mvn spring-boot:run`. For PowerShell, use `$env:DB_PASSWORD = 'your-local-password'` in the same terminal before starting.
3. Use `http://localhost:8082/api/v1/employees` as the base URL. No authentication is configured.
4. For POST and PUT requests, select **Body → raw → JSON** and use `Content-Type: application/json`. GET and DELETE requests need no body.

You can also import [the Postman collection](postman/OneToOne.postman_collection.json) and run its requests in order. It generates a unique email, stores the created employee ID, checks responses, and deletes its test employee at the end. The collection defaults to `http://localhost:8082`; change its `baseUrl` variable if necessary.

### 1. Create an employee with a profile

**POST** `http://localhost:8082/api/v1/employees`

```json
{
  "name": "Durga",
  "email": "durga@example.com",
  "department": "IT",
  "employeeProfileDto": {
    "phoneNumber": "9876543210",
    "address": "Hyderabad",
    "dateOfBirth": "1995-04-12",
    "emergencyContact": "9123456780"
  }
}
```

Expected: **201 Created**. The response includes the employee `id` and nested `employeeProfileResponse` with its own ID. Use the returned employee ID instead of `1` in the following URLs.

Send the same request again before deleting the employee: expect **409 Conflict** because the email already exists. Use a different email when creating another employee.

### 2. List employees

**GET** `http://localhost:8082/api/v1/employees`

Expected: **200 OK** with a JSON array containing the employee created above. An empty database returns `[]`.

### 3. Get an employee by ID

**GET** `http://localhost:8082/api/v1/employees/1`

Expected: **200 OK** with the employee and its profile. An ID that does not exist returns **404 Not Found**.

### 4. Update the profile

**PUT** `http://localhost:8082/api/v1/employees/1/profile`

```json
{
  "phoneNumber": "9988776655",
  "address": "Bengaluru",
  "dateOfBirth": "1995-04-12",
  "emergencyContact": "9000012345"
}
```

Expected: **200 OK** with the updated profile under `employeeProfileResponse`. Repeat the GET request to confirm the saved values. Send all profile fields: omitted or null fields clear their stored values. This request also creates a profile if the employee currently has none.

### 5. Delete only the profile

**DELETE** `http://localhost:8082/api/v1/employees/1/profile`

Expected: **204 No Content**, with an empty response body. Repeat the employee GET request: expect **200 OK** with `employeeProfileResponse` set to `null`.

Deleting the profile again returns **204 No Content** while the employee still exists. Repeat the PUT request from step 4 to recreate the profile before testing employee deletion.

### 6. Delete the employee and its profile

**DELETE** `http://localhost:8082/api/v1/employees/1`

Expected: **204 No Content**, with an empty response body. The associated profile is also deleted. Repeat the employee GET request: expect **404 Not Found**.

After deletion, PUT or DELETE requests for that same employee ID also return **404 Not Found**.

### 7. Create an employee without a profile

**POST** `http://localhost:8082/api/v1/employees`

```json
{
  "name": "Priya",
  "email": "priya@example.com",
  "department": "HR"
}
```

Expected: **201 Created** with `employeeProfileResponse` set to `null`. Use this employee's returned ID with the PUT request from step 4 to add a profile, then DELETE the employee when finished testing.
