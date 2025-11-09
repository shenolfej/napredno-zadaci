import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

class NonExistingItemException extends Throwable{
    String message;
    NonExistingItemException(int id){
        this.message = "Item with id "+ id +" doesn't exist";
    }
    public String getMessage(){
        return message;
    }
}

abstract class Archive{
    private int id;
    private LocalDate dateArchived;

    Archive(int id){
        this.id = id;
        this.dateArchived = null;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
    public LocalDate getDateArchived(){
        return dateArchived;
    }
    public int getId(){
        return id;
    }
}

class LockedArchive extends Archive{
    private LocalDate dateToOpen;

    LockedArchive(int id, LocalDate dateToOpen){
        super(id);
        this.dateToOpen = dateToOpen;
    }
    public LocalDate getDateToOpen(){
        return dateToOpen;
    }
}

class SpecialArchive extends Archive{
    private int maxOpen;
    private int timesOpened;
    SpecialArchive(int id, int maxOpen){
        super(id);
        this.maxOpen = maxOpen;
        this.timesOpened = 0;
    }
    public void incrementOpened(){
        timesOpened++;
    }
    public int getMaxOpen(){
        return maxOpen;
    }
    public int getTimesOpened(){
        return timesOpened;
    }
}

class ArchiveStore{
    private ArrayList<Archive> archives = new ArrayList<>();
    private String log;
    ArchiveStore(){
        archives = new ArrayList<>();
        log = "";
    }
    void archiveItem(Archive item, LocalDate date){
        item.setDateArchived(date);
        archives.add(item);
        log += "Item " + item.getId() + " archived at " + date + "\n";
    }

    void openItem(int id, LocalDate date) throws NonExistingItemException{
        for(Archive a : archives){
            if(id == a.getId()){
                if(a instanceof LockedArchive){
                    LockedArchive la = (LockedArchive) a;
                    if(date.isBefore(la.getDateToOpen())){
                        log += "Item " + la.getId() + " cannot be opened before " + la.getDateToOpen() + "\n";
                    }else{
                        log += "Item " + la.getId() + " opened at " + date + "\n";
                    }
                    return;
                }
                else if(a instanceof SpecialArchive){
                    SpecialArchive sc = (SpecialArchive) a;
                    if(sc.getTimesOpened() < sc.getMaxOpen()){
                        log += "Item " + sc.getId() + " opened at " + date + "\n";
                        sc.incrementOpened();
                    }
                    else{
                        log += "Item " + sc.getId() + " cannot be opened more than " + sc.getMaxOpen() + " times" + "\n";
                    }
                }

                return;
            }
        }
        throw new NonExistingItemException(id);
    }
    public String getLog(){
        return log;
    }
}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}