import java.util.Scanner;
class GymSystem {

    public static  String ANSI_RED = "\u001B[31m";
    public static  String RED = "\u001B[41m";
    public static  String GREEN = "\u001B[42m";
    public static  String ANSI_GREEN = "\u001B[32m";
    public static  String ANSI_RESET = "\u001B[0m";
    public static  String YELLOW = "\u001B[43m";
    public static  String ANSI_YELLOW = "\u001B[33m";
    public static  String ANSI_BLUE = "\u001B[34m";
    public static  String ANSI_CYAN = "\u001B[35m";

    public static void welcome()
    {
        System.out.println("\033[H\033[2J");
    for (int i = 0; i<5; i++) {  
        Banner(i % 7, (i + 1) % 7);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    resetColor();
}

    private static void Banner(int textColorCode, int bgColorCode) {
        System.out.print("\u001B[3" + textColorCode + ";4" + bgColorCode + "m");
    
        System.out.print("\033[5A");
        
    
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.println("|"+String.format("%24s", "")+"      GYM MANAGEMENT SYSTEM        "+String.format("%25s", "")+"|");
        System.out.println("--------------------------------------------------------------------------------------");
    }

    private static void resetColor() {
        System.out.print("\u001B[0m");
    }

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        welcome();
        Gym g = new Gym();  
        do {
            System.out.print(RED); 
            System.out.println(ANSI_BLUE+"1. Add Member");
            System.out.println("2. Display Members");
            System.out.println("3. Search Member");
            System.out.println("4. Remove Member");
            System.out.println("5. Update Member Details");
            System.out.println("6. Display Member Statistics");
            System.out.println("7. Exit");
            System.out.print("Enter your choice:"+ANSI_RESET); 
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    g.addMember();
                    break;
                case 2:
                    Gym.displayMembers();
                    break;
                case 3:
                    Gym.searchMember();
                    break;
                case 4:
                    Gym.removeMember();
                    break;
                case 5:
                    g.updateDetails();
                    break;
                case 6:
                    Gym.displayStatistics();
                    break;
                case 7:
                    System.out.println(ANSI_RED+"Exiting..."+ANSI_RESET);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
    }
}


class GymMember {


    String name;
    int Membership_No;
    String membershipType;
    String Mobile_No;
    String email;
    String BirthDate;
    String gender;
    String address;

    GymMember(String name, int Membership_No, String membershipType, String Mobile_No, String email, String BirthDate, String gender, String address) {
        this.name = name;
        this.Membership_No = Membership_No;
        this.membershipType = membershipType;
        this.Mobile_No = Mobile_No;
        this.email = email;
        this.BirthDate = BirthDate;
        this.gender = gender;
        this.address = address;
    }

    void display() {
        System.out.println(String.format("%18s", "") + "Name: " + name);
        System.out.println(String.format("%18s", "") + "Membership Number: " + Membership_No);
        System.out.println(String.format("%18s", "") + "Membership Type: " + membershipType);
        System.out.println(String.format("%18s", "") + "Contact Number: " + Mobile_No);
        System.out.println(String.format("%18s", "") + "Email: " + email);
        System.out.println(String.format("%18s", "") + "Date of Birth: " + BirthDate);
        System.out.println(String.format("%18s", "") + "Gender: " + gender);
        System.out.println(String.format("%18s", "") + "Address: " + address);
        System.out.println();
    }
}

class Gym {
    public static  String ANSI_RED = "\u001B[31m";
    public static  String ANSI_RESET = "\u001B[0m";

    static Scanner sc = new Scanner(System.in);
    static GymMember[] members = new GymMember[100];  
    static int nextMembership_No = 1;
    int Membership_No;
    String membershipType;
    String gender;
    String Mobile_No;

    void addMember() {
        System.out.println("Enter member name:");
        String name = sc.nextLine();
        System.out.println("Membership types :\n basic - 5000 \n premium - 7500 \n elite - 10000");
        System.out.println("Enter membership type:");
        membershipType = sc.nextLine();
        setMembershipType(membershipType);
        System.out.println("Enter contact number:");
        Mobile_No = sc.nextLine();
        validNumber(Mobile_No);
        System.out.println("Enter email:");
        String email = sc.nextLine();
        System.out.println("Enter date of birth:");
        String BirthDate = sc.nextLine();
        System.out.println("Enter gender:");
        gender = sc.nextLine();
        setGender(gender);

        System.out.println("Enter address:");
        String address = sc.nextLine();
        getMembership_No();
        System.out.println("Your membership number is " + Membership_No);

        GymMember member = new GymMember(name, Membership_No, membershipType, Mobile_No, email, BirthDate, gender, address);
        members[nextMembership_No - 1] = member;
        nextMembership_No++;
    }

    void getMembership_No() {
        this.Membership_No = nextMembership_No;
    }

    void setMembershipType(String Mt) {
        while (!(Mt.equalsIgnoreCase("Basic") || Mt.equalsIgnoreCase("Premium") || Mt.equalsIgnoreCase("Elite"))) {
            System.out.println(ANSI_RED+"You entered wrong membership type"+ANSI_RESET);
            System.out.println("Re-enter membership type:");
            Mt= sc.nextLine();
        }
        this.membershipType = Mt;
    }

    void setGender(String gender1) {
        while (!(gender1.equalsIgnoreCase("male") || gender1.equalsIgnoreCase("female") || gender1.equalsIgnoreCase("other"))) {
            System.out.println(ANSI_RED+"You entered wrong gender details"+ANSI_RESET);
            System.out.println("Re-enter gender details :");
            gender1 = sc.nextLine();
        }
        this.gender = gender1;
    }

    void validNumber(String Mobile_No) {
        while (!isValidMobile(Mobile_No)) {
            System.out.println(ANSI_RED+"Enter a valid 10 digit mobile no containing only 10 digits"+ANSI_RESET);
            Mobile_No = sc.nextLine();
        }
        this.Mobile_No = Mobile_No;
    }

    boolean isValidMobile(String mobile) {
        if (mobile.length() != 10) {
            return false;
        } else {
            for (int i = 0; i < mobile.length(); i++) {
                if (mobile.charAt(i) < '0' || mobile.charAt(i) > '9') {
                    return false;
                }
            }
        }
        return true;
    }

    static void displayMembers() {
        System.out.println("Members:");
        for (GymMember member : members) {
            if (member != null) {
                member.display();
            }
        }
    }

    static void searchMember() {
        System.out.println("Enter membership number to search:");
        int Membership_No = sc.nextInt();
        sc.nextLine();
        for (GymMember member : members) {
            if (member != null && member.Membership_No == Membership_No) {
                member.display();
                break;
            }
        }
    }

    static void removeMember() {
        System.out.println("Enter membership number to remove:");
        int Membership_No = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null && members[i].Membership_No == Membership_No) {
                members[i] = null;
                break;
            }
        }
    }

    void updateDetails() {
        System.out.println("Enter membership number to update:");
        int Membership_No = sc.nextInt();
        sc.nextLine();
        int choice2;
        for (int i = 0; i < members.length; i++) {
            if (members[i] != null && members[i].Membership_No == Membership_No) {
                System.out.println(" 1. Change member name");
                System.out.println(" 2. Change membership type");
                System.out.println(" 3. Change contact number");
                System.out.println(" 4. Change email");
                System.out.println(" 5. Change date of birth");
                System.out.println(" 6. Change gender");
                System.out.println(" 7. Change address");
                System.out.println("Enter your choice:");
                choice2 = sc.nextInt();
                sc.nextLine();
                switch (choice2) {
                    case 1:
                        System.out.println("Enter new name:");
                        String newName = sc.nextLine();
                        members[i].name = newName;
                        break;

                    case 2:
                        System.out.println("Membership types :\n basic - 5000 \n premium - 7500 \n elite - 10000");
                        System.out.println("Enter new membership type:");
                        String membershipType = sc.nextLine();
                        setMembershipType(membershipType);
                        members[i].membershipType = this.membershipType;
                        break;

                    case 3:
                        System.out.println("Enter new contact number:");
                        String Mobile_No = sc.nextLine();
                        validNumber(Mobile_No);
                        members[i].Mobile_No = this.Mobile_No;
                        break;

                    case 4:
                        System.out.println("Enter new email:");
                        String newEmail = sc.nextLine();
                        members[i].email = newEmail;
                        break;

                    case 5:
                        System.out.println("Enter new date of birth:");
                        String newBirthDate = sc.nextLine();
                        members[i].BirthDate = newBirthDate;
                        break;

                    case 6:
                        System.out.println("Enter new gender:");
                        String gender = sc.nextLine();
                        setGender(gender);
                        members[i].gender = this.gender;
                        break;

                    case 7:
                        System.out.println("Enter new address:");
                        String newAddress = sc.nextLine();
                        members[i].address = newAddress;
                        break;

                    default:
                        System.out.println(ANSI_RED+"Please enter a valid choice"+ANSI_RESET);
                }
            }
        }
    }

    static void displayStatistics() {
        int basicCount = 0;
        int premiumCount = 0;
        int eliteCount = 0;
        for (GymMember member : members) {
            if (member != null) {
                if (member.membershipType.equalsIgnoreCase("Basic")) {
                    basicCount++;
                } else if (member.membershipType.equalsIgnoreCase("Premium")) {
                    premiumCount++;
                } else if (member.membershipType.equalsIgnoreCase("Elite")) {
                    eliteCount++;
                }
            }
        }
        System.out.println("Membership Statistics:");
        System.out.println("Basic: " + basicCount);
        System.out.println("Premium: " + premiumCount);
        System.out.println("Elite: " + eliteCount);
    }
}