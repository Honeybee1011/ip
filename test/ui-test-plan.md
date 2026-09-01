# UI Test Plan

This file is the source of truth for console UI test cases run with the `test-ui` skill.

## Test configuration

- Compilation command: `javac -d out (Get-ChildItem src/main/java -Recurse -Filter *.java).FullName`
- Compilation working directory: `C:\Users\joshu\Code\ip`
- Default program start command: `java -cp ..\..\out lloyd.Lloyd`
- Default UI working directory: `C:\Users\joshu\Code\ip\_temp\ui-case`
- Java version: 25.
- Session isolation: Start each test case in a fresh process.
- Storage setup: Before each default Lloyd UI case, use a fresh working directory
  without a `data` directory. A case can provide seeded data explicitly.
- Comparison: Exact text after normalizing CRLF and LF line endings. Spaces, capitalization, punctuation, divider lines, and blank lines are significant.
- Indentation used to format examples in chat is not part of the expected output.

## Test cases

### STORAGE-001: Save and load all task types

**Aim:** Verify that the standalone storage class initializes missing storage,
writes the specified delimited format safely, restores task data, preserves an
existing file after rejected data, and reports malformed data and write failures.

**Compilation command:** `javac -d out (Get-ChildItem src/main/java,test -Recurse -Filter *.java).FullName`

**Program start command:** `java -cp out lloyd.storage.StorageTest`

**Expected startup output:**

```text
Storage save format: PASSED
Storage load: PASSED
Missing storage initialization: PASSED
Invalid storage data: PASSED
Rejected save preservation: PASSED
Storage write failure reporting: PASSED
Reserved delimiter validation: PASSED
```

This test has no user input. The process must exit successfully immediately after printing the expected output.

### UI-009: Save after every successful task-list change

**Aim:** Verify that the chatbot writes the complete task list after adding, marking,
unmarking, and deleting tasks.

**Working directory:** `C:\Users\joshu\Code\ip\_temp\ui-save-test`

**Program start command:** `java -cp ..\..\out lloyd.Lloyd`

**Storage setup:** Ensure the working directory does not contain a `data` directory.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo survey land
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] survey land
 Tasks currently in the master plan: 1.
____________________________________________________________

```

**Expected `data/lloyd.txt`:**

```text
T | 0 | survey land
```

#### Step 2

**Input:**

```text
deadline build bridge /by 29/08/2026
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [D][ ] build bridge (by: Aug 29 2026)
 Tasks currently in the master plan: 2.
____________________________________________________________

```

**Expected `data/lloyd.txt`:**

```text
T | 0 | survey land
D | 0 | build bridge | 2026-08-29
```

#### Step 3

**Input:**

```text
mark 2
```

**Expected output:**

```text
____________________________________________________________
 Magnificent! Efficient work means lower costs. This task is officially complete:
[D][X] build bridge (by: Aug 29 2026)
____________________________________________________________

```

**Expected `data/lloyd.txt`:**

```text
T | 0 | survey land
D | 1 | build bridge | 2026-08-29
```

#### Step 4

**Input:**

```text
event opening ceremony /from 31/08/2026 0900 /to 31/08/2026 1000
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [E][ ] opening ceremony (from: Aug 31 2026, 9:00 AM to: Aug 31 2026, 10:00 AM)
 Tasks currently in the master plan: 3.
____________________________________________________________

```

**Expected `data/lloyd.txt`:**

```text
T | 0 | survey land
D | 1 | build bridge | 2026-08-29
E | 0 | opening ceremony | 2026-08-31T09:00 | 2026-08-31T10:00
```

#### Step 5

**Input:**

```text
unmark 2
```

**Expected output:**

```text
____________________________________________________________
 What? Rework? That is terrible for the budget! Fine, this task is back under construction:
[D][ ] build bridge (by: Aug 29 2026)
____________________________________________________________

```

**Expected `data/lloyd.txt`:**

```text
T | 0 | survey land
D | 0 | build bridge | 2026-08-29
E | 0 | opening ceremony | 2026-08-31T09:00 | 2026-08-31T10:00
```

#### Step 6

**Input:**

```text
delete 1
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Waste eliminated from the budget. I have removed this task:
[T][ ] survey land
 Tasks currently in the master plan: 2.
____________________________________________________________

```

**Expected `data/lloyd.txt`:**

```text
D | 0 | build bridge | 2026-08-29
E | 0 | opening ceremony | 2026-08-31T09:00 | 2026-08-31T10:00
```

#### Step 7

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-010: Load saved tasks when the chatbot starts

**Aim:** Verify that the chatbot loads saved todos, deadlines, and events in order,
including their completion states and task-specific information.

**Working directory:** `C:\Users\joshu\Code\ip\_temp\ui-load-test`

**Program start command:** `java -cp ..\..\out lloyd.Lloyd`

**Initial `data/lloyd.txt`:**

```text
T | 1 | read book
D | 0 | return book | 2026-06-06
E | 0 | project meeting | 2026-08-06T14:00 | 2026-08-06T16:00
```

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][X] read book
 2.[D][ ] return book (by: Jun 6 2026)
 3.[E][ ] project meeting (from: Aug 6 2026, 2:00 PM to: Aug 6 2026, 4:00 PM)
____________________________________________________________

```

#### Step 2

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-011: Recover from missing storage and rejected task data

**Aim:** Verify that startup creates missing storage, a todo without a description
is rejected, and unsavable task data is rolled back without terminating the chatbot.

**Working directory:** `C:\Users\joshu\Code\ip\_temp\ui-storage-errors`

**Program start command:** `java -cp ..\..\out lloyd.Lloyd`

**Storage setup:** Ensure the working directory does not contain a `data` directory.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo
```

**Expected output:**

```text
____________________________________________________________
 Every task needs a description. Tell me what needs doing.
____________________________________________________________

```

#### Step 2

**Input:**

```text
todo compare A | B
```

**Expected output:**

```text
____________________________________________________________
 I could not save that change. The task list was left unchanged. Check that data/lloyd.txt can be written and task details do not contain the | character.
____________________________________________________________

```

#### Step 3

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
____________________________________________________________

```

**Expected `data/lloyd.txt`:** An empty file.

#### Step 4

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-012: Report malformed saved data cleanly

**Aim:** Verify that malformed task data produces a clear error without a Java
stack trace or an accidental overwrite of the storage file.

**Working directory:** `C:\Users\joshu\Code\ip\_temp\ui-corrupt-storage`

**Program start command:** `java -cp ..\..\out lloyd.Lloyd`

**Initial `data/lloyd.txt`:**

```text
T | 2 | invalid status
```

**Expected startup output:** Same as UI-001.

**Expected output immediately after startup:**

```text
____________________________________________________________
 I could not load the task file. Check that data/lloyd.txt contains valid task data and can be read.
____________________________________________________________

```

The process must exit successfully without accepting input, and the seeded file
must remain unchanged.

### UI-001: Add and list all task types

**Aim:** Verify that todo, deadline, and event commands create the correct task subtype, preserve their details, update the count, and appear correctly in the list.

**Expected startup output:**

```text
____________________________________________________________
 _      _                 _
| |    | |               | |
| |    | | ___  _   _  __| |
| |    | |/ _ \| | | |/ _` |
| |____| | (_) | |_| | (_| |
|______|_|\___/ \__, |\__,_|
                 __/ |       
                |___/        
 Lloyd Frontera, the greatest estate developer, at your service!
 Got a problem? Excellent. Problems are profits waiting for an engineer.
 Now, what needs doing?
____________________________________________________________

```

#### Step 1

**Input:**

```text
todo read book
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] read book
 Tasks currently in the master plan: 1.
____________________________________________________________

```

#### Step 2

**Input:**

```text
mark 1
```

**Expected output:**

```text
____________________________________________________________
 Magnificent! Efficient work means lower costs. This task is officially complete:
[T][X] read book
____________________________________________________________

```

#### Step 3

**Input:**

```text
deadline return book /by 06/06/2026
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [D][ ] return book (by: Jun 6 2026)
 Tasks currently in the master plan: 2.
____________________________________________________________

```

#### Step 4

**Input:**

```text
event project meeting /from 06/08/2026 1400 /to 06/08/2026 1600
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [E][ ] project meeting (from: Aug 6 2026, 2:00 PM to: Aug 6 2026, 4:00 PM)
 Tasks currently in the master plan: 3.
____________________________________________________________

```

#### Step 5

**Input:**

```text
todo join sports club
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] join sports club
 Tasks currently in the master plan: 4.
____________________________________________________________

```

#### Step 6

**Input:**

```text
mark 4
```

**Expected output:**

```text
____________________________________________________________
 Magnificent! Efficient work means lower costs. This task is officially complete:
[T][X] join sports club
____________________________________________________________

```

#### Step 7

**Input:**

```text
todo borrow book
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] borrow book
 Tasks currently in the master plan: 5.
____________________________________________________________

```

#### Step 8

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][X] read book
 2.[D][ ] return book (by: Jun 6 2026)
 3.[E][ ] project meeting (from: Aug 6 2026, 2:00 PM to: Aug 6 2026, 4:00 PM)
 4.[T][X] join sports club
 5.[T][ ] borrow book
____________________________________________________________

```

#### Step 9

**Input:**

```text
deadline return book /by 30/08/2026
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [D][ ] return book (by: Aug 30 2026)
 Tasks currently in the master plan: 6.
____________________________________________________________

```

#### Step 10

**Input:**

```text
event project meeting /from 07/08/2026 1400 /to 07/08/2026 1600
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [E][ ] project meeting (from: Aug 7 2026, 2:00 PM to: Aug 7 2026, 4:00 PM)
 Tasks currently in the master plan: 7.
____________________________________________________________

```

#### Step 11

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-013: Check deadlines and event endpoints by date

**Aim:** Verify that `check` lists deadlines due on the requested date and events
that start or end on that date, while excluding todos and dates strictly between
the endpoints of a multi-day event. Also verify missing, invalid, and empty-result
responses.

**Working directory:** `C:\Users\joshu\Code\ip\_temp\ui-check-test`

**Program start command:** `java -cp ..\..\out lloyd.Lloyd`

**Initial `data/lloyd.txt`:**

```text
T | 0 | inspect foundations
D | 0 | submit permit | 2026-08-29
D | 0 | pay supplier | 2026-08-30
E | 0 | council meeting | 2026-08-29T09:00 | 2026-08-29T10:00
E | 0 | building works | 2026-08-29T08:00 | 2026-09-02T18:00
E | 0 | site survey | 2026-08-27T08:00 | 2026-08-29T18:00
```

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
check 29/08/2026
```

**Expected output:**

```text
____________________________________________________________
 Deadlines and event endpoints on Aug 29 2026:
 2.[D][ ] submit permit (by: Aug 29 2026)
 4.[E][ ] council meeting (from: Aug 29 2026, 9:00 AM to: Aug 29 2026, 10:00 AM)
 5.[E][ ] building works (from: Aug 29 2026, 8:00 AM to: Sep 2 2026, 6:00 PM)
 6.[E][ ] site survey (from: Aug 27 2026, 8:00 AM to: Aug 29 2026, 6:00 PM)
____________________________________________________________

```

#### Step 2

**Input:**

```text
check 30/08/2026
```

**Expected output:**

```text
____________________________________________________________
 Deadlines and event endpoints on Aug 30 2026:
 3.[D][ ] pay supplier (by: Aug 30 2026)
____________________________________________________________

```

#### Step 3

**Input:**

```text
check 31/08/2026
```

**Expected output:**

```text
____________________________________________________________
 No deadlines or event endpoints fall on Aug 31 2026.
____________________________________________________________

```

#### Step 4

**Input:**

```text
check
```

**Expected output:**

```text
____________________________________________________________
 Tell me which date to inspect using dd/MM/yyyy.
____________________________________________________________

```

#### Step 5

**Input:**

```text
check 31/02/2026
```

**Expected output:**

```text
____________________________________________________________
 Enter the date to check in dd/MM/yyyy format.
____________________________________________________________

```

#### Step 6

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-002: Reject an invalid deadline date

**Aim:** Verify that a deadline not written in `dd/MM/yyyy` format is rejected
without changing the task list.

**Expected startup output:** Same as UI-001.

The first seven setup interactions and their expected outputs are Steps 1–7 of UI-001. They establish five tasks in a fresh process.

#### Step 8

**Input:**

```text
deadline do homework /by no idea :-p
```

**Expected output:**

```text
____________________________________________________________
 Enter the deadline in dd/MM/yyyy format.
____________________________________________________________

```

#### Step 9

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-003: Show themed command and validation responses

**Aim:** Verify that unmarking and invalid command details produce Lloyd-themed responses without changing their existing behavior.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo inspect foundations
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] inspect foundations
 Tasks currently in the master plan: 1.
____________________________________________________________

```

#### Step 2

**Input:**

```text
unmark 1
```

**Expected output:**

```text
____________________________________________________________
 What? Rework? That is terrible for the budget! Fine, this task is back under construction:
[T][ ] inspect foundations
____________________________________________________________

```

#### Step 3

**Input:**

```text
mark
```

**Expected output:**

```text
____________________________________________________________
 Even I cannot finish an imaginary task. Give me the task number to mark.
____________________________________________________________

```

#### Step 4

**Input:**

```text
mark gold
```

**Expected output:**

```text
____________________________________________________________
 A task number needs to be a number. Even Javier knows that.
____________________________________________________________

```

#### Step 5

**Input:**

```text
mark 99
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 6

**Input:**

```text
unmark
```

**Expected output:**

```text
____________________________________________________________
 Rework requires paperwork. Give me the task number to unmark.
____________________________________________________________

```

#### Step 7

**Input:**

```text
deadline
```

**Expected output:**

```text
____________________________________________________________
 Every profitable project needs details. Provide a description and /by date.
____________________________________________________________

```

#### Step 8

**Input:**

```text
deadline build bridge tomorrow
```

**Expected output:**

```text
____________________________________________________________
 No deadline, no schedule. Specify it using /by.
____________________________________________________________

```

#### Step 9

**Input:**

```text
event
```

**Expected output:**

```text
____________________________________________________________
 Every grand event needs a plan. Provide a description, /from date, and /to date.
____________________________________________________________

```

#### Step 10

**Input:**

```text
event grand opening tomorrow
```

**Expected output:**

```text
____________________________________________________________
 An event without a schedule invites disaster. Specify it using /from and /to.
____________________________________________________________

```

#### Step 11

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-004: Reject tasks without a type prefix

**Aim:** Verify that input without a `todo`, `deadline`, or `event` prefix is rejected and is not added to the task list.

**Working directory:** `C:\Users\joshu\Code\ip\_temp\ui-invalid-prefix-test`

**Storage setup:** Use a fresh working directory without a `data` directory.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
inspect foundations
```

**Expected output:**

```text
____________________________________________________________
 I reject vague contracts. Start every task with todo, deadline, or event.
____________________________________________________________

```

#### Step 2

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
____________________________________________________________

```

#### Step 3

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-005: Preserve the task list after invalid creation commands

**Aim:** Verify that malformed deadline and event commands, plus unprefixed input, are rejected without changing the number, order, or contents of valid tasks.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo survey land
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] survey land
 Tasks currently in the master plan: 1.
____________________________________________________________

```

#### Step 2

**Input:**

```text
deadline build bridge tomorrow
```

**Expected output:**

```text
____________________________________________________________
 No deadline, no schedule. Specify it using /by.
____________________________________________________________

```

#### Step 3

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] survey land
____________________________________________________________

```

#### Step 4

**Input:**

```text
deadline build bridge /by 29/08/2026
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [D][ ] build bridge (by: Aug 29 2026)
 Tasks currently in the master plan: 2.
____________________________________________________________

```

#### Step 5

**Input:**

```text
event opening ceremony /from Monday
```

**Expected output:**

```text
____________________________________________________________
 An event without a schedule invites disaster. Specify it using /from and /to.
____________________________________________________________

```

#### Step 6

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] survey land
 2.[D][ ] build bridge (by: Aug 29 2026)
____________________________________________________________

```

#### Step 7

**Input:**

```text
event opening ceremony /from  /to Tuesday
```

**Expected output:**

```text
____________________________________________________________
 The project contract is incomplete. Provide a description, /from date, and /to date.
____________________________________________________________

```

#### Step 8

**Input:**

```text
event opening ceremony /from 31/08/2026 0900 /to 31/08/2026 1000
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [E][ ] opening ceremony (from: Aug 31 2026, 9:00 AM to: Aug 31 2026, 10:00 AM)
 Tasks currently in the master plan: 3.
____________________________________________________________

```

#### Step 9

**Input:**

```text
build tunnel
```

**Expected output:**

```text
____________________________________________________________
 I reject vague contracts. Start every task with todo, deadline, or event.
____________________________________________________________

```

#### Step 10

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] survey land
 2.[D][ ] build bridge (by: Aug 29 2026)
 3.[E][ ] opening ceremony (from: Aug 31 2026, 9:00 AM to: Aug 31 2026, 10:00 AM)
____________________________________________________________

```

#### Step 11

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-006: Preserve completion state after invalid updates

**Aim:** Verify that invalid task numbers and nonnumeric values do not mark or unmark a valid task, while valid updates still work between the rejected commands.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo inspect walls
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] inspect walls
 Tasks currently in the master plan: 1.
____________________________________________________________

```

#### Step 2

**Input:**

```text
mark 1
```

**Expected output:**

```text
____________________________________________________________
 Magnificent! Efficient work means lower costs. This task is officially complete:
[T][X] inspect walls
____________________________________________________________

```

#### Step 3

**Input:**

```text
mark 0
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 4

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][X] inspect walls
____________________________________________________________

```

#### Step 5

**Input:**

```text
unmark gold
```

**Expected output:**

```text
____________________________________________________________
 A task number needs to be a number. Even Javier knows that.
____________________________________________________________

```

#### Step 6

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][X] inspect walls
____________________________________________________________

```

#### Step 7

**Input:**

```text
unmark 1
```

**Expected output:**

```text
____________________________________________________________
 What? Rework? That is terrible for the budget! Fine, this task is back under construction:
[T][ ] inspect walls
____________________________________________________________

```

#### Step 8

**Input:**

```text
unmark 2
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 9

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] inspect walls
____________________________________________________________

```

#### Step 10

**Input:**

```text
mark -1
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 11

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] inspect walls
____________________________________________________________

```

#### Step 12

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-007: Delete a task and renumber the remaining list

**Aim:** Verify that deleting a task removes the selected object, reports the new task count, and shifts later tasks into consecutive list positions without changing their contents.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo survey land
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] survey land
 Tasks currently in the master plan: 1.
____________________________________________________________

```

#### Step 2

**Input:**

```text
deadline build bridge /by 29/08/2026
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [D][ ] build bridge (by: Aug 29 2026)
 Tasks currently in the master plan: 2.
____________________________________________________________

```

#### Step 3

**Input:**

```text
event opening ceremony /from 31/08/2026 0900 /to 31/08/2026 1000
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [E][ ] opening ceremony (from: Aug 31 2026, 9:00 AM to: Aug 31 2026, 10:00 AM)
 Tasks currently in the master plan: 3.
____________________________________________________________

```

#### Step 4

**Input:**

```text
mark 2
```

**Expected output:**

```text
____________________________________________________________
 Magnificent! Efficient work means lower costs. This task is officially complete:
[D][X] build bridge (by: Aug 29 2026)
____________________________________________________________

```

#### Step 5

**Input:**

```text
delete 2
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Waste eliminated from the budget. I have removed this task:
[D][X] build bridge (by: Aug 29 2026)
 Tasks currently in the master plan: 2.
____________________________________________________________

```

#### Step 6

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] survey land
 2.[E][ ] opening ceremony (from: Aug 31 2026, 9:00 AM to: Aug 31 2026, 10:00 AM)
____________________________________________________________

```

#### Step 7

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```

### UI-008: Reject invalid delete commands without changing the list

**Aim:** Verify that a missing, nonnumeric, zero, negative, or out-of-range task number is rejected and does not remove an existing task.

**Expected startup output:** Same as UI-001.

#### Step 1

**Input:**

```text
todo inspect foundations
```

**Expected output:**

```text
____________________________________________________________
 Excellent! Another investment in your future has been approved:
   [T][ ] inspect foundations
 Tasks currently in the master plan: 1.
____________________________________________________________

```

#### Step 2

**Input:**

```text
delete
```

**Expected output:**

```text
____________________________________________________________
 Demolition needs a target. Give me the task number to delete.
____________________________________________________________

```

#### Step 3

**Input:**

```text
delete gold
```

**Expected output:**

```text
____________________________________________________________
 A task number needs to be a number. Even Javier knows that.
____________________________________________________________

```

#### Step 4

**Input:**

```text
delete 0
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 5

**Input:**

```text
delete -1
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 6

**Input:**

```text
delete 2
```

**Expected output:**

```text
____________________________________________________________
 That task is not in the master plan. Check its number.
____________________________________________________________

```

#### Step 7

**Input:**

```text
list
```

**Expected output:**

```text
____________________________________________________________
 Behold! Here is the master plan:
 1.[T][ ] inspect foundations
____________________________________________________________

```

#### Step 8

**Input:**

```text
bye
```

**Expected output:**

```text
____________________________________________________________
 Leaving already? Fine. Rest while you can; those tasks will not build themselves. Come back when you are ready to work... and remember to bring payment!
____________________________________________________________

```
