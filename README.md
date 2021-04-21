#Introduction to Spring Reactor

This repo contains code for employee query service. Employee's salary details are stored in a `H2` database 
and for employee profile we are calling a `profile service` REST apis.

Endpoints:

`/employees` : To get the details of all the employees.

`/employees/{id}` : To get the employee details by `id` which is passed in url path.

Used lombok to remove verbosity and for Bean Autowiring used constructor injection using `RequiredArgsConstructor`.

Used WireMock for integration tests by creating a mock server for `profile service` and mocking requests to profile endpoints.

