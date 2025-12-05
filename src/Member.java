import java.time.LocalDate;

class Member {
    private static int totalMembers = 0;
    private static final int CURRENT_YEAR = LocalDate.now().getYear();

    private String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private int registrationYear;
    private String membershipType;

    public Member() {
        totalMembers++;
        this.memberId = String.format("MBR%03d", totalMembers);
    }

    public Member(String name, String email, String phoneNumber, int registrationYear, String membershipType) {
        this();
        // Setter dipanggil untuk validasi dan penentuan nilai awal
        setName(name);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRegistrationYear(registrationYear);
        setMembershipType(membershipType);
    }

    // --- Getters ---
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getMembershipType() { return membershipType; }
    public static int getTotalMembers() { return totalMembers; }
    public int getMembershipDuration() { return CURRENT_YEAR - this.registrationYear; }
    public int getMaxBorrowLimit() {
        return switch (membershipType) {
            case "Platinum" -> 10;
            case "Gold" -> 7;
            default -> 5; // Silver
        };
    }
    public double getMembershipDiscount() {
        return switch (membershipType) {
            case "Platinum" -> 0.50; // 50%
            case "Gold" -> 0.30; // 30%
            default -> 0.10; // 10% (Silver)
        };
    }

    // --- Setters (untuk validasi) ---
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Email tidak valid (harus mengandung @ dan .)");
        }
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10,13}")) {
            throw new IllegalArgumentException("Nomor telepon harus 10-13 digit");
        }
        this.phoneNumber = phoneNumber;
    }
    public void setRegistrationYear(int year) { this.registrationYear = year; }
    public void setMembershipType(String membershipType) {
        if (membershipType == null || (!membershipType.equals("Silver") && !membershipType.equals("Gold") && !membershipType.equals("Platinum"))) {
            throw new IllegalArgumentException("Membership type harus Silver/Gold/Platinum");
        }
        this.membershipType = membershipType;
    }

    // --- Methods ---
    public void displayInfo(String separator) {
        String star = switch (membershipType) {
            case "Platinum" -> "⭐⭐⭐";
            case "Gold" -> "⭐⭐";
            default -> "⭐";
        };

        // Menggunakan padding space
        System.out.printf("[%s] %s%n", memberId, name);
        System.out.printf("Email\u00a0          : %s%n", this.email);
        System.out.printf("Phone\u00a0          : %s%n", this.phoneNumber);
        System.out.printf("Membership    : %s %s%n", membershipType, star);
        System.out.printf("Tahun Daftar  : %d%n", this.registrationYear);
        System.out.printf("Durasi Member : %d tahun%n", getMembershipDuration());
        System.out.printf("Batas Pinjam  : %d buku%n", getMaxBorrowLimit());
        System.out.printf("Diskon Denda  : %.0f%%%n", getMembershipDiscount() * 100);
        System.out.println(separator);
    }

    public void upgradeMembership(String newType) {
        String current = this.membershipType;
        try {
            setMembershipType(newType);

            // Output persis
            System.out.printf("✓ %s berhasil di-upgrade dari %s ke %s!%n", name, current, newType);
            System.out.printf("  Batas Pinjam Baru: %d buku | Diskon Denda Baru: %.0f%%%n", getMaxBorrowLimit(), getMembershipDiscount() * 100);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: Gagal upgrade membership.");
        }
    }
}