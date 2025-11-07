import java.util.ArrayList;
import java.util.Scanner;

class FileNameExistsException extends Throwable {
    String message;

    FileNameExistsException(){
        this.message = "File with that name already exists!";
    }
    FileNameExistsException(String filename, String foldername){
        this.message = "There is already a file named " + filename + " in the folder " + foldername;
    }
    void message(){
        System.out.println(message);
    }
    public String getMessage(){
        return message;
    }
}

interface IFile{
    String getFileName();
    long getFileSize();
    String getFileInfo(String padding);
    void sortBySize();
    long findLargestFile();
}

class File implements IFile{
    String name;
    long size;
    File(String name, long size){
        this.name = name;
        this.size = size;
    }
    public String getFileName(){ return name;}
    public long getFileSize(){ return size;}
    public String getFileInfo(String padding){
        return String.format(
            "%sFile name:%12s File size:%10d%n",
            padding, name, size);
    }
    public void sortBySize(){}
    public long findLargestFile() {return size;}
    @Override
    public String toString(){
        return getFileInfo("");
    }
}

class Folder implements IFile{
    String name;
    long size;
    ArrayList<IFile> files;

    Folder(String name){
        this.name = name;
        this.size = 0;
        files = new ArrayList<>();
    }

    public String getFileName(){ return name;}
    public long getFileSize(){
        return size;
    }
    public String getFileInfo(String padding){
        String s = String.format(
                "%sFile name:%12s File size:%10d%n",
                padding, name, size);
        padding += "    ";
        for (IFile f : files) s += f.getFileInfo(padding);
        return s;
    }
    public void sortBySize(){
        for(int i = 0; i<files.size(); i++){
            for(int j = 0; j<files.size()-1; j++){
                if(files.get(j).getFileSize() > files.get(j+1).getFileSize()){
                    IFile tmp = files.get(j);
                    files.set(j, files.get(j+1));
                    files.set(j+1, tmp);
                }
            }
            if(files.get(i) instanceof Folder) files.get(i).sortBySize();
        }
    }
    public long findLargestFile() {
        long max = -1;
        for (IFile f : files){
            if(f instanceof File){
                if(f.getFileSize() > max) max = f.getFileSize();
            }else{
                long val = f.findLargestFile();
                if(val > max) max = val;
            }
        }
        return max;
    }
    void addFile(IFile file) throws FileNameExistsException{
        for (IFile f : files) {
            if (f.getFileName().equals(file.getFileName())) {
                throw new FileNameExistsException(file.getFileName(), this.name);
            }
        }
        files.add(file);
        this.size += file.getFileSize();
    }
    @Override
    public String toString(){
        return getFileInfo("");
    }
}

class FileSystem{
    Folder rootDirectory;

    FileSystem(){
        rootDirectory = new Folder("root");
    }

    void addFile(IFile file) throws FileNameExistsException{
        rootDirectory.addFile(file);
    }

    long findLargestFile(){
        return rootDirectory.findLargestFile();
    }
    public void sortBySize(){
        rootDirectory.sortBySize();
    }
    @Override
    public String toString(){
        return rootDirectory.getFileInfo("");
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}