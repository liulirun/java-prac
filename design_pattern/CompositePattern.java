// WHEN TO USE: Use this when your system has parts made of groups, and groups made of smaller parts, 
// and you want to process single independent units and groups of units exactly the same way.
//
// HOW IT WORKS: Both individual components (leaves) and collections (composites) implement 
// a shared interface. The collection's method loops through its children to do the total work.

import java.util.ArrayList;
import java.util.List;

interface FileSystemNode {
  void printName(String spacing);
}

class FileNode implements FileSystemNode {
  private String name;

  public FileNode(String name) {
    this.name = name;
  }

  @Override
  public void printName(String spacing) {
    System.out.println(spacing + "- File: " + name);
  }
}

class FolderNode implements FileSystemNode {
  private String name;
  private List<FileSystemNode> children = new ArrayList<>();

  public FolderNode(String name) {
    this.name = name;
  }

  public void add(FileSystemNode node) {
    children.add(node);
  }

  @Override
  public void printName(String spacing) {
    System.out.println(spacing + "+ Folder: " + name);
    for (FileSystemNode child : children) {
      child.printName(spacing + "  ");
    }
  }
}

public class CompositePattern {
  public static void main(String[] args) {
    System.out.println("--- Running Composite Pattern ---");

    FolderNode root = new FolderNode("RootDirectory");
    FolderNode documents = new FolderNode("Documents");

    root.add(new FileNode("config.txt"));
    documents.add(new FileNode("resume.pdf"));
    documents.add(new FileNode("notes.md"));
    root.add(documents);

    // Root treats individual files and subfolders identically!
    root.printName("");
  }
}
