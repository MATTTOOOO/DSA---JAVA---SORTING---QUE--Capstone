# Clinic Queue and Patient Management System

## Project Description

This console application helps a small clinic keep patient details and manage the order in which patients are called for consultation. Patient records can be searched and sorted, while a separate FIFO queue preserves arrival order for consultations.

## Objectives

- Practice procedural Java with one `Main` class and static methods.
- Store related patient fields in synchronized parallel `ArrayList`s.
- Use a `LinkedList`-backed queue for real consultation workflow.
- Implement Bubble Sort and Insertion Sort manually.
- Validate input and report empty or unsuccessful operations clearly.

## Use Cases and Features

- Add patient records with an automatically assigned patient ID, name, age, and appointment time.
- Display all records and search by full or partial name or patient ID.
- Remove a patient record; any matching waiting queue entry is removed too.
- Enqueue a registered patient, view the next patient, display/check the queue, and call the next patient.
- Sort records by name, age, or appointment time using either algorithm. Sorting displays the records before and after and never changes queue order.
- Invalid numbers, out-of-range values, and blank required text are handled with prompts to retry.

## Data Structure Explanation

Four parallel `ArrayList`s store names, ages, appointment times, and patient IDs. The value at index `i` in every list belongs to the same patient. Add and removal operations update all lists together, and sorting swaps or shifts every field together. IDs are stable even when record positions change.

The consultation queue is a `Queue<Integer>` backed by `LinkedList`; it stores patient IDs rather than list indexes. `offer`, `poll`, `peek`, iteration, and `isEmpty` support enqueue, FIFO dequeue, next-patient preview, queue display, and empty checks. Sorting the record lists therefore does not reorder who is called next.

## Sorting Algorithm Explanation

### Bubble Sort

Bubble Sort compares neighboring records and swaps them when their selected field is out of order. Each pass moves a largest remaining value to the end; an early-exit check stops when a pass makes no swaps. It is simple for a small clinic's record list and makes each exchange easy to follow. The app can use it to order records by name, age, or appointment-time text.

### Insertion Sort

Insertion Sort grows a sorted prefix by taking each next record and shifting larger records right until it fits. It works well for small lists and lists that are already partly ordered. The app offers the same three sort fields. Both implementations operate directly on the parallel lists and use no built-in sorting method.

## How to Run the Program

From the project root, compile and run with a JDK installed:

```text
javac src/Main.java
java -cp src Main
```

Choose `0` from the main menu to exit. The generated `.class` file is excluded from Git by `.gitignore`.

## Screenshots of the System

The following captures were provided and placed in `screenshots/`:

| Screenshot | What it shows |
|---|---|
| [main-menu.png](screenshots/main-menu.png) | Main menu. |
| [add-patient.png](screenshots/add-patient.png) | Adding patient ID 1007. |
| [display-patients.png](screenshots/display-patients.png) | Displaying the stored patient records, including ID 1007. |
| [queue-enqueue.png](screenshots/queue-enqueue.png) | Enqueueing patient ID 1002. |
| [queue-dequeue.png](screenshots/queue-dequeue.png) | Calling patient ID 1002. |
| [queue-display-fifo.png](screenshots/queue-display-fifo.png) | Queue displayed front to back with two patients. |
| [fifo-queue.png](screenshots/fifo-queue.png) | Queue order with patient ID 1004 first. |
| [fifo-next-called.png](screenshots/fifo-next-called.png) | The next call is patient ID 1004, matching the queue front shown alongside. |
| [bubble-sort.png](screenshots/bubble-sort.png) | Bubble Sort by name, before and after. |
| [bubble-sort-single-record.png](screenshots/bubble-sort-single-record.png) | Bubble Sort output with a single record. |
| [insertion-sort.png](screenshots/insertion-sort.png) | Insertion Sort by age, before and after. |
| [input-validation.png](screenshots/input-validation.png) | Invalid ID input rejected and prompt repeated. |

The add/display and FIFO captures are now included. One screenshot is still needed for the full queue-operations capture:

- **Peek operation:** capture the app showing the next patient with the peek option. For the complete queue-operations screenshot, include enqueue, peek, display, and dequeue output.
