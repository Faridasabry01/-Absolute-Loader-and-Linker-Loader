# Absolute Loader and Linker Loader Simulation

## Description This project simulates the **Absolute Loader** and
**Linker Loader** functionalities for SIC and SIC/XE systems. It
allows users to visualize memory allocation and management through an
interactive GUI built with JavaFX.

---

## Features - Memory simulation for SIC and SIC/XE architectures. -
Parsing and processing of `H`, `T`, and `M` records from object
code. - Relocation and linking of multiple programs using external
symbols. - Interactive JavaFX-based GUI for visualization. - External
symbol table generation and memory modification handling.

---

## Prerequisites - Java JDK 11 or higher - JavaFX SDK - Maven (for building the project)

---

## Setup and Usage

### Steps to Run:
1. Clone the repository: 
2. Import the project into your preferred IDE (IntelliJ IDEA recommended).
3. Add the JavaFX SDK to the project
configuration.
4. Place the input file (`in.txt`) in the project's
root directory.
5. Build and run the project
6. Use the GUI to load and view memory for
SIC and SIC/XE programs.

---

## Input File Format The `in.txt` file should contain object code in
the following format: - **Header Record (`H`)**: Program name,
starting address, and length. - **Text Record (`T`)**:
Instructions to load into memory. - **Modification Record (`M`)**:
Details of memory modifications. - **End Record (`E`)**: Marks the
end of the program.

Example: ``` H^TEST^001000^0017A0 T^001000^1E^14103328103C
M^00100C^05^+LEN E^001000 ```

---

## GUI Features - 
**Main Menu**: Navigate between SIC and SIC/XE modes. 
**SIC Mode**: Simulate absolute loading for SIC systems. 
**SIC/XE Mode**: Handle relocation and linking for SIC/XE systems with external symbols.

