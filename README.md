# Query Performance Problem

In company ABC we run quite a few queries against our databases, and the queries have different performance
characteristics. We frequently need to work on optimizing them. We want to build a system that allow us to
compare different versions of the same query and be able to benchmark the performance of its versions.
Build a system to perform this benchmarking with the following characteristics:
it is a restful service built with Spring (and optionally Spring Boot)
it can execute a performance test
it measures the time for each query to complete (work time)
exactly one of the performance tests can execute at any point of time against a database installation.
tests against different database installations can execute in parallel. So if a user starts a test for query Q, this
test can execute in parallel against databases A,B,C and collect the results in a "report"
Define a data model, define the rest api and write the code for the service.


## I've implemented this with multi-tenancy concepts
project not cleaned up, because it's wasn't sent