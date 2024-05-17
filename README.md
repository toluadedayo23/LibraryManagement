# LibraryManagement

A Library Management System API  built using java and Spring Boot. 

Api endpoints input, response and other necessary details are available on the swagger documentation.

The application uses Json Web Token(JWT) for authentication. Access to endpoints for operations
for Patrons, Books, borrowing and returning of books are role-based and could only be performed 
by users with the role **ROLE_LIBRARIAN**.

The default database configuration for the application is for the MySQL database, you can edit the 
application.yml to input your desired RDMS configuration.
Create a **librarymanagement** database in your database if you are sticking with my MySQL configuration 
before running the application and make sure your local database is running on the port specified in the configuration.

Run the spring boot application using your desired IDE or through your device CLI, hibernate will create the schemas if 
they don't exist already or update if necessary.

Run the query below after the application has successfully started and all the schemas are created.
The query creates a User with the **ROLE_LIBRARIAN** and the password inserted 
is a plain password called "password" but encrypted with the Bcrypt password encoder, this would have been done 
in the application if i created a method/service that creates creates Librarians but that's not the focus of the project.
But this was done to demonstrate the JWT authentication and Role-based access control.

INSERT INTO users (username, email, password, enabled, role, created_at, last_modified_at)
VALUES ('librarian1@gmail.com', 'librarian1@gmail.com', '$2y$10$th/7DKBub2ffU81j7mXE/uOsf5HS1tJQ4gqXhWO8bIxMlAd.cDiai', TRUE, 'ROLE_LIBRARIAN', NOW(), NOW());

if, you ran the query above and was successful, you can login into the application using your preferred http client with the credentials below;

{
  "username": "librarian1@gmail.com",
  "password": "password"
}
