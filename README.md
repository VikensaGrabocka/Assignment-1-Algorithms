# Assignment 1

This program aims to solve the problem of finding the 
cheapest configuration of a computing grid used to 
predict weather. It reads a file provided by the user
and based on the given data it computes the cheapest cost, the
number of cheapest configurations, and it visualizes
one cheapest configuration. The node/nodes denoted by green color represent the dyno/dynos that host the bucket.

Please provide the file in the correct format! 
The file should consist of k + 1 lines. 
The first line should consist of four integer values, space
separated. These values should be the number of dynos (each dyno numbered 1..n),
number of possible bonds k, bucket cost and bond cost. After that k more lines
should follow indicating which pair of dynos can be bonded. Each such line should contain
two space separated integers indicating corresponding dynos.

**WARNING**

If the data is not provided in the correct format the program will not behave as expected. 