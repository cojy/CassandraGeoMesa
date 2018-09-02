import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Main {

	private static int maxThread = 6;
	
	private static ObjectMapper mapper = new ObjectMapper();
	private static final Map<Long, List<String>> lnsUnprocessedData = new HashMap<>();
	
	
	public static void main( String[] args ) throws IOException, InterruptedException
    {	
		
		System.out.println("Inserting initial data");
		
		InsertInitialData();
		
		System.out.println("Insertion is over");
		
		Map<String, List<LNSData2>> lExtractedData = extractData();
		
		ExecutorService execServ = Executors.newFixedThreadPool(maxThread);
		
		for(List<LNSData2> lData : lExtractedData.values()) {
			InsertThread2 t = new InsertThread2(lData);
			execServ.submit(t);
		}		
		
    	System.out.println("all threads submitted");
    	
    	execServ.shutdown();
    	execServ.awaitTermination(600, TimeUnit.SECONDS);

    	System.out.println("it's over");
    	    	
    	System.out.println("All closed. Bye Bye");
    	
    	System.exit(0);
    }
	
	private static Map<String, List<LNSData2>> extractData() {
		
		
		Map<String, List<LNSData2>> extractedData = new HashMap<>();
		for(List<String> ljson : lnsUnprocessedData.values()) {
			for(String json : ljson) {			
				try {
					LNSData2 data2 = mapper.readValue(json, LNSData2.class);
					List<LNSData2> ld2 = extractedData.get(data2.devEUI);
					if(ld2 == null) {
						ld2 = new ArrayList<>();
						extractedData.put(data2.devEUI, ld2);
					}
					ld2.add(data2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return extractedData;
	}

	private static void InsertInitialData() {
		List<LNSData2> lLNSData = new ArrayList<>();
		for(long j=0;j<500;j++) {
			lLNSData.add(new LNSData2(j, "AABBCC"+ (int) (j/10), null, null, null, 48.52, 2.14, 0.0));
		}
		
		int i = 0;
		int j = 0;
		while(i<500) {			
			TreeMap<Long, LNSData2> dataToSave = new TreeMap<>();    
			i++;
			try {    
			    for (LNSData2 lnsData : lLNSData) {
			    	j++;
			        dataToSave.put(lnsData.getId(), lnsData);
			        lnsData.setId(lnsData.getId()+i*10000);
			        String json = mapper.writeValueAsString(lnsData);
			        
			        //Configuration.getInstance().getJedis().lpush(""+ (int) System.currentTimeMillis()/1000, json);
			        
			        List<String> lTSData = lnsUnprocessedData.get((long) System.currentTimeMillis()/1000);
			        if(lTSData == null) {
			        	lTSData = new ArrayList<>();
			        	lnsUnprocessedData.put((long) System.currentTimeMillis()/1000, lTSData);
			        }
			        lTSData.add(json);
			        
			    }
			    //Thread.sleep(2000);
			    System.out.println("add-"+j);
			    //stmr.addData(dataToSave);
			    //stmr.flush();
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}		
	}
	
	public static class InsertThread2 implements Runnable{

		private final List<LNSData2> lData2;
		
		PreparedStatement prepared = Configuration.getInstance().getSession().prepare(
				  "insert into lnsdataks.LNSDATA (devEUI, id, gpsLatitude, gpsLongitude, gpsAltitude, gatewayLat, gatewayLong, gatewayAlt)"
				  + " values (?, ?, ?, ?, ?, ?, ?, ?)");
		
		public InsertThread2(List<LNSData2> lData) {
			this.lData2 = lData;
		}
		
		@Override
		public void run() {

			TreeMap<Long, LNSData> dataToSave = new TreeMap<>();
			
			
			for(LNSData2 ld2 : lData2) {
				LNSData data = new LNSData(ld2);
				dataToSave.put(ld2.getId(), data);
				//cache.remove(ld2.getId());
			
			         
				try {				
				    /*
					StringBuilder sb = new StringBuilder("BEGIN BATCH ")
					//StringBuilder sb = new StringBuilder()
						      .append("INSERT INTO ").append("LNSDATA")
						      .append("(devEUI, id, gpsLatitude, gpsLongitude, gpsAltitude, gatewayLat, gatewayLong, gatewayAlt) ")
						      .append("VALUES ('").append(data.devEUI).append("', ")
						      .append(data.getId()).append(", ")
						      .append(data.getGpsLatitude()).append(", ")
						      .append(data.getGpsLongitude()).append(", ")
						      .append(data.getGpsAltitude()).append(", ")
						      .append(data.getGatewayLat()).append(", ")
						      .append(data.getGatewayLong()).append(", ")
						      .append(data.getGatewayAlt()).append(");")
						      //*/
						      /*
						      .append("INSERT INTO ")
						      .append(TABLE_NAME_BY_TITLE).append("(id, title) ")
						      .append("VALUES (").append(book.getId()).append(", '")
						      .append(book.getTitle()).append("');")
						      .append("APPLY BATCH;");
						 
						    String query = sb.toString();
						    Configuration.getInstance().getSession().execute(query);
					//*/
					
					BoundStatement bound = prepared.bind(data.devEUI, data.getId(), data.getGpsLatitude(), data.getGpsLongitude(),
							data.getGpsAltitude(), data.getGatewayLat(), data.getGatewayLong(), data.getGatewayAlt());
					Configuration.getInstance().getSession().execute(bound);
					
				}catch(Exception e) {
					e.printStackTrace();
				}	
			    System.out.println("Inserting "+dataToSave.size()+" data");
			}
		}
	}

	public static class InsertThread implements Runnable{

		
		public InsertThread() {
		}
		
		@Override
		public void run() {
			
			List<LNSData> lLNSData = new ArrayList<>();
			for(int j=0;j<500;j++) {
				lLNSData.add(new LNSData("AABBCC"+ (int) (j/10), null, null, null, 48.52, 2.14, 0.0));
			}
			
			int i = 0;
			
			while(i<500) {		
				i++;
				TreeMap<UUID, LNSData> dataToSave = new TreeMap<>();            
				try {    
				    for (LNSData lnsData : lLNSData) {
				    	//lnsData.setId(IDGenerator.getInstance().getID());
				        dataToSave.put(lnsData.getId(), lnsData);
				        //stmr.addData(lnsData.getId(), lnsData);
				    }
				    //Thread.sleep(2000);
				    System.out.println("add-"+i);
				    //stmr.addData(dataToSave);
				    //stmr.flush();
				    System.out.println(i);
				}catch(Exception e) {
					e.printStackTrace();
				}	
			}
		}
		
	}
	
}
