public class Counselor {
    private final int id;
    private final String gmail;
    private final String name;
    private final String department;
    private final String schedule; // Updated to use 'schedule'
    private final String profilePic;

    public Counselor(int id, String gmail, String name, String department, String schedule, String profilePic) {
        this.id = id;
        this.gmail = gmail;
        this.name = name;
        this.department = department;
        this.schedule = schedule; // Updated to use 'schedule'
        this.profilePic = profilePic;
    }

    public int getId() {
        return id;
    }

    public String getGmail() {
        return gmail;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getSchedule() {
        return schedule; // Updated to use 'schedule'
    }

    public String getProfilePic() {
        return profilePic;
    }
}
