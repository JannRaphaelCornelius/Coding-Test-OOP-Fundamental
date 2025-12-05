import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

class Transaction {
    private static int totalTransactions = 0;
    public static final double LATE_FEE_PER_DAY = 2000.0;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final int DEFAULT_BORROW_DAYS = 14;

    private String transactionId;
    private Member member;
    private Book book;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private int daysLate;
    private double lateFee;

    public Transaction(Member member, Book book, String borrowDate, int borrowDurationDays) {
        totalTransactions++;
        this.transactionId = String.format("TRX%03d", totalTransactions);
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;

        LocalDate borrowDt = LocalDate.parse(this.borrowDate, DATE_FORMATTER);
        LocalDate dueDt = borrowDt.plusDays(borrowDurationDays);
        this.dueDate = dueDt.format(DATE_FORMATTER);

        this.returnDate = null;
        this.daysLate = 0;
        this.lateFee = 0.0;
    }

    // --- Getters ---
    public boolean isCompleted() { return this.returnDate != null; }
    public static int getTotalTransactions() { return totalTransactions; }
    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public double getLateFee() { return lateFee; }
    public String getTransactionId() { return transactionId; }
    public String getBorrowDate() { return borrowDate; }
    public String getDueDate() { return dueDate; }
    public int getDaysLate() { return daysLate; }
    public String getReturnDate() { return returnDate; }


    // --- Logika Pengembalian ---
    public double processReturn(String returnDateStr) {
        this.returnDate = returnDateStr;
        this.book.returnBook();

        try {
            LocalDate returnDt = LocalDate.parse(returnDateStr, DATE_FORMATTER);
            LocalDate dueDt = LocalDate.parse(this.dueDate, DATE_FORMATTER);

            if (returnDt.isAfter(dueDt)) {
                this.daysLate = (int) ChronoUnit.DAYS.between(dueDt, returnDt);
            } else {
                this.daysLate = 0;
            }

            double memberDiscount = this.member.getMembershipDiscount();
            double feeBeforeDiscount = this.daysLate * LATE_FEE_PER_DAY;
            this.lateFee = feeBeforeDiscount * (1 - memberDiscount);

            return this.lateFee;

        } catch (DateTimeParseException e) {
            return 0.0;
        }
    }

    public boolean isOverdue(String currentDateStr) {
        if (this.returnDate != null) return false;
        try {
            return LocalDate.parse(currentDateStr, DATE_FORMATTER).isAfter(LocalDate.parse(this.dueDate, DATE_FORMATTER));
        } catch (DateTimeParseException e) { return false; }
    }

    public long getDaysLeft(String currentDateStr) {
        if (this.returnDate != null || isOverdue(currentDateStr)) return 0;
        try {
            return ChronoUnit.DAYS.between(LocalDate.parse(currentDateStr, DATE_FORMATTER), LocalDate.parse(this.dueDate, DATE_FORMATTER));
        } catch (DateTimeParseException e) { return 0; }
    }

    public String getTransactionStatus(String currentDateStr) {
        if (this.returnDate != null) {
            if (this.lateFee > 0) return "SELESAI - TERLAMBAT ⚠️";
            return "SELESAI - TEPAT WAKTU ✓";
        }
        return "AKTIF";
    }

    public void displayTransaction(String currentDateStr, String separator) {
        String status = getTransactionStatus(currentDateStr);
        boolean isCompleted = status.startsWith("SELESAI");

        System.out.printf("[%s] %s%n", transactionId, status);
        System.out.printf("Peminjam      : %s (%s) - %s%n", member.getName(), member.getMemberId(), member.getMembershipType());
        System.out.printf("Buku          : %s (%s)%n", book.getTitle(), book.getBookId());
        System.out.printf("Tgl Pinjam    : %s%n", borrowDate);
        System.out.printf("Tgl Tempo     : %s%n", dueDate);

        if (isCompleted) {
            System.out.printf("Tgl Kembali   : %s%n", returnDate);
            System.out.printf("Terlambat     : %d hari%n", daysLate);

            // Format denda agar sesuai output persis (Rp 20.000 - diskon 10%)
            String dendaInfo = (lateFee > 0) ? String.format(" (Rp %,.0f - diskon %.0f%%)", daysLate * LATE_FEE_PER_DAY, member.getMembershipDiscount() * 100) : "";
            System.out.printf("Denda         : Rp %,.0f%s%n", lateFee, dendaInfo);
        } else {
            // Hitung hari tersisa untuk transaksi aktif
            long daysLeft = getDaysLeft(currentDateStr);
            System.out.printf("Status        : Masih Dipinjam (%d hari lagi)%n", daysLeft);
        }
        System.out.println(separator);
    }
}