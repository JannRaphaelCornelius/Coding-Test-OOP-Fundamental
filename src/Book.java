import java.time.LocalDate;

class Book {
    private static int totalBooks = 0;
    private static final String[] VALID_CATEGORIES = {"Fiction", "Non-Fiction", "Science", "Technology", "History"};
    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    private String bookId;
    private String title;
    private String author;
    private String category;
    private int publicationYear;
    private int totalCopies;
    private int availableCopies;

    public Book() {
        totalBooks++;
        this.bookId = String.format("BK%03d", totalBooks);
    }

    public Book(String title, String author, String category, int publicationYear, int totalCopies) {
        this();
        // Setter dipanggil untuk validasi dan penentuan nilai awal
        setTitle(title);
        setAuthor(author);
        setCategory(category);
        setPublicationYear(publicationYear);
        setTotalCopies(totalCopies);
        this.availableCopies = this.totalCopies;
    }

    // --- Getters ---
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public int getPublicationYear() { return publicationYear; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public static int getTotalBooks() { return totalBooks; }
    public int getBookAge() { return CURRENT_YEAR - publicationYear; }
    public boolean isNewRelease() { return getBookAge() <= 7; }

    // --- Setters (untuk validasi) ---
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) {
        boolean valid = false;
        for (String cat : VALID_CATEGORIES) {
            if (cat.equalsIgnoreCase(category)) {
                valid = true;
                this.category = cat;
                break;
            }
        }
        if (!valid) {
            throw new IllegalArgumentException("Validation Error: Kategori harus salah satu dari: Fiction, Non-Fiction, Science, Technology, History.");
        }
    }
    public void setPublicationYear(int year) {
        if (year < 1900 || year > CURRENT_YEAR) {
            throw new IllegalArgumentException("Validation Error: Tahun terbit tidak valid (1900-" + CURRENT_YEAR + ")");
        }
        this.publicationYear = year;
    }
    public void setTotalCopies(int totalCopies) {
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Validation Error: Total eksemplar tidak boleh negatif.");
        }
        this.totalCopies = totalCopies;
        if (this.availableCopies > totalCopies) {
            this.availableCopies = totalCopies;
        }
    }

    // --- Methods ---
    public String getAvailabilityStatus() {
        double ratio = (double) availableCopies / totalCopies;

        if (availableCopies == 0) return "Habis";
        if (ratio > 0.5) return "Banyak Tersedia";
        return "Terbatas";
    }

    public void displayBookInfo(String separator) {
        String availability = getAvailabilityStatus();
        String statusIndicator = "";

        if (availability.contains("Banyak")) {
            statusIndicator = "✓";
        } else if (availability.contains("Terbatas") || availability.contains("Habis")) {
            statusIndicator = "⚠️";
        }
        String newReleaseTag = isNewRelease() && availableCopies > 0 ? " [NEW RELEASE]" : "";
        String availableInfo = (availableCopies == 0) ? "0 eksemplar" : String.format("%d eksemplar", availableCopies);

        System.out.printf("[%s] %s%n", bookId, title);
        System.out.printf("Penulis       : %s%n", author);
        System.out.printf("Kategori      : %s%n", category);
        System.out.printf("Tahun Terbit  : %d%n", publicationYear);
        System.out.printf("Umur Buku     : %d tahun%n", getBookAge());
        System.out.printf("Total Copy    : %d eksemplar%n", totalCopies);
        System.out.printf("Tersedia      : %s | Status: %s %s%s%n",
                availableInfo, availability, statusIndicator.trim(), newReleaseTag);
        System.out.println(separator);
    }

    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
}
