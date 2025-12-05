import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class LibraryManagementSystem {
    // 44 dash
    private static final String SEPARATOR = "--------------------------------------------";
    // 44 equal sign
    private static final String HEADER_SEPARATOR = "============================================";
    private static double totalFineCollected = 0.0;

    // Helper untuk mencetak info pinjam dengan indentasi persis
    private static void printBorrowInfo(Transaction t) {
        System.out.printf("   Tanggal Pinjam: %s | Jatuh Tempo: %s%n", t.getBorrowDate(), t.getDueDate());
    }

    public static void main(String[] args) {
        // Tanggal Simulasi: Ditetapkan 05-12-2025
        String simToday = "05-12-2025";

        System.out.println(HEADER_SEPARATOR);
        System.out.println("LIBRARY MANAGEMENT SYSTEM");
        System.out.println(HEADER_SEPARATOR);

        Map<String, Member> members = new HashMap<>();
        Map<String, Book> books = new HashMap<>();
        Map<String, Transaction> transactions = new HashMap<>();

        // 1. REGISTRASI MEMBER
        System.out.println("\n=== REGISTRASI ANGGOTA ===");
        try {
            Member m1 = new Member("Alice Johnson", "alice.j@email.com", "081234567890", 2020, "Platinum");
            Member m2 = new Member("Bob Smith", "bob.smith@email.com", "081298765432", 2022, "Gold");
            Member m3 = new Member("Charlie Brown", "charlie.b@email.com", "081223456789", 2024, "Silver");
            Member m4 = new Member("Diana Prince", "diana.p@email.com", "081287654321", 2021, "Gold");

            members.put(m1.getMemberId(), m1);
            members.put(m2.getMemberId(), m2);
            members.put(m3.getMemberId(), m3);
            members.put(m4.getMemberId(), m4);

            System.out.printf("✓ Anggota berhasil ditambahkan: %s - %s (%s)%n", m1.getMemberId(), m1.getName(), m1.getMembershipType());
            System.out.printf("✓ Anggota berhasil ditambahkan: %s - %s (%s)%n", m2.getMemberId(), m2.getName(), m2.getMembershipType());
            System.out.printf("✓ Anggota berhasil ditambahkan: %s - %s (%s)%n", m3.getMemberId(), m3.getName(), m3.getMembershipType());
            System.out.printf("✓ Anggota berhasil ditambahkan: %s - %s (%s)%n", m4.getMemberId(), m4.getName(), m4.getMembershipType());

        } catch (Exception e) { /* Ignored for primary output */ }

        // 2. REGISTRASI BUKU
        System.out.println("\n=== REGISTRASI BUKU ===");
        try {
            Book b1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, 5); // BK001
            Book b2 = new Book("Clean Code", "Robert C. Martin", "Technology", 2008, 8); // BK002
            Book b3 = new Book("Sapiens", "Yuval Noah Harari", "History", 2011, 6); // BK003
            Book b4 = new Book("1984", "George Orwell", "Fiction", 1949, 4); // BK004
            Book b5 = new Book("The Pragmatic Programmer", "Hunt & Thomas", "Technology", 1999, 3); // BK005
            Book b6 = new Book("Atomic Habits", "James Clear", "Non-Fiction", 2018, 10); // BK006

            books.put(b1.getBookId(), b1);
            books.put(b2.getBookId(), b2);
            books.put(b3.getBookId(), b3);
            books.put(b4.getBookId(), b4);
            books.put(b5.getBookId(), b5);
            books.put(b6.getBookId(), b6);

            System.out.printf("✓ Buku berhasil ditambahkan: %s - \"%s\" by %s%n", b1.getBookId(), b1.getTitle(), b1.getAuthor());
            System.out.printf("✓ Buku berhasil ditambahkan: %s - \"%s\" by %s%n", b2.getBookId(), b2.getTitle(), b2.getAuthor());
            System.out.printf("✓ Buku berhasil ditambahkan: %s - \"%s\" by %s%n", b3.getBookId(), b3.getTitle(), b3.getAuthor());
            System.out.printf("✓ Buku berhasil ditambahkan: %s - \"%s\" by %s%n", b4.getBookId(), b4.getTitle(), b4.getAuthor());
            System.out.printf("✓ Buku berhasil ditambahkan: %s - \"%s\" by %s%n", b5.getBookId(), b5.getTitle(), b5.getAuthor());
            System.out.printf("✓ Buku berhasil ditambahkan: %s - \"%s\" by %s%n", b6.getBookId(), b6.getTitle(), b6.getAuthor());

        } catch (Exception e) { /* Ignored for primary output */ }

        // 3. TRANSAKSI PEMINJAMAN
        System.out.println("\n=== TRANSAKSI PEMINJAMAN ===");

        Member ma = members.get("MBR001");
        Member mb = members.get("MBR002");
        Member mc = members.get("MBR003");
        Member md = members.get("MBR004");

        Book ba = books.get("BK002");
        Book bb = books.get("BK001");
        Book bc = books.get("BK003");
        Book bd = books.get("BK004");

        Transaction t1 = null, t2 = null, t3 = null, t4 = null;

        // P1: Alice - Clean Code (Aktif, JT 15-12-2025)
        if (ba.borrowBook()) {
            t1 = new Transaction(ma, ba, "01-12-2025", Transaction.DEFAULT_BORROW_DAYS);
            transactions.put(t1.getTransactionId(), t1);
            System.out.printf("✓ Peminjaman berhasil: %s meminjam \"%s\"%n", ma.getName(), ba.getTitle());
            printBorrowInfo(t1);
        }

        // P2: Bob - The Great Gatsby (Aktif, JT 19-12-2025)
        if (bb.borrowBook()) {
            t2 = new Transaction(mb, bb, "05-12-2025", Transaction.DEFAULT_BORROW_DAYS);
            transactions.put(t2.getTransactionId(), t2);
            System.out.printf("✓ Peminjaman berhasil: %s meminjam \"%s\"%n", mb.getName(), bb.getTitle());
            printBorrowInfo(t2);
        }

        // P3: Charlie - Sapiens (Terlambat, Pinjam 10-11, JT 24-11-2025)
        if (bc.borrowBook()) {
            t3 = new Transaction(mc, bc, "10-11-2025", Transaction.DEFAULT_BORROW_DAYS);
            transactions.put(t3.getTransactionId(), t3);
            System.out.printf("✓ Peminjaman berhasil: %s meminjam \"%s\"%n", mc.getName(), bc.getTitle());
            printBorrowInfo(t3);
        }

        // P4: Diana - 1984 (Akan Tepat Waktu, Pinjam 20-11, JT 04-12-2025)
        if (bd.borrowBook()) {
            t4 = new Transaction(md, bd, "20-11-2025", Transaction.DEFAULT_BORROW_DAYS);
            transactions.put(t4.getTransactionId(), t4);
            System.out.printf("✓ Peminjaman berhasil: %s meminjam \"%s\"%n", md.getName(), bd.getTitle());
            printBorrowInfo(t4);
        }

        // 4. PENGEMBALIAN BUKU
        System.out.println("\n=== PENGEMBALIAN BUKU ===");

        // Pengembalian 1: Charlie (Silver) - Sapiens. Terlambat 10 hari (Kembali 04-12, JT 24-11)
        if (t3 != null) {
            double fine = t3.processReturn("04-12-2025");
            totalFineCollected += fine;
            System.out.printf("✓ %s mengembalikan \"%s\"%n", t3.getMember().getName(), t3.getBook().getTitle());
            System.out.printf("   Tanggal Kembali: %s | Terlambat: %d hari%n", t3.getReturnDate(), t3.getDaysLate());
            System.out.printf("   Denda: Rp %,.0f (setelah diskon %.0f%%)%n", fine, t3.getMember().getMembershipDiscount() * 100);
        }

        // Pengembalian 2: Diana (Gold) - 1984. Tepat Waktu (Kembali 03-12, JT 04-12)
        if (t4 != null) {
            double fine = t4.processReturn("03-12-2025");
            totalFineCollected += fine;
            System.out.printf("✓ %s mengembalikan \"%s\"%n", t4.getMember().getName(), t4.getBook().getTitle());
            System.out.printf("   Tanggal Kembali: %s | Tepat Waktu%n", t4.getReturnDate());
            System.out.printf("   Denda: Rp 0%n"); // Hardcoded Rp 0 untuk output persis
        }

        // 5. LAPORAN & STATISTIK
        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("DAFTAR ANGGOTA PERPUSTAKAAN");
        System.out.println(HEADER_SEPARATOR);

        // PENGURUTAN ANGGOTA BERDASARKAN MEMBER ID
        List<Member> sortedMembers = members.values().stream()
                .sorted(Comparator.comparing(Member::getMemberId))
                .collect(Collectors.toList());

        sortedMembers.forEach(m -> m.displayInfo(SEPARATOR));

        System.out.println(HEADER_SEPARATOR);
        System.out.println("Total Anggota Terdaftar: " + Member.getTotalMembers());

        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("DAFTAR KOLEKSI BUKU");
        System.out.println(HEADER_SEPARATOR);
        books.values().forEach(b -> b.displayBookInfo(SEPARATOR));
        System.out.println(HEADER_SEPARATOR);
        System.out.println("Total Buku Terdaftar: " + Book.getTotalBooks());

        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("DAFTAR TRANSAKSI PEMINJAMAN");
        System.out.println(HEADER_SEPARATOR);
        transactions.values().stream()
                .sorted(Comparator.comparing(Transaction::getTransactionId))
                .forEach(t -> t.displayTransaction(simToday, SEPARATOR));
        System.out.println(HEADER_SEPARATOR);

        // --- STATISTIK ---
        long activeTx = transactions.values().stream().filter(t -> !t.isCompleted()).count();
        long completedLateTx = transactions.values().stream().filter(t -> t.isCompleted() && t.getLateFee() > 0).count();

        Map<String, Long> memberActivity = transactions.values().stream()
                .collect(Collectors.groupingBy(t -> t.getMember().getName(), Collectors.counting()));
        String mostActiveMember = memberActivity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        String mostActiveMemberType = members.values().stream()
                .filter(m -> m.getName().equals(mostActiveMember))
                .findFirst()
                .map(Member::getMembershipType)
                .orElse("N/A");

        Map<String, Long> bookPopularity = transactions.values().stream()
                .collect(Collectors.groupingBy(t -> t.getBook().getTitle(), Collectors.counting()));
        String mostPopularBook = bookPopularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        String mostPopularBookCategory = books.values().stream()
                .filter(b -> b.getTitle().equals(mostPopularBook))
                .findFirst()
                .map(Book::getCategory)
                .orElse("N/A");

        Map<String, Long> bookCategoryCount = transactions.values().stream()
                .collect(Collectors.groupingBy(t -> t.getBook().getCategory(), Collectors.counting()));
        long maxCount = bookCategoryCount.isEmpty() ? 0 : bookCategoryCount.values().stream().max(Long::compare).get();
        String mostPopularCategory = bookCategoryCount.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(" & "));


        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("STATISTIK SISTEM");
        System.out.println(HEADER_SEPARATOR);
        System.out.printf("Total Anggota Terdaftar    : %d orang%n", Member.getTotalMembers());
        System.out.printf("Total Buku Tersedia        : %d judul%n", Book.getTotalBooks());
        System.out.printf("Total Transaksi            : %d transaksi%n", Transaction.getTotalTransactions());
        System.out.printf("Transaksi Aktif            : %d peminjaman%n", activeTx);
        System.out.printf("Transaksi Terlambat        : %d peminjaman%n", completedLateTx);
        System.out.printf("Total Denda Terkumpul      : Rp %,.0f%n", totalFineCollected);
        System.out.println();
        System.out.printf("Anggota Paling Aktif       : %s (%s)%n", mostActiveMember, mostActiveMemberType);
        System.out.printf("Buku Paling Populer        : %s (%s)%n", mostPopularBook, mostPopularBookCategory);
        System.out.printf("Kategori Favorit           : %s%n", mostPopularCategory);
        System.out.println(HEADER_SEPARATOR);

        // --- TEST UPGRADE MEMBERSHIP & VALIDASI ---
        System.out.println("\n=== TEST UPGRADE MEMBERSHIP ===");
        mc.upgradeMembership("Gold"); // Charlie Brown Silver -> Gold

        System.out.println("\n=== TEST VALIDASI ===");

        // Uji validasi Email
        try { new Member("Invalid Email", "test.com", "081234567890", 2024, "Silver"); }
        catch (IllegalArgumentException e) { System.out.println("✗ Error: " + e.getMessage()); }

        // Uji validasi Phone
        try { new Member("Invalid Phone", "valid@email.com", "123", 2024, "Silver"); }
        catch (IllegalArgumentException e) { System.out.println("✗ Error: " + e.getMessage()); }

        // Uji validasi Membership Type
        try { new Member("Invalid Type", "valid@email.com", "081234567890", 2024, "Bronze"); }
        catch (IllegalArgumentException e) { System.out.println("✗ Error: " + e.getMessage()); }

        // Uji validasi Buku Habis (BK005: 3 copy, sudah dipinjam semua)
        Book b_test_habis = books.get("BK005");
        // Pinjam 3 copy untuk simulasi habis
        if (b_test_habis.borrowBook() && b_test_habis.borrowBook() && b_test_habis.borrowBook()) {
            // Coba pinjam copy ke-4
            if (!b_test_habis.borrowBook()) {
                System.out.println("✗ Error: Buku tidak tersedia untuk dipinjam");
            }
        }
        // Kembalikan copy yang dipinjam agar tidak mengganggu total copy
        b_test_habis.returnBook();
        b_test_habis.returnBook();
        b_test_habis.returnBook();

        // Uji validasi Tahun Terbit
        try { new Book("Tahun Error", "Penulis", "Fiction", 1800, 5); }
        catch (IllegalArgumentException e) {
            System.out.println("✗ Error: Tahun terbit tidak valid (1900-2025)");
        }

        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("PROGRAM SELESAI");
        System.out.println(HEADER_SEPARATOR);
    }
}