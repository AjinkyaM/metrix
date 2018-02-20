package org.aj.metrix.export.metrix;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "metrix")
public class ConfigProp {
	private Map<String, MetrixConfig> counter;
	private Map<String, MetrixConfig> gauge;
	
	public static class MetrixConfig{
		private String name;
		private String lable;
		private String url;
		private String helpText;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLable() {
			return lable;
		}
		public String getUrl() {
			return url;
		}
		public void setLable(String lable) {
			this.lable = lable;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getHelpText() {
			return helpText;
		}
		public void setHelpText(String helpText) {
			this.helpText = helpText;
		}
	}

	public Map<String, MetrixConfig> getCounter() {
		return counter;
	}

	public void setCounter(Map<String, MetrixConfig> counter) {
		this.counter = counter;
	}

	public Map<String, MetrixConfig> getGauge() {
		return gauge;
	}

	public void setGauge(Map<String, MetrixConfig> gauge) {
		this.gauge = gauge;
	}

}
