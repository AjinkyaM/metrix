package org.aj.metrix.export.metrix;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.aj.metrix.export.metrix.ConfigProp.MetrixConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.prometheus.client.Counter;

@Service
public class MetrixCollectorService {
	
	@Autowired
	private ConfigProp configProp;
	
	private Map<String, Counter> counterMap = new HashMap<>();
	
	@PostConstruct
	public void init(){
		Map<String, MetrixConfig> countermap = configProp.getCountermap();
		countermap.forEach((k,v)-> this.getCounter(v));
	 }
	
	@Scheduled(cron = "${interval.counter.cron}")
	public void collect() {
		Map<String, MetrixConfig> countermap = configProp.getCountermap();
		countermap.forEach((k,v)-> this.collectValue(v));
	}

	private void collectValue(MetrixConfig metrixConfig) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> metrixMap = restTemplate.getForObject(metrixConfig.getUrl(), Map.class);
		
		if (null != metrixMap && metrixMap.size() > 0 ) {
			
			metrixMap.forEach((lable, value) -> buildCounter(metrixConfig.getName(),lable, value));
		}
	}

	private void buildCounter(String counterName, String lable, String value) {
		counterMap.get(counterName).labels(lable).inc(Double.parseDouble(value));
	}
	
	private void getCounter(MetrixConfig metrixConfig) {
		Counter appCounter = Counter.build().name(metrixConfig.getName()).labelNames(metrixConfig.getLable()).help(metrixConfig.getHelpText()).register();
		counterMap.put(metrixConfig.getName(), appCounter);
	}

	public Map<String, Counter> getCounterMap() {
		return counterMap;
	}

	public void setCounterMap(Map<String, Counter> counterMap) {
		this.counterMap = counterMap;
	}

}
