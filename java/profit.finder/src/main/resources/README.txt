
=================================================================
This program can accept 3 optional parameters:
--input <input file path>
--output <output file path>
--threads <number of threads used to process input file>
Default values for them are: "price_moves.csv", "result.txt", 1

=================================================================
The input file is processed in 3 steps:

1. It is splitted into n segments, where n is the number of threads. For each segment, a Iterator<String> is generated. This iterator returns one line of the input file in current segment when its next() is called.

2. A thread pool with n threads is created. Each thread processes on segment and generates an intermediate result, which contains the following info:
   a) the lowest price in this segment
   b) the highest price in this segment
   c) the last price of this segment (price of last entry)
   d) the high price point to generate the maximum profit in current segment
   e) the low price point to generate the maximum profit in current segment
   The intermediate result is an object of class Analytic. All Analytic objects are stored in a List.
   All these infomation by scanning the segment once. Item a, b and c are obvious and can be found quite easy. As for the max profit, it is also simple. At evert price point, the maximum profit we can get is exactly the difference of current price and the lowest price that happened before it. So, we can calculate the profit we can get for each price point as we browse the segment. After compare it with the reacorded maximum profit, we can easily find the best buy and sell point in that segment.
   The alogorithm has a complexity of O(N), where N is the segment length.

3. Merge the intermediate results. 
   Browse through the intermediate results list and record the lowest price we have met. The following value is a potential maximum profit: 
         (the highest price in current segment) - (recorded lowest price)
   Compare it with the max profit values in different segments and pick up the maximum one. That should be the value we are trying to find.

It is possible that there are several pairs of best buy/sell points. The program only returns the first one.

=================================================================
Run method:
Unzip and untar the "MaxProfitFinder.tar.gz" file. Use the following command to execute it:
java -d64 MaxProfitFinder/profit.finder/target/profit.finder-1.0-jar-with-dependencies.jar --threads n

****Please specify a thread number suitable for you machine!****
I suggest it be set to: 2 * n + 2, where n is the number of cores on the running machine.


