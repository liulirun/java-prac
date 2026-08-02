import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class stream_threelevels_medium1 {

  // Level 3: Lowest Leaf Node
  // Renamed 'name' to 'projectName'
  record Project(String projectName, String status) {
  }

  // Level 2: Middle Node
  // Renamed 'name' to 'departmentName'
  record Department(String departmentName, List<Project> projects) {
    public Department(String departmentName, Project... projects) {
      this(departmentName, List.of(projects));
    }
  }

  // Level 1: Root Node
  // Renamed 'name' to 'companyName'
  record Company(String companyName, String industry, List<Department> departments) {
    public Company(String companyName, String industry, Department... departments) {
      this(companyName, industry, List.of(departments));
    }
  }

  public static List<Company> getMockCompanies() {
    return List.of(
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
  }

  public static void main(String[] args) {
    List<Company> companies = getMockCompanies();

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
        .map(c -> c.companyName) // Updated getter
        .collect(Collectors.toList());

    System.out.println("Result: " + names);
  }

  // =========================================================================
  // LEVEL 2 FILTER: Filter parents based on a property inside their children
  // =========================================================================
  static void filterSecondLevel(List<Company> companies) {
    System.out.println("\n--- 2. FILTER BY LEVEL 2: Companies with an 'Engineering' Department ---");

    List<String> targetDepts = Arrays.asList("Engineering", "Analytics");

    List<String> names = companies.stream()
        .filter(c -> c.departments.stream().anyMatch(
            d -> targetDepts.stream().anyMatch(
                t -> d.departmentName.contains(t)))) // Updated getter
        .map(c -> c.companyName) // Updated getter
        .collect(Collectors.toList());

    System.out.println("Result: " + names);
  }

  // =========================================================================
  // LEVEL 3 FILTER (Option A): Match top-level parents based on leaf criteria
  // =========================================================================
  static void filterThirdLevelKeepingParents(List<Company> companies) {
    System.out.println("\n--- 3A. FILTER BY LEVEL 3: Companies that have ANY 'Active' Project ---");

    List<String> names = companies.stream()
        .filter(c -> c.departments.stream()
            .flatMap(d -> d.projects.stream())
            .allMatch(p -> "Active".equals(p.status)))
        .map(c -> c.companyName) // Updated getter
        .collect(Collectors.toList());

    System.out.println("Result: " + names);
  }

  // =========================================================================
  // NEW FUNCTION: Extract Department names based on Level 3 Project criteria
  // =========================================================================
  static void filterThirdLevelGetDepartments(List<Company> companies) {
    System.out.println("\n--- 3C. FILTER BY LEVEL 3: Departments that have ANY 'Active' Project ---");

    List<String> deptNames = companies.stream()
        .flatMap(c -> c.departments.stream())
        .filter(d -> d.projects.stream()
            .anyMatch(p -> "Active".equals(p.status)))
        .map(d -> d.departmentName) // Updated getter
        .collect(Collectors.toList());
    System.out.println("Result: " + deptNames);
  }

  // =========================================================================
  // LEVEL 3 FILTER (Option B): Destroy the hierarchy and pull raw leaves out
  // =========================================================================
  static void flattenAndExtractThirdLevel(List<Company> companies) {
    System.out.println("\n--- 3B. FILTER BY LEVEL 3: Extract names of ALL 'Completed' Projects ---");

    List<String> completedProjectNames = companies.stream()
        .flatMap(c -> c.departments.stream())
        .flatMap(d -> d.projects.stream())
        .filter(p -> "Completed".equals(p.status))
        .map(p -> p.projectName) // Updated getter
        .collect(Collectors.toList());

    System.out.println("Result: " + completedProjectNames);
  }
}
