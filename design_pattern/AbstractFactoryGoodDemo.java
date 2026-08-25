// // Save as: FactoryDataDemo.java (Run via: java FactoryDataDemo.java)
// // 1. Abstract Products
// interface Connection { void connect(); }
// interface Encryptor { String encrypt(String data); }
// // 2. Abstract Factory Interface
// interface CloudPipelineFactory {
// Connection createConnection();
// Encryptor createEncryptor();
// }
// // 3. Concrete Variant A: AWS Stack
// class AwsConnection implements Connection { public void connect() {
// System.out.println("☁️ Connected to AWS S3"); } }
// class AwsEncryptor implements Encryptor { public String encrypt(String d) {
// return "[KMS-Encrypted: " + d + "]"; } }
// class AwsPipelineFactory implements CloudPipelineFactory {
// public Connection createConnection() { return new AwsConnection(); }
// public Encryptor createEncryptor() { return new AwsEncryptor(); }
// }
// // 4. Concrete Variant B: Azure Stack
// class AzureConnection implements Connection {
// public void connect() { System.out.println("🔷 Connected to Azure Blob"); }
// }
// class AzureEncryptor implements Encryptor {
// public String encrypt(String d) { return "[KeyVault-Encrypted: " + d + "]"; }
// }
// class AzurePipelineFactory implements CloudPipelineFactory {
// public Connection createConnection() { return new AzureConnection(); }
// public Encryptor createEncryptor() { return new AzureEncryptor(); }
// }
// // Main Runner
// public class AbstractFactoryGoodDemo {
// public static void main(String[] args) {
// // Simulated Environment Config: Can change at runtime
// String targetCloud = "AWS";
// // Java 21 Switch Expression to cleanly select the factory configuration
// // ONCE
// CloudPipelineFactory factory = switch (targetCloud.toUpperCase()) {
// case "AWS" -> new AwsPipelineFactory();
// case "AZURE" -> new AzurePipelineFactory();
// default -> throw new IllegalArgumentException("Unknown cloud environment: " +
// targetCloud);
// };
// // HIGH-VALUE BENEFIT: The actual business logic below doesn't care if it's
// // AWS or Azure!
// // It blindly works with the abstract factory interface, avoiding endless
// // if/else checks.
// Connection conn = factory.createConnection();
// Encryptor enc = factory.createEncryptor();
// conn.connect();
// System.out.println(enc.encrypt("Sensitive Application Data Log"));
// }
// }
