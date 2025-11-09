import java.time.Instant;
import java.util.*;


class CategoryNotFoundException extends Exception{
    String message;

    CategoryNotFoundException(String cat){
        message = "Category " + cat + " was not found";
    }

    public String getMessage(){
        return message;
    }
    public void message(){
        System.out.println(message);
    }
}
class Category{
    private String category_name;
    Category(String category_name){
        this.category_name = category_name;
    }
    public String getCategory(){
        return category_name;
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Category)) return false;
        return category_name.equals(((Category) o).category_name);
    }
}


abstract class NewsItem{
    private String title;
    private Date publishedDate;
    private Category category;
    NewsItem(String title, Date publishedDate, Category category){
        this.title = title;
        this.publishedDate = publishedDate;
        this.category = category;
    }
    public Category getCategory(){
        return category;
    }
    public Date getPublishedDate(){
        return publishedDate;
    }
    public String getTitle(){
        return title;
    }
    abstract public String getTeaser();
}

class TextNewsItem extends NewsItem{
    String text;
    TextNewsItem(String title, Date publishedDate, Category category, String text){
        super(title, publishedDate, category);
        this.text = text;
    }
    @Override
    public String getTeaser(){
        long publishedAt = this.getPublishedDate().getTime();
        long currTime = Date.from(Instant.now()).getTime();

        int milliSeconds = (int) (publishedAt - currTime);
        int minutes = milliSeconds / 60000;
        int max = Math.min(text.length(), 80);
        return this.getTitle() + "\n" + Math.abs(minutes) + "\n" + text.substring(0,max);
    }
}

class MediaNewsItem extends NewsItem{
    private String url;
    private int views;
    MediaNewsItem(String title, Date publishedDate, Category category, String url, int views){
        super(title, publishedDate, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        long publishedAt = this.getPublishedDate().getTime();
        long currTime = Date.from(Instant.now()).getTime();

        int milliSeconds = (int) (publishedAt - currTime);
        int minutes = milliSeconds / 60000;

        return this.getTitle() + "\n" + Math.abs(minutes) + "\n" + url + "\n" + views;
    }
}

class FrontPage{
    private List<NewsItem> news;
    private List<Category> categories;
    FrontPage(Category[] categories){
        this.categories = new ArrayList<>();
        this.news = new ArrayList<>();
        this.categories.addAll(Arrays.asList(categories));
    }

    void addNewsItem(NewsItem newsItem){
        news.add(newsItem);
    }
    public List<NewsItem> listByCategory(Category category){
        List<NewsItem> list = new ArrayList<>();
        for(NewsItem n : news){
            if(n.getCategory().equals(category))list.add(n);
        }
        return list;
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException{
        List<NewsItem> list = new ArrayList<>();
        Category c = new Category(category);
        if(!categories.contains(c)) throw new CategoryNotFoundException(category);
        for(NewsItem n : news){
            if(n.getCategory().equals(c)) list.add(n);
        }
        return list;
    }

    @Override
    public String toString() {
        String out = "";
        for(NewsItem n : news){
            out += n.getTeaser() + "\n";
        }
        return out;
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde