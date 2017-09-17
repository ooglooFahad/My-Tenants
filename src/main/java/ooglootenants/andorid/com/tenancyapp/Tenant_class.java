package ooglootenants.andorid.com.tenancyapp;

public class Tenant_class {
    private String R_id, L_id, Renter_Name, Renter_Phoneno, Renter_Email, Renter_Paymentdate;
    private String Rent_Amount, Rent_Security, Renter_CNIC, Payment_Received, Notifications, RentNotificationDate;

    public Tenant_class(String Rid, String Lid, String name, String phone, String email, String cnic, String paymentdate, String rent, String security, String received, String notification, String rentNotificationDate) {
        this.R_id = Rid;
        this.L_id = Lid;
        this.Renter_Name = name;
        this.Renter_Phoneno = phone;
        this.Renter_Email = email;
        this.Renter_CNIC = cnic;
        this.Renter_Paymentdate = paymentdate;
        this.Rent_Amount = rent;
        this.Rent_Security = security;
        this.Payment_Received = received;
        this.Notifications = notification;
        this.RentNotificationDate = rentNotificationDate;
    }

    public String getR_id() {
        return R_id;
    }

    public void setR_id(String r_id) {
        R_id = r_id;
    }

    public String getL_id() {
        return L_id;
    }

    public void setL_id(String l_id) {
        L_id = l_id;
    }


    public String getRenter_Name() {
        return Renter_Name;
    }

    public void setRenter_Name(String renter_Name) {
        Renter_Name = renter_Name;
    }

    public String getRenter_Phoneno() {
        return Renter_Phoneno;
    }

    public void setRenter_Phoneno(String renter_Phoneno) {
        Renter_Phoneno = renter_Phoneno;
    }

    public String getRenter_Email() {
        return Renter_Email;
    }

    public void setRenter_Email(String renter_Email) {
        Renter_Email = renter_Email;
    }

    public String getRenter_Paymentdate() {
        return Renter_Paymentdate;
    }

    public void setRenter_Paymentdate(String renter_Paymentdate) {
        Renter_Paymentdate = renter_Paymentdate;
    }

    public String getRent_Amount() {
        return Rent_Amount;
    }

    public void setRent_Amount(String rent_Amount) {
        Rent_Amount = rent_Amount;
    }

    public String getRent_Security() {
        return Rent_Security;
    }

    public void setRent_Security(String rent_Security) {
        Rent_Security = rent_Security;
    }

    public String getRenter_CNIC() {
        return Renter_CNIC;
    }

    public void setRenter_CNIC(String renter_CNIC) {
        Renter_CNIC = renter_CNIC;
    }

    public String getPayment_Received() {
        return Payment_Received;
    }

    public void setPayment_Received(String payment_Received) {
        Payment_Received = payment_Received;
    }

    public String getNotifications() {
        return Notifications;
    }

    public void setNotifications(String notifications) {
        Notifications = notifications;
    }

    public String getRentNotificationDate() {
        return RentNotificationDate;
    }

    public void setRentNotificationDate(String rentNotificationDate) {
        RentNotificationDate = rentNotificationDate;
    }
}
