# Disambiguation / Functional choices

* what data to use for the headline?
  * Decision -> use `data.title` returned by the Giphy Api

## Data Layer

* Retrofit + Gson

## Dependency injection

Decision → use Hilt.

Pros

* I know Hilt; and it's relatively easy to set up.

Cons

* Hilt/Dagger depend on Java and the JVM; so it can't be used for multi-platform Kotlin code.

## Logging

Decision → use Timber.

## Repository

Decision → Coroutines for handling concurrency

## ViewModels

Decision → LiveData for exposing data streams

The LiveData API is simple (simplistic?). For more advanced use cases it could be worth it to use 
Kotlin Flows. Although it could be argued that NOT exposing Flows to the MVVM View is an advantage
because it reduces the temptation to add logic into the View.

## MVVM View implementation

* Decision → Use one Activity with multiple Fragments.
* Decision → Use classic Android Views; because that's what I'm familiar with.

## Tests

### Unit tests

Write a unit test for the Repository that uses a fake implementation of the Giphy API

When it makes sense I prefer to use real dependencies; even in unit tests.
Using the real REST API would be slow and flaky; so I decided to use a fake API.

Using real or fake dependencies makes it easier to test the requirements without testing
the implementation details.

### Integration tests

TBD

### End to end tests

TBD