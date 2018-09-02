import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Session;

import redis.clients.jedis.Jedis;

public class Configuration {

	private static final Configuration instance = new Configuration();
	
	private Jedis jedis;	

	private Cluster cluster;
	 
    private Session session;
	
	private Configuration() {
		//jedis = new Jedis("localhost");
	    this.connect("127.0.0.1", 9042);
		
		this.createKeyspace("lnsdataks", "SimpleStrategy", 1);
	
		this.createTable();
	}

	public static Configuration getInstance() {
		return instance;
	}

	public Jedis getJedis() {
		return jedis;
	}
 
    public void connect(String node, Integer port) {
        Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
 
        session = cluster.connect();
    }
 
    public Session getSession() {
        return this.session;
    }
 
    public void close() {
        session.close();
        cluster.close();
    }
	
    public void createKeyspace(
    		  String keyspaceName, String replicationStrategy, int replicationFactor) {
    		  StringBuilder sb = 
    		    new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
    		      .append(keyspaceName).append(" WITH replication = {")
    		      .append("'class':'").append(replicationStrategy)
    		      .append("','replication_factor':").append(replicationFactor)
    		      .append("};");
    		         
    		    String query = sb.toString();
    		    session.execute(query);
    		}
    
    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
          .append("lnsdataks.LNSDATA").append("(")
          .append("devEUI text,")
          .append("id uuid, ")
          .append("gpsLatitude double,")
          .append("gpsLongitude double,")
          .append("gpsAltitude double,")
          .append("gatewayLat double,")
          .append("gatewayLong double,")
          .append("gatewayAlt double, ")
          .append("PRIMARY KEY (devEUI, id));");
     
        String query = sb.toString();
        session.execute(query);
    }

	
}
