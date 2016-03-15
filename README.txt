Thumbtack’s Simple Database Challenge
https://www.thumbtack.com/challenges/simple-database

Author: Lin Su

java version "1.8.0_60"
Java(TM) SE Runtime Environment (build 1.8.0_60-b27)
Java HotSpot(TM) 64-Bit Server VM (build 25.60-b23, mixed mode)
-------------------------------------------------------------------------------------- 
Instruction of executing the code:

1. Compile 
Make sure simpleDB.java and run.java are in the same fold, compile from this folder:
% javac simipleDB.java run.java

2. Run the program by passing a file of commands
% java run test.txt
(test.txt is in the same folder, or you need to specify the path of test file)

3.Run the program interactively
% java run
Start to enter command described in the Challenge, use “END” to exit program. For example:
SET a 50
BEGIN
GET a
SET a 60
BEGIN
UNSET a
GET a
ROLLBACK
GET a
COMMIT
GET a
END

-------------------------------------------------------------------------------------- 
Programming design explanation:

There are two files in my program simpleDB.jave and run.java.
SimpleDB.java is where the simple database is designed, including SET, GET, BEGIN and all the other required command.
Run.jave is the test file to invoke the program. It includes main() method, and deals with standard input and output.

Here’s my though process for this program: 

1. In this program, first there is a base database outside the transaction. After each BEGIN command, there will be created a new block database with initialization the same as previous block database. But every block database should be stored separately until the ROLLBACK or COMMIT command. Therefore, each block database should have its own memory, and they can be linked by a linked list.

2. For each block, in order to get a good performance for commands BEGIN, GET, SET, and UNSET, Hashtable or HashMap is the first choice, since it only need constant time to get or put new element.
Hashtable is thread safety, while HashMap has a better performance when there is no multi-thread. For the consideration of thread safety that if many people access database at the same time, simpleDB use Hashtable.

3. In order to give NUMEQUALTO an average-case runtime of O(log N) or better, there need to add another hashtable for each block to record the count of each distinguish value.

In summary, there will be two hashtable for each block recording name_value and value_count. And two hashtable are stored separately in two linked list, which are:
LinkedList<Hashtable<String, Integer>> database;
LinkedList<Hashtable<String, Integer>> count;
The first element in linkedList is information of base block.

4. For COMMIT command, in order to permanently apply the changes, we need to go through all the blocks with order in the linked list, and overwrites the information in the base block.

