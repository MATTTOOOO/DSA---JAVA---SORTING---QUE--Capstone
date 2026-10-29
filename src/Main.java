import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final ArrayList<String> names = new ArrayList<>();
    private static final ArrayList<Integer> ages = new ArrayList<>();
    private static final ArrayList<String> appointmentTimes = new ArrayList<>();
    private static final ArrayList<Integer> patientIds = new ArrayList<>();
    private static final Queue<Integer> consultationQueue = new LinkedList<>();
    private static int nextPatientId = 1001;

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("=== Clinic Queue and Patient Management System ===");
        while (running) {
            showMenu();
            int choice = readInt("Choose an option: ", 0, 9);
            switch (choice) {
                case 1: addPatient(); break;
                case 2: displayPatients(); break;
                case 3: searchPatient(); break;
                case 4: removePatient(); break;
                case 5: enqueuePatient(); break;
                case 6: processNextPatient(); break;
                case 7: peekQueue(); break;
                case 8: displayQueue(); break;
                case 9: sortPatientsMenu(); break;
                case 0: running = false; System.out.println("Goodbye."); break;
                default: System.out.println("Invalid menu choice.");
            }
            if (running) pause();
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add patient record");
        System.out.println("2. Display patient records");
        System.out.println("3. Search by name or patient ID");
        System.out.println("4. Remove patient record");
        System.out.println("5. Enqueue patient for consultation");
        System.out.println("6. Call next patient (dequeue)");
        System.out.println("7. Peek at next patient");
        System.out.println("8. Display consultation queue / check empty");
        System.out.println("9. Sort patient records");
        System.out.println("0. Exit");
    }

    private static void addPatient() {
        String name = readNonEmpty("Patient name: ");
        int age = readInt("Age (0-120): ", 0, 120);
        String time = readAppointmentTime();
        int id = nextPatientId++;
        names.add(name);
        ages.add(age);
        appointmentTimes.add(time);
        patientIds.add(id);
        System.out.println("Patient added. ID: " + id);
    }

    private static void displayPatients() {
        if (names.isEmpty()) { System.out.println("No patient records to display."); return; }
        System.out.println("\nPatient records:");
        printHeader();
        for (int i = 0; i < names.size(); i++) printPatient(i);
    }

    private static void searchPatient() {
        if (names.isEmpty()) { System.out.println("No patient records to search."); return; }
        System.out.println("1. Search by name\n2. Search by patient ID");
        int mode = readInt("Choose search type: ", 1, 2);
        boolean found = false;
        if (mode == 1) {
            String query = readNonEmpty("Enter full or partial name: ").toLowerCase();
            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).toLowerCase().contains(query)) { if (!found) printHeader(); printPatient(i); found = true; }
            }
        } else {
            int id = readInt("Enter patient ID: ", 1, Integer.MAX_VALUE);
            int index = patientIds.indexOf(id);
            if (index >= 0) { printHeader(); printPatient(index); found = true; }
        }
        if (!found) System.out.println("No matching patient was found.");
    }

    private static void removePatient() {
        if (names.isEmpty()) { System.out.println("No patient records to remove."); return; }
        int id = readInt("Patient ID to remove: ", 1, Integer.MAX_VALUE);
        int index = patientIds.indexOf(id);
        if (index < 0) { System.out.println("Patient ID not found."); return; }
        Integer queuedId;
        while ((queuedId = consultationQueue.peek()) != null && queuedId == id) consultationQueue.poll();
        consultationQueue.remove(id);
        names.remove(index); ages.remove(index); appointmentTimes.remove(index); patientIds.remove(index);
        System.out.println("Patient record removed. Any queue entry for that patient was also removed.");
    }

    private static void enqueuePatient() {
        if (names.isEmpty()) { System.out.println("No patient records are available to enqueue."); return; }
        int id = readInt("Patient ID to add to consultation queue: ", 1, Integer.MAX_VALUE);
        int index = patientIds.indexOf(id);
        if (index < 0) { System.out.println("Patient ID not found; patient was not enqueued."); return; }
        if (consultationQueue.contains(id)) { System.out.println("That patient is already waiting in the queue."); return; }
        consultationQueue.offer(id);
        System.out.println(names.get(index) + " (ID " + id + ") joined the end of the queue.");
    }

    private static void processNextPatient() {
        if (consultationQueue.isEmpty()) { System.out.println("The consultation queue is empty; nobody can be called."); return; }
        int id = consultationQueue.poll();
        int index = patientIds.indexOf(id);
        if (index >= 0) System.out.println("Now calling " + names.get(index) + " (ID " + id + ").");
        else System.out.println("Queue entry for removed patient ID " + id + " was cleared.");
    }

    private static void peekQueue() {
        Integer id = consultationQueue.peek();
        if (id == null) { System.out.println("The consultation queue is empty."); return; }
        int index = patientIds.indexOf(id);
        if (index >= 0) System.out.println("Next: " + names.get(index) + " (ID " + id + ").");
        else System.out.println("Next queue entry has missing patient ID " + id + ".");
    }

    private static void displayQueue() {
        if (consultationQueue.isEmpty()) { System.out.println("The consultation queue is empty."); return; }
        System.out.println("Queue is FIFO (front to back):");
        int position = 1;
        for (Integer id : consultationQueue) {
            int index = patientIds.indexOf(id);
            if (index >= 0) System.out.println(position + ". " + names.get(index) + " (ID " + id + ")");
            position++;
        }
        System.out.println("Queue is not empty.");
    }

    private static void sortPatientsMenu() {
        if (names.isEmpty()) { System.out.println("No patient records to sort."); return; }
        System.out.println("1. Bubble Sort\n2. Insertion Sort");
        int algorithm = readInt("Choose sorting algorithm: ", 1, 2);
        System.out.println("Sort by: 1. Name  2. Age  3. Appointment time");
        int field = readInt("Choose field: ", 1, 3);
        System.out.println("Records before sorting:"); displayPatients();
        if (algorithm == 1) bubbleSort(field); else insertionSort(field);
        System.out.println("Records after " + (algorithm == 1 ? "Bubble Sort" : "Insertion Sort") + ":"); displayPatients();
        System.out.println("The consultation queue order was not changed.");
    }

    // Bubble Sort repeatedly moves the largest remaining selected value to the end.
    // Swapping every parallel-list field together keeps each patient's data aligned.
    private static void bubbleSort(int field) {
        for (int end = names.size() - 1; end > 0; end--) {
            boolean changed = false;
            for (int i = 0; i < end; i++) {
                if (compareRecords(i, i + 1, field) > 0) { swapRecords(i, i + 1); changed = true; }
            }
            if (!changed) break;
        }
    }

    // Insertion Sort grows a sorted prefix and inserts each next record into its
    // correct position; shifting whole records preserves parallel-list alignment.
    private static void insertionSort(int field) {
        for (int i = 1; i < names.size(); i++) {
            String name = names.get(i), time = appointmentTimes.get(i);
            int age = ages.get(i), id = patientIds.get(i), j = i - 1;
            while (j >= 0 && compareValues(j, name, age, time, field) > 0) {
                copyRecord(j, j + 1); j--;
            }
            names.set(j + 1, name); ages.set(j + 1, age);
            appointmentTimes.set(j + 1, time); patientIds.set(j + 1, id);
        }
    }

    private static int compareRecords(int left, int right, int field) {
        if (field == 1) return names.get(left).compareToIgnoreCase(names.get(right));
        if (field == 2) return Integer.compare(ages.get(left), ages.get(right));
        return Integer.compare(parseAppointmentMinutes(appointmentTimes.get(left)),
                parseAppointmentMinutes(appointmentTimes.get(right)));
    }

    private static int compareValues(int index, String name, int age, String time, int field) {
        if (field == 1) return names.get(index).compareToIgnoreCase(name);
        if (field == 2) return Integer.compare(ages.get(index), age);
        return Integer.compare(parseAppointmentMinutes(appointmentTimes.get(index)), parseAppointmentMinutes(time));
    }

    private static String readAppointmentTime() {
        while (true) {
            System.out.print("Appointment time (e.g. 09:30 AM or 14:30): ");
            String value = INPUT.nextLine().trim();
            if (parseAppointmentMinutes(value) >= 0) return value;
            System.out.println("Enter a valid time as H:MM, HH:MM, or with AM/PM (minutes 00-59).");
        }
    }

    // Convert supported 12-hour or 24-hour input to minutes after midnight so
    // sorting follows clock order instead of comparing the displayed text.
    private static int parseAppointmentMinutes(String value) {
        String normalized = value.trim().toUpperCase().replaceAll("\\s+", "");
        boolean hasAm = normalized.endsWith("AM");
        boolean hasPm = normalized.endsWith("PM");
        boolean hasMeridiem = hasAm || hasPm;
        if (hasMeridiem) normalized = normalized.substring(0, normalized.length() - 2);

        int colon = normalized.indexOf(':');
        if (colon <= 0 || colon != normalized.lastIndexOf(':') || colon == normalized.length() - 1) return -1;
        try {
            int hour = Integer.parseInt(normalized.substring(0, colon));
            int minute = Integer.parseInt(normalized.substring(colon + 1));
            if (minute < 0 || minute > 59) return -1;
            if (hasMeridiem) {
                if (hour < 1 || hour > 12) return -1;
                hour = hour % 12 + (hasPm ? 12 : 0);
            } else if (hour < 0 || hour > 23) return -1;
            return hour * 60 + minute;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void swapRecords(int a, int b) {
        String name = names.get(a); names.set(a, names.get(b)); names.set(b, name);
        int age = ages.get(a); ages.set(a, ages.get(b)); ages.set(b, age);
        String time = appointmentTimes.get(a); appointmentTimes.set(a, appointmentTimes.get(b)); appointmentTimes.set(b, time);
        int id = patientIds.get(a); patientIds.set(a, patientIds.get(b)); patientIds.set(b, id);
    }

    private static void copyRecord(int from, int to) {
        names.set(to, names.get(from)); ages.set(to, ages.get(from));
        appointmentTimes.set(to, appointmentTimes.get(from)); patientIds.set(to, patientIds.get(from));
    }

    private static void printHeader() { System.out.printf("%-8s %-24s %-6s %-24s%n", "ID", "Name", "Age", "Appointment"); }
    private static void printPatient(int i) { System.out.printf("%-8d %-24s %-6d %-24s%n", patientIds.get(i), names.get(i), ages.get(i), appointmentTimes.get(i)); }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = INPUT.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
                System.out.println("Please enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("Input cannot be empty.");
        }
    }

    private static void pause() { System.out.println("\nPress Enter to continue..."); INPUT.nextLine(); }
}
