// //need to fix dev-container for this
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;

// // Make sure Jackson core/databind dependencies are added to your project classpath
// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;

// public class stream_bank_account_summary {

//     // --- JSON LAYER 5 (Leaf): Account Properties ---
//     static class Account {
//         public String key;
//         public String status;
//         public String ownership_type;
//         public String resource;

//         @Override
//         public String toString() {
//             return String.format("Account{key='%s', ownership='%s'}", key, ownership_type);
//         }
//     }

//     // --- JSON LAYER 4: Product Category Wrappers ---
//     static class Banking {
//         public List<Account> accounts;
//     }

//     static class CreditCard {
//         public List<Account> accounts;
//     }

//     static class InsideAccounts {
//         public Banking banking;
//         public CreditCard credit_card;
//     }

//     // --- JSON LAYER 3: Entity Segments ---
//     static class Personal { public InsideAccounts accounts; }
//     static class Business { public InsideAccounts accounts; }

//     // --- JSON LAYER 2: Nested Accounts Root Node ---
//     static class AccountsRoot {
//         public Personal personal;
//         public Business business;
//     }

//     // --- JSON LAYER 1 (Top Wrapper): Matches {"data": {"accounts": ...}} ---
//     static class DataWrapper {
//         public AccountsRoot accounts;
//     }

//     static class ResponseRoot {
//         public DataWrapper data;
//     }

//     public static void main(String[] args) {
//         String fileName = "333444_V4_ACCOUNTS_SUMMARY.json";
        
//         try {
//             System.out.println("Reading data stream from file: " + fileName);
//             // 1. Read the raw text strings out of the file in the workspace directory
//             String rawJsonContent = Files.readString(Paths.get(fileName));

//             // 2. Initialize Jackson ObjectMapper to map strings directly to instances
//             ObjectMapper mapper = new ObjectMapper();
//             // Ignore extra properties like "total" or "business_functions" so code stays compact
//             mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//             // 3. Deserialize JSON string into object tree structure
//             ResponseRoot response = mapper.readValue(rawJsonContent, ResponseRoot.class);

//             if (response != null && response.data != null && response.data.accounts != null) {
//                 // 4. Pass the mapped Java instance node right into our stream filter
//                 List<Account> finalAccounts = getRetailBankingAccounts(response.data.accounts);
//                 System.out.println("\n--- FINAL RETURNED LIST ---\n" + finalAccounts);
//             } else {
//                 System.out.println("Failed to parse valid object architecture from JSON source.");
//             }

//         } catch (IOException e) {
//             System.err.println("Fatal Error: Could not read file " + fileName + ". Check path locations.");
//             e.printStackTrace();
//         }
//     }

//     static List<Account> getRetailBankingAccounts(AccountsRoot root) {
//         return Arrays.asList(root.personal, root.business).stream()
//             .filter(node -> node != null && node.accounts != null)
//             .peek(node -> System.out.println("[JSON Node Check] Parsing: " + node.getClass().getSimpleName()))
//             .map(node -> node.accounts.banking)
//             .filter(bNode -> bNode != null && bNode.accounts != null)
//             .peek(bNode -> System.out.println("  ↳ [Property Check] Looking at 'banking' branch..."))
//             .flatMap(bNode -> bNode.accounts.stream())
//             .peek(acc -> System.out.println("    ↳ [Field Test] Checking key: " + acc.key + " | Ownership: " + acc.ownership_type))
//             .filter(acc -> "RETAIL".equals(acc.ownership_type))
//             .collect(Collectors.toList());
//     }
// }
