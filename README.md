# HPC_Assignment_2
Concurrent Queue Simulation in Java
Overview
This project simulates two types of concurrent queue systems using Java:

Bank Queue: A single shared queue with multiple tellers serving customers concurrently.
Grocery Queues: Multiple cashiers, each with their own queue. Customers join the shortest queue or leave if all are full after waiting for a short time.
The program is multithreaded and handles concurrency with synchronization to prevent race conditions. It outputs data on the number of customers served, those who left without service, and average service time.

The simulation will run for 2 hours, with 3 tellers and 3 cashiers.

Authors
  abuhasan60a
  MohiuddinPrantiq
