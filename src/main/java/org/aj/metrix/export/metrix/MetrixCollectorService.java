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
import io.prometheus.client.Gauge;

@Service
public class MetrixCollectorService {
	
	@Autowired
	private ConfigProp configProp;
	
	private Map<String, Counter> counterMap = new HashMap<>();
	private Map<String, Gauge> gaugeMap = new HashMap<>();
	
	@PostConstruct
	public void init(){
		Map<String, MetrixConfig> counters = configProp.getCounter();
		counters.forEach((k,v)-> this.getCounter(v));
		
		Map<String, MetrixConfig> gauges = configProp.getGauge();
		gauges.forEach((k,v)-> this.getGauge(v));
	 }
	
	@Scheduled(cron = "${interval.counter.cron}")
	public void collect() {
		Map<String, MetrixConfig> counters = configProp.getCounter();
		counters.forEach((k,v)-> this.getCounterValue(v));
		
		Map<String, MetrixConfig> gauges = configProp.getGauge();
		gauges.forEach((k,v)-> this.getGaugeValue(v));
	}

	private void getCounterValue(MetrixConfig metrixConfig) {
		Map<String, String> metrixMap = getMetrixMap(metrixConfig);
		
		if (null != metrixMap && metrixMap.size() > 0 ) {
			
			metrixMap.forEach((lable, value) -> counterMap.get(metrixConfig.getName()).labels(lable).inc(Double.parseDouble(value)) );
		}
	}
	
	private void getGaugeValue(MetrixConfig metrixConfig) {
		Map<String, String> metrixMap = getMetrixMap(metrixConfig);
		
		if (null != metrixMap && metrixMap.size() > 0 ) {
			
			metrixMap.forEach((lable, value) -> gaugeMap.get(metrixConfig.getName()).labels(lable).set(Double.parseDouble(value)) );
		}
	}

	private Map<String, String> getMetrixMap(MetrixConfig metrixConfig) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> metrixMap = restTemplate.getForObject(metrixConfig.getUrl(), Map.class);
		return metrixMap;
	}
	
	
	
	private void getCounter(MetrixConfig metrixConfig) {
		Counter appCounter = Counter.build().name(metrixConfig.getName()).labelNames(metrixConfig.getLable()).help(metrixConfig.getHelpText()).register();
		counterMap.put(metrixConfig.getName(), appCounter);
	}
	
	private void getGauge(MetrixConfig metrixConfig) {
		Gauge appGauge = Gauge.build().name(metrixConfig.getName()).labelNames(metrixConfig.getLable()).help(metrixConfig.getHelpText()).register();
		gaugeMap.put(metrixConfig.getName(), appGauge);
	}
}
