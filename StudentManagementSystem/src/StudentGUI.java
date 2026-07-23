import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StudentGUI extends JFrame {
    static Student[] students = new Student[100];
    static int count = 0;

    DefaultTableModel tableModel;
    JTable table;
    JTextField idField, nameField, marksField;

    public StudentGUI() {
        setTitle("Student Management System");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---- Top form panel ----
        JPanel formPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));

        idField = new JTextField();
        nameField = new JTextField();
        marksField = new JTextField();

        formPanel.add(new JLabel("ID:"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Marks:"));
        formPanel.add(idField);
        formPanel.add(nameField);
        formPanel.add(marksField);

        // ---- Buttons panel ----
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update Marks");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search by ID");
        JButton sortBtn = new JButton("Sort by Marks");
        JButton refreshBtn = new JButton("Refresh/View All");

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(sortBtn);
        buttonPanel.add(refreshBtn);

        // ---- Table ----
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Marks"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Auto-fill fields when a row is clicked (only when a single row is selected)
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRows().length == 1) {
                int row = table.getSelectedRow();
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                marksField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        // ---- Layout ----
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // ---- Button actions ----
        addBtn.addActionListener(this::addStudent);
        updateBtn.addActionListener(this::updateMarks);
        deleteBtn.addActionListener(this::deleteStudent);
        searchBtn.addActionListener(this::searchStudent);
        sortBtn.addActionListener(this::sortStudents);
        refreshBtn.addActionListener(e -> refreshTable());
    }

    // Add student
    void addStudent(ActionEvent e) {
        try {
            if (count >= students.length) {
                JOptionPane.showMessageDialog(this, "Student list is full!");
                return;
            }
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            double marks = Double.parseDouble(marksField.getText().trim());

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name cannot be empty.");
                return;
            }

            students[count] = new Student(id, name, marks);
            count++;
            refreshTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Student added successfully.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID and Marks must be numbers.");
        }
    }

    // Update marks
    void updateMarks(ActionEvent e) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            double newMarks = Double.parseDouble(marksField.getText().trim());

            for (int i = 0; i < count; i++) {
                if (students[i].getId() == id) {
                    students[i].setMarks(newMarks);
                    refreshTable();
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Marks updated successfully.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid ID and Marks.");
        }
    }

    // Delete student(s) - supports multiple selected rows
    void deleteStudent(ActionEvent e) {
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Select at least one student to delete.");
            return;
        }

        // Collect the IDs of selected rows first
        int[] idsToDelete = new int[selectedRows.length];
        for (int i = 0; i < selectedRows.length; i++) {
            idsToDelete[i] = (int) tableModel.getValueAt(selectedRows[i], 0);
        }

        // Delete each by ID
        for (int id : idsToDelete) {
            for (int i = 0; i < count; i++) {
                if (students[i].getId() == id) {
                    for (int j = i; j < count - 1; j++) {
                        students[j] = students[j + 1];
                    }
                    students[count - 1] = null;
                    count--;
                    break;
                }
            }
        }

        refreshTable();
        clearFields();
        JOptionPane.showMessageDialog(this, "Selected student(s) deleted successfully.");
    }

    // Search student by ID (Linear Search)
    void searchStudent(ActionEvent e) {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            for (int i = 0; i < count; i++) {
                if (students[i].getId() == id) {
                    tableModel.setRowCount(0);
                    tableModel.addRow(new Object[]{
                            students[i].getId(), students[i].getName(), students[i].getMarks()
                    });
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Student not found.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid ID.");
        }
    }

    // Sort students by marks (Bubble Sort, descending)
    void sortStudents(ActionEvent e) {
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (students[j].getMarks() < students[j + 1].getMarks()) {
                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;
                }
            }
        }
        refreshTable();
    }

    // Refresh table to show all students
    void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < count; i++) {
            tableModel.addRow(new Object[]{
                    students[i].getId(), students[i].getName(), students[i].getMarks()
            });
        }
    }

    void clearFields() {
        idField.setText("");
        nameField.setText("");
        marksField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGUI().setVisible(true));
    }
}