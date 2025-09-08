# Excel Reconciliation Engine

A Java desktop application for visually reconciling data between two Excel files. This tool provides a user-friendly interface to select files, map columns dynamically, and generate a detailed reconciliation report in Excel format.

## Features

- **Swing UI**: A simple and intuitive graphical user interface for desktop use.
- **Dynamic Column Mapping**: Select which columns to compare directly from the UI. The application reads headers dynamically, so no hardcoding is needed.
- **Flexible Reconciliation Logic**:
    - Compares a single attribute column from Excel 1 against multiple attribute columns in Excel 2.
    - Matches many-to-one for "Report" and "Product" columns.
    - All string comparisons are case-insensitive.
- **Rich Excel Reports**: Generates a `.xlsx` report with two sheets:
    - **Summary Sheet**: High-level statistics of the reconciliation (passes, fails, mismatches).
    - **Detailed Sheet**: A row-by-row breakdown of every comparison, with PASS/FAIL results color-coded for easy analysis.

## Prerequisites

To build and run this project, you will need:
- **Java Development Kit (JDK)**: Version 8 or higher.
- **Apache Maven**: To manage dependencies and build the project.

## How to Build

1.  **Clone the repository or download the source code.**

2.  **Open a terminal or command prompt** and navigate to the root directory of the project (where the `pom.xml` file is located).

3.  **Run the Maven build command:**
    ```sh
    mvn clean install
    ```
    This command will compile the source code, run the unit tests, and package the application into a single, runnable JAR file in the `target` directory.

## How to Run

Once the build is complete, you can run the application using the following command:

```sh
java -jar target/recon-engine-1.0-SNAPSHOT-jar-with-dependencies.jar
```

This will launch the "Excel Reconciliation Engine" GUI. From there, you can select your two Excel files, map the columns you wish to reconcile, and click "Run Reconciliation" to generate the report. The output file, `Recon_Report.xlsx`, will be created in the same directory where you ran the command.
