import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class stream_threelevels_medium1 {

  // Level 3: Lowest Leaf Node
  static class Project {
    String name;
    String status; // "Active", "Completed"

    Project(String name, String status) {
      this.name = name;
      this.status = status;
    }
  }

  // Level 2: Middle Node
  static class Department {
    String name;
    List<Project> projects;

    Department(String name, Project... projects) {
      this.name = name;
      this.projects = Arrays.asList(projects);
    }
  }

  // Level 1: Root Node
  static class Company {
    String name;
    String industry;
    List<Department> departments;

    Company(String name, String industry, Department... departments) {
      this.name = name;
      this.industry = industry;
      this.departments = Arrays.asList(departments);
    }
  }

  public static void main(String[] args) {
    // Setup 3-Level Deep Hierarchical Mock Data
    List<Company> companies = Arrays.asList(
        new Company("TechCorp", "Tech",
            new Department("Engineering",
                new Project("Cloud Migration", "Active"),
                new Project("Legacy Refactor", "Completed")),
            new Department("HR",
                new Project("Hiring Pipeline", "Active"))),
        new Company("HealthInc", "Healthcare",
            new Department("Research",
                new Project("Vaccine Study", "Completed"))),
        new Company("FinancePro", "Finance",
            new Department("Analytics",
                new Project("Risk Engine", "Active"))));

    // filterFirstLevel(companies);
    filterSecondLevel(companies);
    filterThirdLevelGetDepartments(companies);
    // filterThirdLevelKeepingParents(companies);
    // flattenAndExtractThirdLevel(companies);
  }

  // =========================================================================
  // LEVEL 1 FILTER: Filter by properties directly on the root object
  // =========================================================================
  static void filterFirstLevel(List<Company> companies) {
    System.out.println("--- 1. FILTER BY LEVEL 1: Tech Companies Only ---");

    List<String> names = companies.stream()
        .filter(c -> "Tech".equals(c.industry))
        .map(c -> c.name)
        .collect(Collectors.toList());

    System.out.println("Result: " + names); // [TechCorp]
  }

  // =========================================================================
  // LEVEL 2 FILTER: Filter parents based on a property inside their children
  // =========================================================================
  static void filterSecondLevel(List<Company> companies) {
    System.out.println("\n--- 2. FILTER BY LEVEL 2: Companies with an 'Engineering' Department ---");

    // Mock target departments for this execution context
    List<String> targetDepts = Arrays.asList("Engineering", "Analytics");

    List<String> names = companies.stream()
        .filter(c -> c.departments.stream().anyMatch(
            d -> targetDepts.stream().anyMatch(
                t -> d.name.contains(t))))
        .map(c -> c.name)
        .collect(Collectors.toList());

    System.out.println("Result: " + names); // [TechCorp]
  }

  // =========================================================================
  // LEVEL 3 FILTER (Option A): Match top-level parents based on leaf criteria
  // =========================================================================
  static void filterThirdLevelKeepingParents(List<Company> companies) {
    System.out.println("\n--- 3A. FILTER BY LEVEL 3: Companies that have ANY 'Active' Project ---");

    List<String> names = companies.stream()
        .filter(c -> c.departments.stream()
            .flatMap(d -> d.projects.stream()) // Flattens all nested projects into a single stream
            .allMatch(p -> "Active".equals(p.status)))
        .map(c -> c.name)
        .collect(Collectors.toList());

    System.out.println("Result: " + names); // [TechCorp, FinancePro]
  }

  // =========================================================================
  // NEW FUNCTION: Extract Department names based on Level 3 Project criteria
  // =========================================================================
  static void filterThirdLevelGetDepartments(List<Company> companies) {
    System.out.println("\n--- 3C. FILTER BY LEVEL 3: Departments that have ANY 'Active' Project ---");

    List<String> deptNames = companies.stream()
        // 1. Break the Company shell and stream all Departments directly
        .flatMap(c -> c.departments.stream())
        // 2. Now 'd' represents a Department. Look inside its projects.
        .filter(d -> d.projects.stream()
            .anyMatch(p -> "Active".equals(p.status)))
        // 3. Extract the Department name
        .map(d -> d.name)
        .collect(Collectors.toList());
    System.out.println("Result: " + deptNames); // [Engineering, HR, Analytics]
  }

  // =========================================================================
  // LEVEL 3 FILTER (Option B): Destroy the hierarchy and pull raw leaves out
  // =========================================================================
  static void flattenAndExtractThirdLevel(List<Company> companies) {
    System.out.println("\n--- 3B. FILTER BY LEVEL 3: Extract names of ALL 'Completed' Projects ---");

    List<String> completedProjectNames = companies.stream()
        .flatMap(c -> c.departments.stream()) // Drop Level 1 -> Stream of Departments
        .flatMap(d -> d.projects.stream()) // Drop Level 2 -> Stream of Projects
        .filter(p -> "Completed".equals(p.status))
        .map(p -> p.name)
        .collect(Collectors.toList());

    System.out.println("Result: " + completedProjectNames); // [Legacy Refactor, Vaccine Study]
  }
}
