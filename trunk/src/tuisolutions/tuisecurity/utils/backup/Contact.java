package tuisolutions.tuisecurity.utils.backup;

public class Contact extends Resources {
    
    private String contactId;
    private Phone phoneNumber;
    private StructuredName phoneName;
    private Address address;
    private Email email;
    
    public Contact(String contactId, Phone phoneNumber, StructuredName phoneName, Address address, Email email) {
        super();
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.phoneName = phoneName;
        this.address = address;
        this.email = email;
    }
    
    public String getContactId() {
        return contactId;
    }
    
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
    
    public Phone getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(Phone phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Email getEmail() {
        return email;
    }
    
    public void setEmail(Email email) {
        this.email = email;
    }
    
    public StructuredName getPhoneName() {
        return phoneName;
    }
    
    public void setPhoneName(StructuredName phoneName) {
        this.phoneName = phoneName;
    }
}
