package org.aj.metrix.export.metrix;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "metrix")
public class ConfigProp {
	private Map<String, MetrixConfig> countermap;
	
	public Map<String, MetrixConfig> getCountermap() {
		return countermap;
	}

	public void setCountermap(Map<String, MetrixConfig> countermap) {
		this.countermap = countermap;
	}

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

}
