package tuisolutions.tuisecurity.models;

import android.location.Address;

public class SIM {
    
    private String serial;
    private String number;
    private String networkProvider;
    private Address address;
    
    public SIM() {
    }
    
    public String getSerial() {
        return serial;
    }
    
    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getNetworkProvider() {
        return networkProvider;
    }
    
    public void setNetworkProvider(String networkProvider) {
        this.networkProvider = networkProvider;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
}
