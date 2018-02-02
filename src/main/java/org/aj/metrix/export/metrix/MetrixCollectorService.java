package org.aj.metrix.export.metrix;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

@Service
public class MetrixCollectorService {

	@Value("${metrix-url}")
	private String metrixUrl;
	
	private static final Counter appCounter = Counter.build().name("appCounter").labelNames("browserType").help("No. of user by browser type").register();
	
	private static final Gauge appGauge = Gauge.build().name("appGauge").labelNames("counterType").help("appGauge help text").register();

	static final Summary appSummaryValue = Summary.build().name("appSummaryValue").labelNames("summaryType").help("summaryType value help text").register();
	static final Summary appSummaryLatency = Summary.build().name("appSummaryLatency").labelNames("summaryType").help("summaryType Latency help text").register();
	
	static final Histogram appHistogram = Histogram.build().name("appHistogram").labelNames("appHistogramType").help("appHistogram help text").register();

	@Scheduled(cron = "${interval.counter.cron}")
	public void collect() {

		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> metrixMap = restTemplate.getForObject(metrixUrl, Map.class);
		
		if (null != metrixMap && metrixMap.size() > 0 ) {
			
			metrixMap.forEach((k, v) -> processMetrixData(k, v));
		}

	}	

	private void processMetrixData(String lable, String value) {
		if(lable.contains("-")) {
			String[] lableMetrixArray = lable.split("-");
			
			switch (lableMetrixArray[1]) {

			case "counter": buildCounter(lableMetrixArray[0],value);
							break;

			case "gauge": buildGauge(lableMetrixArray[0],value);
						  break;

			case "summary": buildSummary(lableMetrixArray[0],value);
							break;

			case "histogram": buildHistogram(lableMetrixArray[0],value);
							  break;
			}
			
		}else {
			buildCounter(lable, value);
		}
	}

	private void buildHistogram(String lable, String value) {
		appHistogram.labels(lable).observe(Double.parseDouble(value));		
	}

	private void buildSummary(String lable, String value) {
		appSummaryValue.labels(lable).observe(Double.parseDouble(value));		
	}

	private void buildGauge(String lable, String value) {
		appGauge.labels(lable).inc(Double.parseDouble(value));		
	}

	private void buildCounter(String lable, String value) {
		 appCounter.labels(lable).inc(Double.parseDouble(value));
	}

}
