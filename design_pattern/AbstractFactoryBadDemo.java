// Save as: BadDataDemo.java (Run via: java BadDataDemo.java)
// Concrete Classes for AWS
class AwsConnection { public void connect() { System.out.println("☁️ Connected to AWS S3"); } }

class AwsEncryptor { public String encrypt(String d) { return "[KMS-Encrypted: " + d + "]"; } }

// Concrete Classes for Azure
class AzureConnection { public void connect() { System.out.println("🔷 Connected to Azure Blob"); } }

class AzureEncryptor { public String encrypt(String d) { return "[KeyVault-Encrypted: " + d + "]"; } }

public class AbstractFactoryBadDemo {
  public static void main(String[] args) {
    String targetCloud = "AWS"; // Environment configuration
    String data = "Sensitive Application Data Log";
    // 🔴 PROBLEM 1: Extreme Code Duplication & Messy Logic
    // If you need to do this in 10 different places in your app, you must
    // copy-paste this entire block.
    // 🔴 PROBLEM 2: Whenever you need this, you have to do copy/paste for
    // if/elese.
    // because no object can hold this if/else object
    if (targetCloud.equalsIgnoreCase("AWS")) {
      AwsConnection conn = new AwsConnection();
      AwsEncryptor enc = new AwsEncryptor();
      conn.connect();
      System.out.println(enc.encrypt(data));
    } else if (targetCloud.equalsIgnoreCase("AZURE")) {
      AzureConnection conn = new AzureConnection();
      AzureEncryptor enc = new AzureEncryptor();
      conn.connect();
      System.out.println(enc.encrypt(data));
    } else {
      throw new IllegalArgumentException("Unknown cloud: " + targetCloud);
    }
    // 🔴 PROBLEM 3: High Risk of Mixing Components
    // Because there is no factory enforcing a family, a developer could easily
    // make a typo like this:
    // AwsConnection conn = new AwsConnection();
    // AzureEncryptor enc = new AzureEncryptor(); // Worst nightmare: sending
    // AWS data to Azure tools!
  }
}
