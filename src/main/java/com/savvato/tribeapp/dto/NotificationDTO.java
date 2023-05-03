public class NotificationDTO {
    private String description;
    private String body;
    private LocalDateTime lastUpdatedDate;
    private String iconUrl;

    public NotificationDTO(String description, String body, LocalDateTime lastUpdatedDate, String iconUrl) {
        this.description = description;
        this.body = body;
        this.lastUpdatedDate = lastUpdatedDate;
        this.iconUrl = iconUrl;
    }

    // getters and setters
}
