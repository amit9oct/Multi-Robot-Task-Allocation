# Multi-Robot-Task-Allocation
Contributors
--------------------
  
  Amitayush Thakur
  Garima Dhanania
  
--------------------

# What is this simulation about?
It is about implementation of distibuted algorithms to solve the Multi-Robot-Task-Allocation problem.
Problem description can be found here:
  http://robotics.usc.edu/~gerkey/research/mrta.html

# What does it do?
It works on efficient algorithms to implement static Multi-Robot-Task Allocation problem. It uses Robocode
as its simulation environment. It can be modified to test any Swarm Agent based Algorithm.
The main idea behind our algorithm is:
      :: Central Clustering 
      :: Low Level Obstacle Handling and optimal movement using Travelling Salesmen Algorithm(Christofides)
      :: Optimal Allocation of task using Hungarian Algorithm
