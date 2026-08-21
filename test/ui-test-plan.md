# UI Test Plan

This file is the source of truth for console UI test cases run with the `test-ui` skill.

## Test configuration

- Program start command: To be recorded before the first test run.
- Working directory: Project root.
- Java version: 25.
- Session isolation: Start each test case in a fresh process unless a case says otherwise.
- Comparison: Exact text after normalizing CRLF and LF line endings. Spaces, capitalization, punctuation, divider lines, and blank lines are significant.

## Test cases

No test cases have been supplied yet. Add each case using the following structure before running it.

### UI-NNN: Short descriptive name

**Aim:** State the behavior this case verifies.

**Expected startup output:**

```text
Exact output emitted before the first input
```

#### Step 1

**Input:**

```text
command entered by the user
```

**Expected output:**

```text
Exact output emitted in response
```

Add further numbered steps in input order. Include the command that closes the program and its expected output when shutdown behavior is part of the case.
