package model;

/**
 *
 * @author Admin
 */
public class ParkingSite {

    private int siteId;
    private String siteName;
    private String address;
    private Region region;
    private State siteState;
    private State siteStatus;
    private int managerId;
    private int totalSlots;

    public ParkingSite(int siteId, String siteName, String address, Region region, State siteStatus, int managerId, int totalSlots) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.address = address;
        this.region = region;
        this.siteStatus = siteStatus;
        this.managerId = managerId;
    }

    public ParkingSite(String siteName, String address, Region region, State siteState, int managerId) {
        this.siteName = siteName;
        this.address = address;
        this.region = region;
        this.siteState = siteState;
        this.managerId = managerId;
    }

    public ParkingSite(int siteId, String siteName, String address, Region region, State siteStatus, int managerId, int totalSlots, int tmp) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.address = address;
        this.region = region;
        this.siteStatus = siteStatus;
        this.managerId = managerId;
        this.totalSlots = totalSlots;
    }
    
    public ParkingSite(int siteId, String siteName, String address, Region region, State siteState, State siteStatus, int managerId, int totalSlots) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.address = address;
        this.region = region;
        this.siteState = siteState;
        this.siteStatus = siteStatus;
        this.managerId = managerId;
        this.totalSlots = totalSlots;
    }

    public ParkingSite(int siteId, String siteName, String address, Region region, State siteState, int managerId) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.address = address;
        this.region = region;
        this.siteState = siteState;
        this.managerId = managerId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public State getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(State siteStatus) {
        this.siteStatus = siteStatus;
    }

    public State getSiteState() {
        return siteState;
    }

    public void setSiteState(State siteState) {
        this.siteState = siteState;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public enum Region {
        NORTH("Miền Bắc"),
        MIDDLE("Miền Trung"),
        SOUTH("Miền Nam");

        private final String label;

        Region(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

    }

    public enum State {
        OPERATING("Hoạt động"),
        MAINTENANCE("Bảo trì"),
        CLOSED("Đóng cửa");

        private final String label;

        private State(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
