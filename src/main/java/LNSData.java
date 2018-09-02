import java.util.UUID;

public class LNSData {
    public final UUID id;

    public String devEUI;
	
	private Double gpsLatitude;

	private Double gpsLongitude;
	
	private Double gpsAltitude;

	private	Double gatewayLat;

	private Double gatewayLong;

	private Double gatewayAlt;	
    
	public LNSData(String devEUI, Double gpsLatitude, Double gpsLongitude, Double gpsAltitude, Double gatewayLat, Double gatewayLong,
			Double gatewayAlt) {
		super();
		this.id = UUID.randomUUID();
		this.devEUI = devEUI;
		this.gpsLatitude = gpsLatitude==null?0:gpsLatitude;
		this.gpsLongitude = gpsLongitude==null?0:gpsLongitude;
		this.gpsAltitude = gpsAltitude==null?0.0:gpsAltitude;
		this.gatewayLat = gatewayLat;
		this.gatewayLong = gatewayLong;
		this.gatewayAlt = gatewayAlt;
				
	}
	
	public LNSData(UUID id, String devEUI, Double gpsLatitude, Double gpsLongitude, Double gpsAltitude, Double gatewayLat, Double gatewayLong,
			Double gatewayAlt) {
		super();
		this.id = id;
		this.devEUI = devEUI;
		this.gpsLatitude = gpsLatitude==null?0:gpsLatitude;
		this.gpsLongitude = gpsLongitude==null?0:gpsLongitude;
		this.gpsAltitude = gpsAltitude==null?0.0:gpsAltitude;
		this.gatewayLat = gatewayLat;
		this.gatewayLong = gatewayLong;
		this.gatewayAlt = gatewayAlt;
				
	}
	
	public LNSData(LNSData2 data2) {
		super();
		this.id = UUID.randomUUID();
		this.devEUI = data2.getDevEUI();
		this.gpsLatitude = data2.getGpsLatitude();
		this.gpsLongitude = data2.getGpsLongitude();
		this.gpsAltitude = data2.getGpsAltitude();
		this.gatewayLat = data2.getGatewayLat();
		this.gatewayLong = data2.getGatewayLong();
		this.gatewayAlt = data2.getGatewayAlt();
				
	}

	public UUID getId() {
		return id;
	}

	public String getDevEUI() {
		return devEUI;
	}

	public void setDevEUI(String devEUI) {
		this.devEUI = devEUI;
	}

	public Double getGpsLatitude() {
		return gpsLatitude;
	}

	public void setGpsLatitude(Double gpsLatitude) {
		this.gpsLatitude = gpsLatitude;
	}

	public Double getGpsLongitude() {
		return gpsLongitude;
	}

	public void setGpsLongitude(Double gpsLongitude) {
		this.gpsLongitude = gpsLongitude;
	}

	public Double getGpsAltitude() {
		return gpsAltitude;
	}

	public void setGpsAltitude(Double gpsAltitude) {
		this.gpsAltitude = gpsAltitude;
	}

	public Double getGatewayLat() {
		return gatewayLat;
	}

	public void setGatewayLat(Double gatewayLat) {
		this.gatewayLat = gatewayLat;
	}

	public Double getGatewayLong() {
		return gatewayLong;
	}

	public void setGatewayLong(Double gatewayLong) {
		this.gatewayLong = gatewayLong;
	}

	public Double getGatewayAlt() {
		return gatewayAlt;
	}

	public void setGatewayAlt(Double gatewayAlt) {
		this.gatewayAlt = gatewayAlt;
	}
	
    
}
