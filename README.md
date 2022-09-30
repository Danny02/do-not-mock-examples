# Test examples exposing Mocks as the root of all evil

Here you will find a collection of cases in which fakes result
in more stable tests then if mocks where used.

## Cases

### CQRS

This is a more involved case. You can find a service can write 
and read customer data. The data is managed in a write and read
model, which is updated from the write model.

The case consist of two similar implementations, which only differ
in the implementation of persistent layer. 

In this scenario the application started with the version which
used a delete flag (flag package). This was then refactored to
just delete data directly (direct packag).

The refactored version (direct) contains a bug in the service 
code.

Tests with mocks do not detect this bug, as can be seen 
by executing the tests.

### Logging

Shows the problems when mocking overloaded methods. 
The difficulties mocking Slf4j are also shown.
