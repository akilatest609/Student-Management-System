import java.util.Scanner;

public class Main {
    static Student[] students = new Student[100];
    static int count = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Sort Students by Marks");
            System.out.println("5. Delete Student by ID");
            System.out.println("6. Update Student Marks");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewStudents();
                case 3 -> searchStudent();
                case 4 -> sortStudents();
                case 5 -> deleteStudent();
                case 6 -> updateMarks();
                case 0 -> System.out.println("Exiting program...");
                default -> System.out.println("Invalid choice, try again.");
            }
        } while (choice != 0);

        sc.close();
    }

    // 1. Add student
    static void addStudent() {
        if (count >= students.length) {
            System.out.println("Student list is full!");
            return;
        }
        System.out.print("Enter ID: ");
        while (!sc.hasNextInt()) {
            System.out.print("Invalid ID. Enter a number: ");
            sc.next();
        }
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Marks: ");
        while (!sc.hasNextDouble()) {
            System.out.print("Invalid marks. Enter a number: ");
            sc.next();
        }
        double marks = sc.nextDouble();

        students[count] = new Student(id, name, marks);
        count++;
        System.out.println("Student added successfully.");
    }

    // 2. View all students
    static void viewStudents() {
        if (count == 0) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n--- Student List ---");
        for (int i = 0; i < count; i++) {
            System.out.println(students[i]);
        }
    }

    // 3. Search student by ID (Linear Search)
    static void searchStudent() {
        System.out.print("Enter ID to search: ");
        while (!sc.hasNextInt()) {
            System.out.print("Invalid ID. Enter a number: ");
            sc.next();
        }
        int id = sc.nextInt();

        for (int i = 0; i < count; i++) {
            if (students[i].getId() == id) {
                System.out.println("Student found: " + students[i]);
                return;
            }
        }
        System.out.println("Student with ID " + id + " not found.");
    }

    // 4. Sort students by marks (Bubble Sort, descending)
    static void sortStudents() {
        if (count == 0) {
            System.out.println("No students to sort.");
            return;
        }
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (students[j].getMarks() < students[j + 1].getMarks()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
        System.out.println("Students sorted by marks (highest first).");
        viewStudents();
    }

    // 5. Delete student by ID
    static void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        while (!sc.hasNextInt()) {
            System.out.print("Invalid ID. Enter a number: ");
            sc.next();
        }
        int id = sc.nextInt();

        int indexToDelete = -1;
        for (int i = 0; i < count; i++) {
            if (students[i].getId() == id) {
                indexToDelete = i;
                break;
            }
        }

        if (indexToDelete == -1) {
            System.out.println("Student not found.");
            return;
        }

        for (int i = indexToDelete; i < count - 1; i++) {
            students[i] = students[i + 1];
        }
        students[count - 1] = null;
        count--;
        System.out.println("Student deleted successfully.");
    }

    // 6. Update student marks
    static void updateMarks() {
        System.out.print("Enter ID to update: ");
        while (!sc.hasNextInt()) {
            System.out.print("Invalid ID. Enter a number: ");
            sc.next();
        }
        int id = sc.nextInt();

        for (int i = 0; i < count; i++) {
            if (students[i].getId() == id) {
                System.out.print("Enter new marks: ");
                while (!sc.hasNextDouble()) {
                    System.out.print("Invalid marks. Enter a number: ");
                    sc.next();
                }
                double newMarks = sc.nextDouble();
                students[i].setMarks(newMarks);
                System.out.println("Marks updated successfully.");
                return;
            }
        }
        System.out.println("Student not found.");
    }
}