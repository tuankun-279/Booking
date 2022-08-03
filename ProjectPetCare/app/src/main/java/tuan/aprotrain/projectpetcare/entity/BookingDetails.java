package tuan.aprotrain.projectpetcare.entity;

public class BookingDetails {
    private long bookingId;
    private long serviceId;

    public BookingDetails(){}

    public BookingDetails(long bookingId, long serviceId) {
        this.bookingId = bookingId;
        this.serviceId = serviceId;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }
}
