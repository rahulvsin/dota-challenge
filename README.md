bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.

# How it works

combat log file will be processed and inserted the significant events in H2 database as part of `POST /api/match` Endpoint.
REST Endpoints are implemented to retrieve the significant events for specific match.
JpaRepository are used to interacting with database.


# Improvements Possible


* [ ] Implement Test cases
