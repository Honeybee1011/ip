# Lloyd User Guide

// Update the title above to match the actual product name

// Product screenshot goes here

// Product intro goes here

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Viewing reminders

Enter `reminder` to view incomplete deadlines and events that need attention.
Lloyd places tasks dated before today under `Overdue`. Tasks dated from today
through three calendar days later, inclusive, appear under
`Due within 3 days, including today`.

Deadlines use their due date. Events use their start date, even if the event
started earlier today. Todos and completed tasks are not shown. The reminder
period is fixed at three days and cannot be configured yet.

For example, if today is September 14, 2026:

```text
reminder
```

Lloyd may respond with:

```text
 The schedule waits for no one! Here are your reminders:
 Overdue:
 2.[D][ ] pay invoice (by: Sep 13 2026)
 Due within 3 days, including today:
 4.[E][ ] site inspection (from: Sep 14 2026, 10:00 AM to: Sep 14 2026, 11:00 AM)
 5.[D][ ] file permit (by: Sep 17 2026)
```

The displayed numbers are the tasks' positions in the master task list, so
they can be used directly with commands such as `mark` and `delete`.

The command does not accept arguments. For example, `reminder 5` produces:

```text
 The reminder schedule is fixed at 3 days for now. Enter reminder without any extra details.
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
